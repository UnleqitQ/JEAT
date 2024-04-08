package com.unleqitq.jeat.genetics.gene;

import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a gene definition.
 * @param <I> The type of the gene identifier.
 * @param <S> The type of the gene definition (self reference).
 * @param <G> The type of the gene.
 */
@Accessors (fluent = true)
@Getter
public abstract class GeneDefinition<I extends Comparable<I>, S extends GeneDefinition<I, S, G>, G extends Gene<I, G, S>>
	implements Comparable<S> {
	
	@NotNull
	private final I id;
	
	protected GeneDefinition(@NotNull I id) {
		this.id = id;
	}
	
	@NotNull
	public abstract G createGene(@NotNull Genome genome);
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		GeneDefinition<?, ?, ?> that = (GeneDefinition<?, ?, ?>) o;
		return id.equals(that.id);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public int compareTo(@NotNull S o) {
		return id.compareTo(o.id());
	}
	
}
