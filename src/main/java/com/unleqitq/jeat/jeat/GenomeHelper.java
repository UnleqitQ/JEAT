package com.unleqitq.jeat.jeat;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.calculator.Calculator;
import com.unleqitq.jeat.genetics.genome.Genome;
import com.unleqitq.jeat.genetics.genome.GenomeStore;
import com.unleqitq.jeat.utils.ListDirection;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Accessors (fluent = true)
@Getter
public class GenomeHelper {
	
	@NotNull
	private final Jeat jeat;
	@NotNull
	private final GenomeStore store;
	
	public GenomeHelper(@NotNull Jeat jeat) {
		this.jeat = jeat;
		this.store = jeat.genomeStore();
	}
	
	/**
	 * Creates a new genome and optionally adds it to the genome store.
	 *
	 * @param addToStore Whether to add the genome to the genome store.
	 * @return The created genome.
	 */
	@NotNull
	public Genome createGenome(boolean addToStore) {
		Genome genome = new Genome(jeat).init();
		if (addToStore) {
			store.add(genome);
		}
		return genome;
	}
	
	/**
	 * Creates a new genome and adds it to the genome store.
	 *
	 * @return The created genome.
	 */
	@NotNull
	public Genome createGenome() {
		return createGenome(true);
	}
	
	/**
	 * Creates a list of new genomes and optionally adds them to the genome store.
	 *
	 * @param count The number of genomes to create.
	 * @param addToStore Whether to add the genome to the genome store.
	 * @return The created genomes.
	 */
	@NotNull
	public List<Genome> createGenomes(int count, boolean addToStore) {
		List<Genome> genomes = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			genomes.add(createGenome(addToStore));
		}
		return genomes;
	}
	
	/**
	 * Creates a list of new genomes and adds them to the genome store.
	 *
	 * @param count The number of genomes to create.
	 * @return The created genomes.
	 */
	@NotNull
	public List<Genome> createGenomes(int count) {
		return createGenomes(count, true);
	}
	
	/**
	 * Removes the worst genomes from the genome store.
	 * @param count The number of genomes to remove.
	 */
	public void removeWorstCount(int count) {
		store.sort(ListDirection.DESCENDING);
		for (int i = 0; i < count; i++) {
			store.remove(store.genomes().getLast());
		}
	}
	
	/**
	 * Removes the worst genomes from the genome store.
	 * @param ratio The ratio of genomes to remove.
	 */
	public void removeWorstRatio(double ratio) {
		removeWorstCount((int) (store.size() * ratio));
	}
	
	/**
	 * Creates a list of copies of random genomes from the genome store.
	 *
	 * @param count The number of genomes to copy.
	 * @param addToStore Whether to add the copied genomes to the genome store.
	 * @param probabilityFunction The function that determines the probability of a genome being selected.
	 * @return The created genomes.
	 */
	@NotNull
	public List<Genome> copyGenomes(int count, boolean addToStore,
		@NotNull Function<Genome, Double> probabilityFunction) {
		if (store.size() == 0) throw new IllegalStateException("There are no genomes in the store");
		List<Genome> genomes = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			Genome genome = store.random(jeat.random(), probabilityFunction);
			if (genome == null) throw new IllegalStateException("The random genome is null");
			Genome copy = genome.copy(false);
			genomes.add(copy);
		}
		if (addToStore) store.addAll(genomes);
		return genomes;
	}
	
	/**
	 * Creates a list of copies of random genomes from the genome store.
	 *
	 * @param count The number of genomes to copy.
	 * @param probabilityFunction The function that determines the probability of a genome being selected.
	 * @return The created genomes.
	 */
	@NotNull
	public List<Genome> copyGenomes(int count,
		@NotNull Function<Genome, Double> probabilityFunction) {
		return copyGenomes(count, true, probabilityFunction);
	}
	
	/**
	 * Creates a list of copies of random genomes from the genome store.
	 *
	 * @param count The number of genomes to copy.
	 * @param addToStore Whether to add the copied genomes to the genome store.
	 */
	@NotNull
	public List<Genome> copyGenomes(int count, boolean addToStore) {
		if (store.size() == 0) throw new IllegalStateException("There are no genomes in the store");
		List<Genome> genomes = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			Genome genome = store.random(jeat.random());
			if (genome == null) throw new IllegalStateException("The random genome is null");
			Genome copy = genome.copy(false);
			genomes.add(copy);
		}
		if (addToStore) {
			store.addAll(genomes);
		}
		return genomes;
	}
	
	/**
	 * Creates a list of copies of random genomes from the genome store.
	 *
	 * @param count The number of genomes to copy.
	 */
	@NotNull
	public List<Genome> copyGenomes(int count) {
		return copyGenomes(count, true);
	}
	
	/**
	 * Mutates the genomes in the genome store.
	 */
	public void mutate() {
		store.genomes().forEach(Genome::mutate);
	}
	
	/**
	 * Crosses the genomes in the genome store.
	 *
	 * @param count The number of genomes to create.
	 * @param addToStore Whether to add the crossed genomes to the genome store.
	 * @param swapParents Whether to swap the parents if the fitness of the second parent is higher.
	 * @param probabilityFunction The function that determines the probability of a genome being selected.
	 * @return The created genomes.
	 */
	@NotNull
	public List<Genome> cross(int count, boolean addToStore, boolean swapParents,
		@NotNull Function<Genome, Double> probabilityFunction) {
		if (store.size() < 2)
			throw new IllegalStateException("There are not enough genomes in the store");
		List<Genome> genomes = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			Genome parent1 = store.random(jeat.random(), probabilityFunction);
			Genome parent2 =
				store.random(jeat.random(), (g) -> g.equals(parent1) ? 0 : probabilityFunction.apply(g));
			if (parent1 == null || parent2 == null)
				throw new IllegalStateException("The random genomes are null");
			Genome p1, p2;
			if (swapParents && parent2.fitness() > parent1.fitness()) {
				p1 = parent2;
				p2 = parent1;
			}
			else {
				p1 = parent1;
				p2 = parent2;
			}
			Genome child = p1.crossover(p2);
			genomes.add(child);
		}
		if (addToStore) store.addAll(genomes);
		return genomes;
	}
	
	/**
	 * Crosses the genomes in the genome store.
	 *
	 * @param count The number of genomes to create.
	 * @param addToStore Whether to add the crossed genomes to the genome store.
	 * @param swapParents Whether to swap the parents if the fitness of the second parent is higher.
	 * @return The created genomes.
	 */
	@NotNull
	public List<Genome> cross(int count, boolean addToStore, boolean swapParents) {
		return cross(count, addToStore, swapParents, (g) -> 1.0);
	}
	
	/**
	 * Creates a Map of calculators for the genomes in the genome store.
	 * @return The created calculators.
	 */
	@NotNull
	public Map<Genome, Calculator> createCalculators() {
		Map<Genome, Calculator> calculators = new HashMap<>();
		store.genomes().forEach(genome -> calculators.put(genome, Calculator.create(genome)));
		return calculators;
	}
	
	public void clearFitness() {
		store.genomes().forEach(genome -> genome.fitness(0));
	}
	
}
