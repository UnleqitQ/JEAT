package com.unleqitq.jeat.genetics.gene.node.bias;

import com.unleqitq.jeat.genetics.gene.node.AbstractNodeGeneDefinition;
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
public class BiasNodeGeneDefinition
	extends AbstractNodeGeneDefinition<BiasNodeGeneDefinition, BiasNodeGene> {
	
	protected BiasNodeGeneDefinition(@NotNull UUID id, double x, @Nullable String name) {
		super(id, x, true, name);
	}
	
	protected BiasNodeGeneDefinition(double x, @Nullable String name) {
		super(x, true, name);
	}
	
	@Override
	public @NotNull BiasNodeGene createGene(@NotNull Genome genome) {
		return new BiasNodeGene(genome, this);
	}
	
}
