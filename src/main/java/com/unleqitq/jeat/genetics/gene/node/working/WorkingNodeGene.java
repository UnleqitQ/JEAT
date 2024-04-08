package com.unleqitq.jeat.genetics.gene.node.working;

import com.unleqitq.jeat.genetics.gene.node.AbstractNodeGene;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors (fluent = true)
@Getter
@Setter
public class WorkingNodeGene extends AbstractNodeGene<WorkingNodeGene, WorkingNodeGeneDefinition> {
	
	private boolean enabled = true;
	
	public WorkingNodeGene(@NotNull Genome genome, @NotNull WorkingNodeGeneDefinition definition) {
		super(genome, definition);
	}
	
	@Override
	@NotNull
	public WorkingNodeGene copy(@NotNull Genome genome) {
		WorkingNodeGene copy = new WorkingNodeGene(genome, definition());
		copy.enabled = enabled;
		return copy;
	}
	
}
