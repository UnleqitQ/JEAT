package com.unleqitq.jeat.genetics.gene.node;

import com.unleqitq.jeat.genetics.gene.Gene;
import com.unleqitq.jeat.genetics.gene.connection.ConnectionGene;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

@Accessors (fluent = true)
@Getter
@Setter
public abstract class NodeGene<S extends NodeGene<S, D>, D extends NodeGeneDefinition<D, S>>
	extends Gene<UUID, S, D> {
	
	public NodeGene(@NotNull Genome genome, @NotNull D definition) {
		super(genome, definition);
	}
	
	public double x() {
		return definition().x();
	}
	
	public boolean input() {
		return definition().input();
	}
	
	@Nullable
	public String name() {
		return definition().name();
	}
	
	@NotNull
	public Collection<ConnectionGene> inputs() {
		return genome().connections().values().stream()
			.filter(connection -> connection.toId().equals(id()))
			.toList();
	}
	
	@NotNull
	public Collection<ConnectionGene> outputs() {
		return genome().connections().values().stream()
			.filter(connection -> connection.fromId().equals(id()))
			.toList();
	}
	
	@NotNull
	public Collection<ConnectionGene> connections() {
		return genome().connections().values().stream()
			.filter(connection -> connection.fromId().equals(id()) || connection.toId().equals(id()))
			.toList();
	}
	
	public abstract double distance(@NotNull NodeGene<?, ?> other);
	
}
