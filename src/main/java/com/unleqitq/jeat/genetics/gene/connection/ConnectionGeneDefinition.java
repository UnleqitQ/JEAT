package com.unleqitq.jeat.genetics.gene.connection;

import com.unleqitq.jeat.genetics.gene.GeneDefinition;
import com.unleqitq.jeat.genetics.genome.Genome;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ConnectionGeneDefinition
	extends GeneDefinition<ConnectionIdentifier, ConnectionGeneDefinition, ConnectionGene> {
	
	public ConnectionGeneDefinition(ConnectionIdentifier id) {
		super(id);
	}
	
	public ConnectionGeneDefinition(UUID from, UUID to) {
		this(new ConnectionIdentifier(from, to));
	}
	
	@Override
	@NotNull
	public ConnectionGene createGene(@NotNull Genome genome) {
		return new ConnectionGene(genome, this);
	}
	
	public UUID from() {
		return this.id().from();
	}
	
	public UUID to() {
		return this.id().to();
	}
	
}
