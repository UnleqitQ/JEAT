package com.unleqitq.jeat.population;


import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.genetics.genome.Genome;
import com.unleqitq.jeat.genetics.species.Species;
import com.unleqitq.jeat.internal.InternalUse;
import com.unleqitq.jeat.utils.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * This class contains helper methods for reproduction of the population.<br>
 * For the StagnationHelper, see {@link StagnationHelper}.
 * <p>
 * This class is not intended to be used by the user.
 */
@InternalUse
public class ReproductionHelper {
	
	/**
	 * The Jeat instance
	 */
	@NotNull
	private final Jeat jeat;
	
	/**
	 * The population to which this helper belongs.
	 */
	@NotNull
	private final Population population;
	
	/**
	 * Creates a new instance of this class.
	 *
	 * @param population The population to which this helper belongs.
	 */
	public ReproductionHelper(@NotNull Population population) {
		this.population = population;
		this.jeat = population.jeat();
	}
	
	/**
	 * Resets the fitness of all species in the population.
	 */
	public void resetFitness() {
		population.species().values().forEach(Species::resetFitness);
	}
	
	/**
	 * Calculates the fitness of all species in the population.
	 *
	 * @param aggregationFunction The function to aggregate the fitness of all genomes in a species.
	 */
	public void calculateFitness(ToDoubleFunction<Collection<Genome>> aggregationFunction) {
		population.species().values().forEach(species -> species.calculateFitness(aggregationFunction));
	}
	
	/**
	 * Prepares the population for reproduction.
	 *
	 * @param config The configuration for the reproduction.
	 * @param savedGenomes The collection to which the best genomes will be saved.
	 * @return The adjusted minimal species size.
	 */
	public int preReproduction(@NotNull ReproductionConfig config,
		@NotNull Collection<Genome> savedGenomes) {
		// First of all, calculate the fitness of all species
		calculateFitness(config.aggregationFunction);
		
		// Save the best genomes
		{
			int savedGenomeAmount = (int) (population.populationSize() * config.elitismRatio);
			List<Genome> sortedGenomes = population.genomes()
				.values()
				.stream()
				.sorted(Comparator.comparingDouble(g -> -g.fitness()))
				.toList();
			for (int i = 0; i < savedGenomeAmount; i++) {
				Genome genome = sortedGenomes.get(i);
				savedGenomes.add(genome.copy(false));
			}
		}
		
		int minimalSpeciesSize = config.minimalSpeciesSize;
		int minimalSpeciesAmount = config.minimalSpeciesAmount;
		double discardRatio = config.discardRatio;
		
		// Adjust the minimal species size and discard ratio if necessary
		{
			// Adjust the minimal species size
			{
				// First value: size, second value: amount of species with this size
				int finalMinimalSpeciesSize = minimalSpeciesSize;
				Map<Integer, Integer> speciesSizes = population.species()
					.values()
					.stream()
					.map(Species::size)
					.filter(size -> size < finalMinimalSpeciesSize)
					.collect(Collectors.groupingBy(i -> i, Collectors.summingInt(i -> 1)));
				int currentSpeciesAmount = (int) population.species()
					.values()
					.stream()
					.map(Species::size)
					.filter(size -> size >= finalMinimalSpeciesSize)
					.count();
				PriorityQueue<Pair<Integer, Integer>> sortedSpeciesSizes =
					new PriorityQueue<>(Comparator.comparingInt(v -> -v.first()));
				sortedSpeciesSizes.addAll(
					speciesSizes.entrySet().stream().map(e -> new Pair<>(e.getKey(), e.getValue())).toList());
				while (currentSpeciesAmount < minimalSpeciesAmount && !sortedSpeciesSizes.isEmpty()) {
					Pair<Integer, Integer> speciesSize = sortedSpeciesSizes.poll();
					minimalSpeciesSize = speciesSize.first();
					currentSpeciesAmount += speciesSize.second();
				}
			}
			// Adjust the discard ratio
			{
				int finalMinimalSpeciesSize = minimalSpeciesSize;
				int speciesAmount = (int) population.species()
					.values()
					.stream()
					.filter(species -> species.size() >= finalMinimalSpeciesSize)
					.count();
				if (speciesAmount * (1 - discardRatio) < minimalSpeciesAmount) {
					discardRatio = 1 - (double) minimalSpeciesAmount / speciesAmount;
				}
			}
		}
		
		// Discard the worst species
		{
			int discardAmount = (int) (population.species().size() * discardRatio);
			List<Species> sortedSpecies = population.species()
				.values()
				.stream()
				.sorted(Comparator.comparingDouble(Species::fitness))
				.toList();
			for (int i = 0; i < discardAmount; i++) {
				population.decimateSpecies(sortedSpecies.get(i));
			}
		}
		
		return minimalSpeciesSize;
	}
	
	/**
	 * Randomly selects a genome from the given list of pairs of genomes and their probabilities of being selected.
	 * The probabilities are normalized.
	 *
	 * @param genomes The list of pairs of genomes and their probabilities of being selected.
	 * @return The selected genome.
	 */
	@NotNull
	private Genome selectGenome(@NotNull List<Pair<Genome, Double>> genomes) {
		if (genomes.isEmpty()) {
			throw new IllegalArgumentException("The list of genomes must not be empty.");
		}
		double random = jeat.random().nextDouble();
		double current = 0;
		for (Pair<Genome, Double> pair : genomes) {
			current += pair.second();
			if (current >= random) {
				return pair.first();
			}
		}
		return genomes.getLast().first();
	}
	
	/**
	 * Randomly selects a genome from the given list of pairs of genomes and their probabilities of being selected.
	 * While ensuring that the selected genome is not the same as the ignored genome.
	 * The probabilities do not need to be normalized.
	 *
	 * @param genomes The list of pairs of genomes and their probabilities of being selected.
	 * @param ignored The id of the genome that should be ignored.
	 */
	@NotNull
	private Genome selectGenome(@NotNull List<Pair<Genome, Double>> genomes, @NotNull UUID ignored) {
		if (genomes.isEmpty()) {
			throw new IllegalArgumentException("The list of genomes must not be empty.");
		}
		double random = jeat.random().nextDouble() * genomes.stream().mapToDouble(Pair::second).sum();
		double current = 0;
		for (Pair<Genome, Double> pair : genomes) {
			if (pair.first().id().equals(ignored)) {
				continue;
			}
			current += pair.second();
			if (current >= random) {
				return pair.first();
			}
		}
		return genomes.getLast().first();
	}
	
	/**
	 * Randomly selects a species from the given list of pairs of species and their probabilities of being selected.<br>
	 * The probabilities are normalized.<br>
	 *
	 * @param species The list of pairs of species and their probabilities of being selected.
	 * @return The selected species.
	 */
	@NotNull
	private Species selectSpecies(@NotNull List<Pair<Species, Double>> species) {
		if (species.isEmpty()) {
			throw new IllegalArgumentException("The list of species must not be empty.");
		}
		double random = jeat.random().nextDouble();
		double current = 0;
		for (Pair<Species, Double> pair : species) {
			current += pair.second();
			if (current >= random) {
				return pair.first();
			}
		}
		return species.getLast().first();
	}
	
	/**
	 * Randomly selects a species from the given list of pairs of species and their probabilities of being selected.<br>
	 * While ensuring that the selected species is not the same as the ignored species.<br>
	 * The probabilities do not need to be normalized.
	 *
	 * @param species The list of pairs of species and their probabilities of being selected.
	 * @param ignored The id of the species that should be ignored.
	 */
	@NotNull
	private Species selectSpecies(@NotNull List<Pair<Species, Double>> species,
		@NotNull UUID ignored) {
		if (species.isEmpty()) {
			throw new IllegalArgumentException("The list of species must not be empty.");
		}
		double random = jeat.random().nextDouble() * species.stream().mapToDouble(Pair::second).sum();
		double current = 0;
		for (Pair<Species, Double> pair : species) {
			if (pair.first().id().equals(ignored)) {
				continue;
			}
			current += pair.second();
			if (current >= random) {
				return pair.first();
			}
		}
		return species.getLast().first();
	}
	
	
	/**
	 * Generates the normalized probabilities for each species in the given list of species.
	 *
	 * @param parentSpecies The parent species.
	 * @return The list of pairs of species and their probabilities of being selected.
	 */
	@NotNull
	private static List<Pair<Species, Double>> generateSpeciesProbabilities(
		@NotNull List<Species> parentSpecies) {
		// calculate the fitness range
		double minFitness = parentSpecies.stream().mapToDouble(Species::fitness).min().orElse(0);
		double maxFitness = parentSpecies.stream().mapToDouble(Species::fitness).max().orElse(0);
		double fitnessRange = Math.max(maxFitness - minFitness, 1e-6);
		// calculate the probabilities
		List<Pair<Species, Double>> pairs = parentSpecies.stream()
			.map(species -> new Pair<>(species, (species.fitness() - minFitness) / fitnessRange))
			.toList();
		// normalize the probabilities
		double sum = pairs.stream().mapToDouble(Pair::second).sum();
		return pairs.stream().map(pair -> new Pair<>(pair.first(), pair.second() / sum)).toList();
	}
	
	/**
	 * Generates the normalized probabilities for each genome for each species in the given list of species.
	 *
	 * @param config The configuration for the reproduction.
	 * @param parentSpecies The parent species.
	 * @param sortedGenomesBySpecies The map of species and their sorted genomes.
	 * @return The map of species and their list of pairs of genomes and their probabilities of being selected.
	 */
	private static Map<Species, List<Pair<Genome, Double>>> generateGenomeProbabilities(
		@NotNull ReproductionConfig config, @NotNull List<Species> parentSpecies, @NotNull Map<UUID, List<Genome>> sortedGenomesBySpecies) {
		return parentSpecies.stream().collect(Collectors.toMap(s -> s, s -> {
			// Sort the genomes by fitness
			List<Genome> sortedGenomes = sortedGenomesBySpecies.get(s.id());
			// calculate the amount of parents in this species
			int parentAmount = Math.max((int) (Math.ceil(s.size() * config.parentRatio)), 1);
			// get the sublist
			List<Genome> genomes = sortedGenomes.subList(0, parentAmount);
			// calculate the fitness range
			double minFitness = genomes.stream().mapToDouble(Genome::fitness).min().orElse(0);
			double maxFitness = genomes.stream().mapToDouble(Genome::fitness).max().orElse(0);
			double fitnessRange = Math.max(maxFitness - minFitness, 1e-6);
			// calculate the probabilities
			List<Pair<Genome, Double>> pairs = genomes.stream()
				.map(genome -> new Pair<>(genome, (genome.fitness() - minFitness) / fitnessRange))
				.toList();
			// normalize the probabilities
			double sum = pairs.stream().mapToDouble(Pair::second).sum();
			return pairs.stream().map(pair -> new Pair<>(pair.first(), pair.second() / sum)).toList();
		}));
	}
	
	/**
	 * Collects all species that are allowed to reproduce.
	 *
	 * @param minimalSpeciesSize The minimal size of a species to be allowed to reproduce.
	 * @return The list of species that are allowed to reproduce.
	 */
	@NotNull
	private List<Species> collectParentSpecies(int minimalSpeciesSize) {
		return population.species()
			.values()
			.stream()
			.filter(species -> species.size() >= minimalSpeciesSize)
			.toList();
	}
	
	/**
	 * Collects all genomes that should survive to the next generation.
	 *
	 * @param config The configuration for the reproduction.
	 * @param sortedGenomesBySpecies The map of species and their sorted genomes.
	 * @return The collection of genomes that should survive to the next generation.
	 */
	@NotNull
	private Collection<Genome> collectSurvivingGenomes(@NotNull ReproductionConfig config,
		@NotNull Map<UUID, List<Genome>> sortedGenomesBySpecies) {
		// No need to copy the genomes, as they will only be modified after the reproduction
		
		List<Genome> survivingGenomes = new ArrayList<>();
		population.species().values().forEach(species -> {
			int survivalAmount = (int) (Math.ceil(species.size() * config.survivalRate));
			List<Genome> sortedGenomes = sortedGenomesBySpecies.get(species.id());
			survivingGenomes.addAll(sortedGenomes.subList(0, survivalAmount));
		});
		return survivingGenomes;
	}
	
	/**
	 * Reproduces the population using sexual reproduction.<br>
	 * This method only creates the new genomes and does not add them to the population.
	 *
	 * @param amount The amount of genomes to create.
	 * @param config The configuration for the reproduction.
	 * @param minimalSpeciesSize The adjusted minimal species size for reproduction.
	 * @param sortedGenomesBySpecies The map of species and their sorted genomes.
	 * @return The created genomes.
	 */
	@NotNull
	public Collection<Genome> reproduceSexually(int amount, @NotNull ReproductionConfig config,
		int minimalSpeciesSize, @NotNull Map<UUID, List<Genome>> sortedGenomesBySpecies) {
		ReproductionConfig.SexualReproductionConfig sConfig = config.sexualReproductionConfig;
		if (sConfig == null)
			throw new IllegalArgumentException("No sexual reproduction configuration provided.");
		List<Species> parentSpecies = collectParentSpecies(minimalSpeciesSize);
		Map<Species, List<Pair<Genome, Double>>> parentGenomes =
			generateGenomeProbabilities(config, parentSpecies, sortedGenomesBySpecies);
		List<Pair<Species, Double>> normalizedSpeciesProbabilities =
			generateSpeciesProbabilities(parentSpecies);
		
		List<Genome> newGenomes = new ArrayList<>();
		
		// create the new genomes
		while (newGenomes.size() < amount) {
			Species parentSpecies1 = selectSpecies(normalizedSpeciesProbabilities);
			boolean selectDifferentSpecies = parentSpecies1.size() < 2 ||
				(jeat.random().nextDouble() < sConfig.interspeciesMatingProbability &&
					parentSpecies.size() > 1);
			Species parentSpecies2 = selectDifferentSpecies ?
				selectSpecies(normalizedSpeciesProbabilities, parentSpecies1.id()) : parentSpecies1;
			Genome parent1 = selectGenome(parentGenomes.get(parentSpecies1));
			Genome parent2 = selectDifferentSpecies ? selectGenome(parentGenomes.get(parentSpecies2)) :
				selectGenome(parentGenomes.get(parentSpecies1), parent1.id());
			
			if (sConfig.swapParents && parent1.fitness() < parent2.fitness()) {
				Genome temp = parent1;
				parent1 = parent2;
				parent2 = temp;
			}
			
			Genome child = parent1.crossover(parent2);
			newGenomes.add(child);
		}
		
		return newGenomes;
	}
	
	/**
	 * Reproduces the population using asexual reproduction.
	 *
	 * @param amount The amount of genomes to create.
	 * @param config The configuration for the reproduction.
	 * @param minimalSpeciesSize The adjusted minimal species size for reproduction.
	 * @return The created genomes.
	 */
	@NotNull
	public Collection<Genome> reproduceAsexually(int amount, @NotNull ReproductionConfig config,
		int minimalSpeciesSize, @NotNull Map<UUID, List<Genome>> sortedGenomesBySpecies) {
		List<Species> parentSpecies = collectParentSpecies(minimalSpeciesSize);
		Map<Species, List<Pair<Genome, Double>>> parentGenomes =
			generateGenomeProbabilities(config, parentSpecies, sortedGenomesBySpecies);
		List<Pair<Species, Double>> normalizedSpeciesProbabilities =
			generateSpeciesProbabilities(parentSpecies);
		
		List<Genome> newGenomes = new ArrayList<>();
		
		// create the new genomes
		while (newGenomes.size() < amount) {
			Species parentSpecies1 = selectSpecies(normalizedSpeciesProbabilities);
			Genome parent1 = selectGenome(parentGenomes.get(parentSpecies1));
			Genome child = parent1.copy(true);
			newGenomes.add(child);
		}
		
		return newGenomes;
	}
	
	/**
	 * Reproduces the population.
	 *
	 * @param target The target population size.
	 * @param config The configuration for the reproduction.
	 * @return The created genomes.
	 */
	@NotNull
	public Collection<Genome> reproduce(int target, @NotNull ReproductionConfig config) {
		// TODO: Implement reproduction
		throw new UnsupportedOperationException("Reproduction is not yet implemented.");
	}
	
}
