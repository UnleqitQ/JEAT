package com.unleqitq.jeat.genetics.gene.node.input;

import com.unleqitq.jeat.genetics.gene.node.NodeGeneDefinition;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
public class InputNodeGeneDefinition
	extends NodeGeneDefinition<InputNodeGeneDefinition, InputNodeGene> {
	
	protected InputNodeGeneDefinition(@NotNull UUID id, double x,
		@Nullable String name) {
		super(id, x, true, name);
	}
	
	public InputNodeGeneDefinition(double x, @Nullable String name) {
		super(x, true, name);
	}
	
	@Override
	public @NotNull InputNodeGene createGene(@NotNull Genome genome) {
		return new InputNodeGene(genome, this);
	}
	
}
