package com.unleqitq.jeat.utils.tuple;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;

@Accessors (fluent = true)
@Getter
public class Pair<T1, T2> extends AbstractTuple {
	
	private final T1 first;
	private final T2 second;
	
	public Pair(T1 first, T2 second) {
		super(2);
		this.first = first;
		this.second = second;
	}
	
	@Override
	protected <T> T doGet(int index) {
		return switch (index) {
			case 0 -> (T) first;
			case 1 -> (T) second;
			default -> throw new IndexOutOfBoundsException();
		};
	}
	
	@Override
	public List<ElementWrapper<?>> asList() {
		return List.of(Tuple.wrap(first), Tuple.wrap(second));
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		
		Pair<?, ?> pair = (Pair<?, ?>) o;
		return Objects.equals(first, pair.first) &&
			Objects.equals(second, pair.second);
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + Objects.hashCode(first);
		result = 31 * result + Objects.hashCode(second);
		return result;
	}
	
}
