package com.unleqitq.jeat.genetics.genome;

import com.unleqitq.jeat.internal.InternalUse;
import com.unleqitq.jeat.utils.ListDirection;
import com.unleqitq.jeat.utils.tuple.Pair;
import com.unleqitq.jeat.utils.tuple.Tuple;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class GenomeStore {
	
	@Getter (onMethod_ = {@InternalUse})
	@NotNull
	private final Map<UUID, Genome> genomes = new HashMap<>();
	/**
	 * Used for sorting genomes by fitness, by using a list it doesn't need to be sorted every time
	 */
	@Getter (onMethod_ = {@InternalUse})
	@NotNull
	private final List<Genome> genomesList = new ArrayList<>();
	private boolean sorted = false;
	@NotNull
	private ListDirection direction = ListDirection.DESCENDING;
	
	public void add(@NotNull Genome genome) {
		genomes.put(genome.id(), genome);
		genomesList.add(genome);
		sorted = false;
	}
	
	public void remove(@NotNull Genome genome) {
		genomes.remove(genome.id());
		genomesList.remove(genome);
	}
	
	public void remove(@NotNull UUID id) {
		Genome genome = genomes.remove(id);
		if (genome != null) {
			genomesList.remove(genome);
		}
	}
	
	@Nullable
	public Genome get(@NotNull UUID id) {
		return genomes.get(id);
	}
	
	@NotNull
	public List<Genome> genomes() {
		return Collections.unmodifiableList(genomesList);
	}
	
	public int size() {
		return genomes.size();
	}
	
	@Nullable
	public Genome random(@NotNull Random random) {
		if (genomesList.isEmpty()) return null;
		return genomesList.get(random.nextInt(genomesList.size()));
	}
	
	@Nullable
	public Genome random(@NotNull Random random,
		@NotNull Function<Genome, Double> probabilityFunction) {
		if (genomesList.isEmpty()) return null;
		List<Pair<Genome, Double>> probabilities = new ArrayList<>();
		double sum = 0;
		for (Genome genome : genomesList) {
			double probability = probabilityFunction.apply(genome);
			probabilities.add(Tuple.of(genome, probability));
			sum += probability;
		}
		double randomValue = random.nextDouble() * sum;
		double current = 0;
		for (Pair<Genome, Double> pair : probabilities) {
			current += pair.second();
			if (current >= randomValue) {
				return pair.first();
			}
		}
		return null;
	}
	
	public void clear() {
		genomes.clear();
		genomesList.clear();
		sorted = false;
	}
	
	public void invalidateOrder() {
		sorted = false;
	}
	
	public void sort(@NotNull ListDirection direction, @NotNull Comparator<Genome> comparator) {
		genomesList.sort((g1, g2) -> {
			if (direction == ListDirection.ASCENDING) {
				return comparator.compare(g1, g2);
			}
			else {
				return comparator.compare(g2, g1);
			}
		});
		sorted = true;
		this.direction = direction;
	}
	
	public void sort(@NotNull Comparator<Genome> comparator) {
		sort(ListDirection.DESCENDING, comparator);
	}
	
	public void sort(@NotNull ListDirection direction) {
		sort(direction, Genome::compareByFitness);
	}
	
	public void sort() {
		sort(ListDirection.DESCENDING);
	}
	
	@Nullable
	public Genome best(int offset) {
		if (sorted) return direction == ListDirection.DESCENDING ? genomesList.get(offset) :
			genomesList.get(genomesList.size() - offset - 1);
		return genomesList.stream()
			.sorted(Comparator.comparingDouble(Genome::fitness))
			.skip(offset)
			.findFirst()
			.orElse(null);
	}
	
	@Nullable
	public Genome worst(int offset) {
		if (sorted) return direction == ListDirection.DESCENDING ?
			genomesList.get(genomesList.size() - offset - 1) : genomesList.get(offset);
		return genomesList.stream()
			.sorted(Comparator.comparingDouble(g -> -g.fitness()))
			.skip(offset)
			.findFirst()
			.orElse(null);
	}
	
	public void addAll(@NotNull Collection<Genome> genomes) {
		genomes.forEach(this::add);
	}
	
}
