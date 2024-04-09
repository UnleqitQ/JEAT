package com.unleqitq.jeat.genetics.gene.node.input;

import com.unleqitq.jeat.genetics.gene.node.NodeGene;
import com.unleqitq.jeat.genetics.genome.Genome;
import com.unleqitq.jeat.internal.GlobalSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
@Getter
@Setter
public class InputNodeGene extends NodeGene<InputNodeGene, InputNodeGeneDefinition> {
	
	public InputNodeGene(@NotNull Genome genome, @NotNull InputNodeGeneDefinition definition) {
		super(genome, definition);
	}
	
	@Override
	public void mutate() {
		// Input nodes are not mutable
	}
	
	@Override
	public @NotNull InputNodeGene copy(@NotNull Genome genome) {
		return new InputNodeGene(genome, definition());
	}
	
	@Override
	public double distance(@NotNull NodeGene<?, ?> other) {
		if (!(other instanceof InputNodeGene)) {
			switch (GlobalSettings.COMPARING_DIFFERENT_NODE_TYPES) {
				case THROW:
					throw new IllegalArgumentException(
						"Cannot compare different node types. (%s, %s)".formatted(getClass().getName(),
							other.getClass().getName()));
				case WARN:
					System.err.printf("(JEAT) [WARN] Cannot compare different node types. (%s, %s)%n",
						getClass().getName(), other.getClass().getName());
				case IGNORE:
					return 0.0;
			}
		}
		return 0.0;
	}
	
}
