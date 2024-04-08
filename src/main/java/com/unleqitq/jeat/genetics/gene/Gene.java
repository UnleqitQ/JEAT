package com.unleqitq.jeat.genetics.gene;

import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a gene.
 * @param <I> The type of the gene identifier.
 * @param <S> The type of the gene (self reference).
 * @param <D> The type of the gene definition.
 */
@Accessors (fluent = true)
@Getter
public abstract class Gene<I extends Comparable<I>, S extends Gene<I, S, D>, D extends GeneDefinition<I, D, S>>
	implements Comparable<S> {
	
	@NotNull
	private final Genome genome;
	
	@NotNull
	private final D definition;
	
	protected Gene(@NotNull Genome genome, @NotNull D definition) {
		this.genome = genome;
		this.definition = definition;
	}
	
	public I id() {
		return definition.id();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		Gene<?, ?, ?> that = (Gene<?, ?, ?>) o;
		return definition.equals(that.definition);
	}
	
	@Override
	public int hashCode() {
		return definition.hashCode();
	}
	
	@Override
	public int compareTo(@NotNull S o) {
		return definition.compareTo(o.definition());
	}
	
	@NotNull
	public abstract S copy(@NotNull Genome genome);
	
}
