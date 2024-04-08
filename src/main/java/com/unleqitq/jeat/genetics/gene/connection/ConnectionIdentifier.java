package com.unleqitq.jeat.genetics.gene.connection;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ConnectionIdentifier(
	@NotNull UUID from, @NotNull UUID to
) implements Comparable<ConnectionIdentifier> {
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		ConnectionIdentifier that = (ConnectionIdentifier) o;
		return to.equals(that.to) && from.equals(that.from);
	}
	
	@Override
	public int hashCode() {
		int result = from.hashCode();
		result = 31 * result + to.hashCode();
		return result;
	}
	
	@Override
	public int compareTo(ConnectionIdentifier o) {
		int fromComparison = from.compareTo(o.from);
		if (fromComparison != 0) {
			return fromComparison;
		}
		return to.compareTo(o.to);
	}
	
}
