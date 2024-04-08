package com.unleqitq.jeat.utils.tuple;

import java.util.List;

public interface Tuple {
	
	int size();
	<T> T get(int index);
	List<ElementWrapper<?>> asList();
	
	record ElementWrapper<T>(T value) {}
	
	static <T> ElementWrapper<T> wrap(T value) {
		return new ElementWrapper<>(value);
	}
	
	static <T> T unwrap(ElementWrapper<T> wrapper) {
		return wrapper.value;
	}
	
	static <T1, T2> Pair<T1, T2> of(T1 first, T2 second) {
		return new Pair<>(first, second);
	}
	
	static <T1, T2, T3> Triple<T1, T2, T3> of(T1 first, T2 second, T3 third) {
		return new Triple<>(first, second, third);
	}
	
	static <T1, T2, T3, T4> Quadruple<T1, T2, T3, T4> of(T1 first, T2 second, T3 third, T4 fourth) {
		return new Quadruple<>(first, second, third, fourth);
	}
	
	static <T1, T2, T3, T4, T5> Quintuple<T1, T2, T3, T4, T5> of(T1 first, T2 second, T3 third, T4 fourth, T5 fifth) {
		return new Quintuple<>(first, second, third, fourth, fifth);
	}
	
}
