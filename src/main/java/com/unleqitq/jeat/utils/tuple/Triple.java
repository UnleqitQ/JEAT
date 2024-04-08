package com.unleqitq.jeat.utils.tuple;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;

@Accessors (fluent = true)
@Getter
public class Triple<T1, T2, T3> extends AbstractTuple {
	
	private final T1 first;
	private final T2 second;
	private final T3 third;
	
	public Triple(T1 first, T2 second, T3 third) {
		super(3);
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	@Override
	protected <T> T doGet(int index) {
		return switch (index) {
			case 0 -> (T) first;
			case 1 -> (T) second;
			case 2 -> (T) third;
			default -> throw new IndexOutOfBoundsException();
		};
	}
	
	@Override
	public List<ElementWrapper<?>> asList() {
		return List.of(Tuple.wrap(first), Tuple.wrap(second), Tuple.wrap(third));
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		
		Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
		return Objects.equals(first, triple.first) &&
			Objects.equals(second, triple.second) &&
			Objects.equals(third, triple.third);
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + Objects.hashCode(first);
		result = 31 * result + Objects.hashCode(second);
		result = 31 * result + Objects.hashCode(third);
		return result;
	}
	
}
