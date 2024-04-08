package com.unleqitq.jeat.genetics.gene.node;

import com.unleqitq.jeat.genetics.gene.Gene;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Accessors (fluent = true)
@Getter
@Setter
public abstract class AbstractNodeGene<S extends AbstractNodeGene<S, D>, D extends AbstractNodeGeneDefinition<D, S>>
	extends Gene<UUID, S, D> {
	
	public AbstractNodeGene(@NotNull Genome genome, @NotNull D definition) {
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
	
}
