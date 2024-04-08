package com.unleqitq.jeat.genetics.gene.connection;

import com.unleqitq.jeat.genetics.gene.Gene;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Accessors (fluent = true)
@Getter
@Setter
public class ConnectionGene
	extends Gene<ConnectionIdentifier, ConnectionGene, ConnectionGeneDefinition> {
	
	private boolean enabled = true;
	private double weight;
	
	public ConnectionGene(@NotNull Genome genome, @NotNull ConnectionGeneDefinition definition) {
		super(genome, definition);
	}
	
	@NotNull
	public UUID fromId() {
		return definition().from();
	}
	
	@NotNull
	public UUID toId() {
		return definition().to();
	}
	
	@Override
	@NotNull
	public ConnectionGene copy(@NotNull Genome genome) {
		ConnectionGene copy = new ConnectionGene(genome, definition());
		copy.enabled = enabled;
		copy.weight = weight;
		return copy;
	}
	
}
