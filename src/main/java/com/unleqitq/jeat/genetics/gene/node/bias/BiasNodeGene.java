package com.unleqitq.jeat.genetics.gene.node.bias;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.genetics.gene.node.NodeGene;
import com.unleqitq.jeat.genetics.genome.Genome;
import com.unleqitq.jeat.internal.GlobalSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors (fluent = true)
@Getter
@Setter
public class BiasNodeGene extends NodeGene<BiasNodeGene, BiasNodeGeneDefinition> {
	
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
	
	@Override
	public double distance(@NotNull NodeGene<?, ?> other) {
		if (!(other instanceof BiasNodeGene)) {
			switch (GlobalSettings.COMPARING_DIFFERENT_NODE_TYPES) {
				case THROW:
					throw new IllegalArgumentException(
						"Cannot compare different node types. (%s, %s)".formatted(getClass().getName(),
							other.getClass().getName()));
				case WARN:
					Jeat.LOGGER.warn("Cannot compare different node types. ({}, {})", getClass().getName(),
						other.getClass().getName());
				case IGNORE:
					return 0.0;
			}
		}
		return 0.0;
	}
	
}
