package com.unleqitq.jeat.genetics.gene.node.bias;

import com.unleqitq.jeat.genetics.gene.node.AbstractNodeGene;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
@Getter
@Setter
public class BiasNodeGene extends AbstractNodeGene<BiasNodeGene, BiasNodeGeneDefinition> {
	
	public BiasNodeGene(@NotNull Genome genome, @NotNull BiasNodeGeneDefinition definition) {
		super(genome, definition);
	}
	
	@Override
	public void mutate() {
		// Bias nodes are not mutable
	}
	
	@Override
	public @NotNull BiasNodeGene copy(@NotNull Genome genome) {
		return new BiasNodeGene(genome, definition());
	}
	
}
