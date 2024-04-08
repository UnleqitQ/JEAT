package com.unleqitq.jeat.utils.tuple;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors (fluent = true)
@Getter
public class Quadruple<T1, T2, T3, T4> extends AbstractTuple {
	
	private final T1 first;
	private final T2 second;
	private final T3 third;
	private final T4 fourth;
	
	public Quadruple(T1 first, T2 second, T3 third, T4 fourth) {
		super(4);
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}
	
	@Override
	protected <T> T doGet(int index) {
		return switch (index) {
			case 0 -> (T) first;
			case 1 -> (T) second;
			case 2 -> (T) third;
			case 3 -> (T) fourth;
			default -> throw new IndexOutOfBoundsException();
		};
	}
	
	@Override
	public List<ElementWrapper<?>> asList() {
		return List.of(Tuple.wrap(first), Tuple.wrap(second), Tuple.wrap(third),
			Tuple.wrap(fourth));
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		
		Quadruple<?, ?, ?, ?> that = (Quadruple<?, ?, ?, ?>) o;
		
		return first.equals(that.first) && second.equals(that.second) && third.equals(that.third) &&
			fourth.equals(that.fourth);
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + first.hashCode();
		result = 31 * result + second.hashCode();
		result = 31 * result + third.hashCode();
		result = 31 * result + fourth.hashCode();
		return result;
	}
	
}
