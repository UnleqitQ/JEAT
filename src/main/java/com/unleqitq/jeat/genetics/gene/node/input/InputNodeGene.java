package com.unleqitq.jeat.genetics.gene.node.input;

import com.unleqitq.jeat.genetics.gene.node.AbstractNodeGene;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
@Getter
@Setter
public class InputNodeGene extends AbstractNodeGene<InputNodeGene, InputNodeGeneDefinition> {
	
	public InputNodeGene(@NotNull Genome genome, @NotNull InputNodeGeneDefinition definition) {
		super(genome, definition);
	}
	
	@Override
	public @NotNull InputNodeGene copy(@NotNull Genome genome) {
		return new InputNodeGene(genome, definition());
	}
	
}
