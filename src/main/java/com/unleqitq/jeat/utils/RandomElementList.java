package com.unleqitq.jeat.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomElementList<T> {
	
	private final List<Element<T>> elements = new ArrayList<>();
	
	public RandomElementList<T> add(T element, double probability) {
		elements.add(new Element<>(element, probability));
		return this;
	}
	
	public RandomElementList<T> add(T element) {
		return add(element, 1);
	}
	
	public void clear() {
		elements.clear();
	}
	
	public void remove(T element) {
		elements.removeIf(e -> e.element().equals(element));
	}
	
	public List<Element<T>> getElements() {
		return Collections.unmodifiableList(elements);
	}
	
	public T getRandomElement(Random random) {
		return getRandomElement(random, null);
	}
	
	public T getRandomElement(Random random, T defaultValue) {
		double randomValue = random.nextDouble() * elements.stream().mapToDouble(Element::probability).sum();
		double sum = 0;
		for (Element<T> element : elements) {
			sum += element.probability();
			if (randomValue <= sum) {
				return element.element();
			}
		}
		return defaultValue;
	}
	
	public record Element<T>(T element, double probability) {}
	
}
