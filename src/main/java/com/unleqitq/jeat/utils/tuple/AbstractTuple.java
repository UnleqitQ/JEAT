package com.unleqitq.jeat.utils.tuple;

public abstract class AbstractTuple implements Tuple {
	
	private final int size;
	
	protected AbstractTuple(int size) {
		this.size = size;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public <T> T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		return doGet(index);
	}
	
	protected abstract <T> T doGet(int index);
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			asList().stream().map(ElementWrapper::toString).reduce((a, b) -> a + ", " + b).orElse("") +
			")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		AbstractTuple that = (AbstractTuple) o;
		return size == that.size && asList().equals(that.asList());
	}
	
	@Override
	public int hashCode() {
		return asList().hashCode();
	}
	
}
