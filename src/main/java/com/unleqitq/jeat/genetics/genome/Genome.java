package com.unleqitq.jeat.genetics.genome;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.config.MutationConfig;
import com.unleqitq.jeat.genetics.gene.connection.ConnectionGene;
import com.unleqitq.jeat.genetics.gene.connection.ConnectionGeneDefinition;
import com.unleqitq.jeat.genetics.gene.connection.ConnectionIdentifier;
import com.unleqitq.jeat.genetics.gene.node.NodeGene;
import com.unleqitq.jeat.genetics.gene.node.working.WorkingNodeGene;
import com.unleqitq.jeat.genetics.gene.node.working.WorkingNodeGeneDefinition;
import lombok.Getter;
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
	@NotNull
	private final Map<UUID, NodeGene<?, ?>> nodes = new TreeMap<>();
	@NotNull
	private final Map<ConnectionIdentifier, ConnectionGene> connections = new TreeMap<>();
	
	public Genome(@NotNull Jeat jeat) {
		this.jeat = jeat;
		this.id = UUID.randomUUID();
	}
	
	public Genome(@NotNull Jeat jeat, @NotNull UUID id) {
		this.jeat = jeat;
		this.id = id;
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
					ConnectionGene conGene = (ConnectionGene) jeat.connectionDefinitions()
						.createGene(id, this, () -> new ConnectionGeneDefinition(id));
					connections.put(id, conGene);
					break;
				}
			}
			if (rnd.nextDouble() < con.removeConnectionChance) {
				if (!connections.isEmpty()) {
					ConnectionIdentifier id = new ArrayList<>(connections.keySet())
						.get(rnd.nextInt(connections.size()));
					connections.remove(id);
				}
			}
		}
		{
			MutationConfig.NodeMutationConfig.NodeStructureMutationConfig nd = cfg.node.structure;
			if (rnd.nextDouble() < nd.removeNodeChance) {
				NodeGene<?, ?> node = getRandomNode(true, false);
				if (node != null) {
					node.connections().forEach(c -> connections.remove(c.id()));
					nodes.remove(node.id());
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
						nodes.put(n2.id(), n2);
						cons.forEach(c -> {
							ConnectionIdentifier id = new ConnectionIdentifier(c.from().id(), n2.id());
							ConnectionGene conGene = ((ConnectionGene) jeat.connectionDefinitions()
								.createGene(id, this, () -> new ConnectionGeneDefinition(id))).weight(c.weight());
							connections.put(id, conGene);
							connections.remove(c.id());
						});
						ConnectionGene conGene = ((ConnectionGene) jeat.connectionDefinitions()
							.createGene(new ConnectionIdentifier(n2.id(), n1.id()), this,
								() -> new ConnectionGeneDefinition(new ConnectionIdentifier(n2.id(), n1.id())))).weight(1);
						connections.put(conGene.id(), conGene);
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
						nodes.put(n2.id(), n2);
						cons.forEach(c -> {
							ConnectionIdentifier id = new ConnectionIdentifier(n2.id(), c.to().id());
							ConnectionGene conGene = ((ConnectionGene) jeat.connectionDefinitions()
								.createGene(id, this, () -> new ConnectionGeneDefinition(id))).weight(c.weight());
							connections.put(id, conGene);
							connections.remove(c.id());
						});
						ConnectionGene conGene = ((ConnectionGene) jeat.connectionDefinitions()
							.createGene(new ConnectionIdentifier(n1.id(), n2.id()), this,
								() -> new ConnectionGeneDefinition(new ConnectionIdentifier(n1.id(), n2.id())))).weight(1);
						connections.put(conGene.id(), conGene);
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
					nodes.put(n2.id(), n2);
					ConnectionIdentifier id1 = new ConnectionIdentifier(from.id(), n2.id());
					ConnectionGene conGene1 = ((ConnectionGene) jeat.connectionDefinitions()
						.createGene(id1, this, () -> new ConnectionGeneDefinition(id1))).weight(1);
					connections.put(id1, conGene1);
					ConnectionIdentifier id2 = new ConnectionIdentifier(n2.id(), to.id());
					ConnectionGene conGene2 = ((ConnectionGene) jeat.connectionDefinitions()
						.createGene(id2, this, () -> new ConnectionGeneDefinition(id2))).weight(con.weight());
					connections.put(id2, conGene2);
					connections.remove(con.id());
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
	
}
