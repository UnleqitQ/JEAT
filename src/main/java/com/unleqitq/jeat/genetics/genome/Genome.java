package com.unleqitq.jeat.genetics.genome;

import com.unleqitq.jeat.Jeat;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Accessors (fluent = true)
@Getter
public class Genome implements Comparable<Genome> {
	
	@NotNull
	private final Jeat jeat;
	@NotNull
	private final UUID id;
	
	public Genome(@NotNull Jeat jeat) {
		this.jeat = jeat;
		this.id = UUID.randomUUID();
	}
	
	public Genome(@NotNull Jeat jeat, @NotNull UUID id) {
		this.jeat = jeat;
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		Genome genome = (Genome) o;
		return id.equals(genome.id);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public int compareTo(@NotNull Genome o) {
		return id.compareTo(o.id);
	}
	
}
