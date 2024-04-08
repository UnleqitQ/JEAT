package com.unleqitq.jeat.genetics.genome;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.config.CrossoverConfig;
import com.unleqitq.jeat.config.InitialStructureConfig;
import com.unleqitq.jeat.config.MutationConfig;
import com.unleqitq.jeat.genetics.gene.connection.ConnectionGene;
import com.unleqitq.jeat.genetics.gene.connection.ConnectionGeneDefinition;
import com.unleqitq.jeat.genetics.gene.connection.ConnectionIdentifier;
import com.unleqitq.jeat.genetics.gene.node.NodeGene;
import com.unleqitq.jeat.genetics.gene.node.bias.BiasNodeGene;
import com.unleqitq.jeat.genetics.gene.node.bias.BiasNodeGeneDefinition;
import com.unleqitq.jeat.genetics.gene.node.input.InputNodeGeneDefinition;
import com.unleqitq.jeat.genetics.gene.node.working.WorkingNodeGene;
import com.unleqitq.jeat.genetics.gene.node.working.WorkingNodeGeneDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Accessors (fluent = true)
@Getter
public class Genome implements Comparable<Genome> {
	
	@NotNull
	private final Jeat jeat;
	@NotNull
	private final UUID id;
	@Nullable
	private BiasNodeGene bias;
	@NotNull
	private final Map<UUID, NodeGene<?, ?>> nodes = new TreeMap<>();
	@NotNull
	private final Map<String, NodeGene<?, ?>> namedNodes = new HashMap<>();
	@NotNull
	private final Map<ConnectionIdentifier, ConnectionGene> connections = new TreeMap<>();
	@Setter
	private double fitness = 0;
	
	public Genome(@NotNull Jeat jeat, @NotNull UUID id) {
		this.jeat = jeat;
		this.id = id;
	}
	
	public Genome(@NotNull Jeat jeat) {
		this(jeat, UUID.randomUUID());
	}
	
	public void init() {
		Random rnd = jeat.random();
		InitialStructureConfig cfg = jeat.config().initialStructure;
		List<UUID> inputNodes = new ArrayList<>();
		List<UUID> outputNodes = new ArrayList<>();
		{
			NodeGene<?, ?> node = jeat.nodeDefinitions()
				.createGene("bias", this, () -> new BiasNodeGeneDefinition(-1, "bias"));
			addNode(node);
			inputNodes.add(node.id());
		}
		for (InitialStructureConfig.InputNodeConfig nc : cfg.inputNodes) {
			NodeGene<?, ?> node = jeat.nodeDefinitions()
				.createGene(nc.name, this, () -> new InputNodeGeneDefinition(nc.x, nc.name));
			addNode(node);
			inputNodes.add(node.id());
		}
		List<ConnectionIdentifier> possibleConnections = new ArrayList<>();
		for (InitialStructureConfig.OutputNodeConfig nc : cfg.outputNodes) {
			NodeGene<?, ?> node = jeat.nodeDefinitions()
				.createGene(nc.name, this,
					() -> new WorkingNodeGeneDefinition(nc.x, nc.name).removable(false)
						.lockedActivationFunction(nc.lockedActivationFunction)
						.lockedAggregationFunction(nc.lockedAggregationFunction)
						.canDisable(nc.canDisable));
			addNode(node);
			outputNodes.add(node.id());
			for (UUID input : inputNodes) {
				possibleConnections.add(new ConnectionIdentifier(input, node.id()));
			}
		}
		{
			int cnt = (int) (cfg.connectionDensity * possibleConnections.size());
			for (int i = 0; i < cnt; i++) {
				ConnectionIdentifier id =
					possibleConnections.remove(rnd.nextInt(possibleConnections.size()));
				if (connections.containsKey(id)) {
					continue;
				}
				ConnectionGene con =
					jeat.connectionDefinitions().createGene(id, this, () -> new ConnectionGeneDefinition(id));
				addConnection(con);
			}
		}
	}
	
	private void addNode(@NotNull NodeGene<?, ?> node) {
		if (node instanceof BiasNodeGene) {
			if (bias != null) {
				throw new IllegalStateException("Bias node already exists");
			}
			bias = (BiasNodeGene) node;
		}
		nodes.put(node.id(), node);
		if (node.name() != null) {
			namedNodes.put(node.name(), node);
		}
	}
	
	private void addConnection(@NotNull ConnectionGene connection) {
		connections.put(connection.id(), connection);
	}
	
	private void removeNode(@NotNull UUID id) {
		NodeGene<?, ?> node = nodes.get(id);
		if (node != null) {
			removeNode(node);
		}
	}
	
	private void removeNode(@NotNull NodeGene<?, ?> node) {
		nodes.remove(node.id());
		if (node.name() != null) {
			namedNodes.remove(node.name());
		}
	}
	
	private void removeConnection(@NotNull ConnectionIdentifier id) {
		connections.remove(id);
	}
	
	private void removeConnection(@NotNull ConnectionGene connection) {
		removeConnection(connection.id());
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		Genome genome = (Genome) o;
		return id.equals(genome.id);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public int compareTo(@NotNull Genome o) {
		return id.compareTo(o.id);
	}
	
	@Nullable
	public NodeGene<?, ?> node(@NotNull UUID id) {
		return nodes.get(id);
	}
	
	@Nullable
	public NodeGene<?, ?> node(@NotNull String name) {
		return namedNodes.get(name);
	}
	
	@Nullable
	public ConnectionGene connection(@NotNull ConnectionIdentifier id) {
		return connections.get(id);
	}
	
	public void mutate() {
		Random rnd = jeat.random();
		MutationConfig cfg = jeat.config().mutation;
		nodes.values().forEach(NodeGene::mutate);
		connections.values().forEach(ConnectionGene::mutate);
		{
			MutationConfig.ConnectionMutationConfig.ConnectionStructureMutationConfig con =
				cfg.connection.structure;
			if (rnd.nextDouble() < con.addConnectionChance) {
				for (int i = 0; i < con.addConnectionAttempts; i++) {
					NodeGene<?, ?> n1 = getRandomNode(true, true);
					if (n1 == null) break;
					NodeGene<?, ?> n2 = getRandomNode(!n1.input(), true);
					if (n2 == null) continue;
					if (n1.x() == n2.x()) {
						continue;
					}
					NodeGene<?, ?> from = n1.x() < n2.x() ? n1 : n2;
					NodeGene<?, ?> to = n1.x() < n2.x() ? n2 : n1;
					ConnectionIdentifier id = new ConnectionIdentifier(from.id(), to.id());
					if (connections.containsKey(id)) {
						continue;
					}
					ConnectionGene conGene = jeat.connectionDefinitions()
						.createGene(id, this, () -> new ConnectionGeneDefinition(id));
					addConnection(conGene);
					break;
				}
			}
			if (rnd.nextDouble() < con.removeConnectionChance) {
				if (!connections.isEmpty()) {
					ConnectionIdentifier id =
						new ArrayList<>(connections.keySet()).get(rnd.nextInt(connections.size()));
					removeConnection(id);
				}
			}
		}
		{
			MutationConfig.NodeMutationConfig.NodeStructureMutationConfig nd = cfg.node.structure;
			if (rnd.nextDouble() < nd.removeNodeChance) {
				NodeGene<?, ?> node = getRandomNode(true, false);
				if (node != null) {
					node.connections().forEach(this::removeConnection);
					removeNode(node);
				}
			}
			if (rnd.nextDouble() < nd.add.combineInputsChance) {
				NodeGene<?, ?> n1 = getRandomNode(false, true);
				if (n1 != null) {
					Collection<ConnectionGene> cons = n1.inputs();
					if (cons.size() > 1) {
						double maxX = cons.stream().mapToDouble(c -> c.from().x()).max().orElse(0);
						double newX = (n1.x() + maxX) / 2;
						WorkingNodeGeneDefinition def = new WorkingNodeGeneDefinition(newX);
						WorkingNodeGene n2 = def.createGene(this);
						jeat.nodeDefinitions().add(def);
						addNode(n2);
						cons.forEach(c -> {
							ConnectionIdentifier id = new ConnectionIdentifier(c.from().id(), n2.id());
							ConnectionGene conGene = jeat.connectionDefinitions()
								.createGene(id, this, () -> new ConnectionGeneDefinition(id))
								.weight(c.weight());
							addConnection(conGene);
							removeConnection(c.id());
						});
						ConnectionGene conGene = jeat.connectionDefinitions()
							.createGene(new ConnectionIdentifier(n2.id(), n1.id()), this,
								() -> new ConnectionGeneDefinition(new ConnectionIdentifier(n2.id(), n1.id())))
							.weight(1);
						addConnection(conGene);
					}
				}
			}
			if (rnd.nextDouble() < nd.add.combineOutputsChance) {
				NodeGene<?, ?> n1 = getRandomNode(true, true);
				if (n1 != null) {
					Collection<ConnectionGene> cons = n1.outputs();
					if (cons.size() > 1) {
						double minX = cons.stream().mapToDouble(c -> c.to().x()).min().orElse(0);
						double newX = (n1.x() + minX) / 2;
						WorkingNodeGeneDefinition def = new WorkingNodeGeneDefinition(newX);
						WorkingNodeGene n2 = def.createGene(this);
						jeat.nodeDefinitions().add(def);
						addNode(n2);
						cons.forEach(c -> {
							ConnectionIdentifier id = new ConnectionIdentifier(n2.id(), c.to().id());
							ConnectionGene conGene = jeat.connectionDefinitions()
								.createGene(id, this, () -> new ConnectionGeneDefinition(id))
								.weight(c.weight());
							addConnection(conGene);
							removeConnection(c.id());
						});
						ConnectionGene conGene = jeat.connectionDefinitions()
							.createGene(new ConnectionIdentifier(n1.id(), n2.id()), this,
								() -> new ConnectionGeneDefinition(new ConnectionIdentifier(n1.id(), n2.id())))
							.weight(1);
						addConnection(conGene);
					}
				}
			}
			if (rnd.nextDouble() < nd.add.splitChance) {
				ConnectionGene con = getRandomConnection();
				if (con != null) {
					NodeGene<?, ?> from = con.from();
					NodeGene<?, ?> to = con.to();
					double newX = (from.x() + to.x()) / 2;
					WorkingNodeGeneDefinition def = new WorkingNodeGeneDefinition(newX);
					WorkingNodeGene n2 = def.createGene(this);
					jeat.nodeDefinitions().add(def);
					addNode(n2);
					ConnectionIdentifier id1 = new ConnectionIdentifier(from.id(), n2.id());
					ConnectionGene conGene1 = jeat.connectionDefinitions()
						.createGene(id1, this, () -> new ConnectionGeneDefinition(id1))
						.weight(1);
					addConnection(conGene1);
					ConnectionIdentifier id2 = new ConnectionIdentifier(n2.id(), to.id());
					ConnectionGene conGene2 = jeat.connectionDefinitions()
						.createGene(id2, this, () -> new ConnectionGeneDefinition(id2))
						.weight(con.weight());
					addConnection(conGene2);
					removeConnection(con.id());
				}
			}
		}
	}
	
	@Nullable
	private NodeGene<?, ?> getRandomNode(boolean includeInput, boolean includePermanent) {
		List<NodeGene<?, ?>> nodes = new ArrayList<>(this.nodes.values());
		if (!includeInput) {
			nodes.removeIf(NodeGene::input);
		}
		if (!includePermanent) {
			nodes.removeIf(n -> !n.definition().canRemove());
		}
		if (nodes.isEmpty()) {
			return null;
		}
		return nodes.get(jeat.random().nextInt(nodes.size()));
	}
	
	@Nullable
	private ConnectionGene getRandomConnection() {
		if (connections.isEmpty()) {
			return null;
		}
		return new ArrayList<>(connections.values()).get(jeat.random().nextInt(connections.size()));
	}
	
	@NotNull
	public Genome copy(boolean copyId) {
		Genome genome = copyId ? new Genome(jeat, id) : new Genome(jeat);
		nodes.values().forEach(node -> genome.addNode(node.copy(genome)));
		connections.values().forEach(con -> genome.addConnection(con.copy(genome)));
		return genome;
	}
	
	@NotNull
	public Genome crossover(@NotNull Genome other) {
		Random rnd = jeat.random();
		CrossoverConfig cfg = jeat.config().crossover;
		Genome genome = new Genome(jeat);
		List<ConnectionIdentifier> createdConnections = new ArrayList<>();
		{
			Set<UUID> overlappingNodes = new HashSet<>();
			for (NodeGene<?, ?> node : nodes.values()) {
				if (other.nodes.containsKey(node.id())) {
					overlappingNodes.add(node.id());
				}
				else {
					genome.addNode(node.copy(genome));
				}
			}
			for (NodeGene<?, ?> node : other.nodes.values()) {
				if (!overlappingNodes.contains(node.id())) {
					genome.addNode(node.copy(genome));
				}
			}
			for (UUID id : overlappingNodes) {
				boolean fromThis = rnd.nextDouble() < cfg.geneInheritanceProbability;
				NodeGene<?, ?> node = fromThis ? nodes.get(id) : other.nodes.get(id);
				genome.addNode(node.copy(genome));
			}
		}
		{
			Set<ConnectionIdentifier> overlappingConnections = new HashSet<>();
			for (ConnectionGene con : connections.values()) {
				if (other.connections.containsKey(con.id())) {
					overlappingConnections.add(con.id());
				}
				else {
					genome.addConnection(con.copy(genome));
					createdConnections.add(con.id());
				}
			}
			for (ConnectionGene con : other.connections.values()) {
				if (!overlappingConnections.contains(con.id())) {
					genome.addConnection(con.copy(genome));
					createdConnections.add(con.id());
				}
			}
			for (ConnectionIdentifier id : overlappingConnections) {
				boolean fromThis = rnd.nextDouble() < cfg.geneInheritanceProbability;
				ConnectionGene con = fromThis ? connections.get(id) : other.connections.get(id);
				genome.addConnection(con.copy(genome));
				createdConnections.add(con.id());
			}
		}
		
		if (cfg.thinOutConnections) {
			int cnt =
				(int) (Math.min(cfg.thinOutPercentage, 1) * createdConnections.size() * rnd.nextDouble());
			for (int i = 0; i < cnt; i++) {
				ConnectionIdentifier id = createdConnections.get(rnd.nextInt(createdConnections.size()));
				genome.removeConnection(id);
			}
		}
		
		return genome;
	}
	
}
