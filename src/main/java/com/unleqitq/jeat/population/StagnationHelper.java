package com.unleqitq.jeat.population;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.genetics.genome.Genome;
import com.unleqitq.jeat.genetics.species.Species;
import com.unleqitq.jeat.internal.InternalUse;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.ToDoubleFunction;

/**
 * This class contains helper methods to detect stagnation in the population.
 * <p>
 * This class is not intended to be used by the user.
 */
@InternalUse
public class StagnationHelper {
	
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
	public StagnationHelper(@NotNull Population population) {
		this.population = population;
		this.jeat = population.jeat();
	}
	
	/**
	 * Resets the stagnation fitness of all species in the population.
	 */
	public void resetStagnationFitness() {
		population.internalSpecies().values().forEach(Species::resetStagnationFitness);
	}
	
	/**
	 * Updates the stagnation fitness of all species in the population.
	 */
	public void updateStagnationFitness() {
		population.internalSpecies().values().forEach(Species::calculateStagnationFitness);
	}
	
	/**
	 * For all species in the population, add the stagnation fitness to the history.<br>
	 * <b style="color:red;">Important: This method should be called after the stagnated species are determined.</b>
	 */
	public void addToHistory() {
		population.internalSpecies().values().forEach(Species::storeStagnationFitness);
	}
	
	/**
	 * Applies stagnation to the population.<br>
	 * This method will remove all stagnated species from the population.<br>
	 * The stagnation fitness of all species will be recalculated at the beginning of this method.<br>
	 * <b style="color:red;">Important: This method should only be called after the fitness of all genomes are set.</b>
	 *
	 * @param addToHistory If {@code true}, the stagnation fitness of all species will be added to their history.
	 * @param aggregationFunction The function to calculate the fitness of a species. (This is for the general fitness of the species and not the stagnation fitness)
	 * @return The Collection of all species that were removed from the population.
	 */
	@NotNull
	public Collection<Species> stagnate(boolean addToHistory,
		@NotNull ToDoubleFunction<Collection<Genome>> aggregationFunction) {
		// Recalculate the stagnation fitness of all species
		updateStagnationFitness();
		
		// Get all stagnated species
		Collection<Species> stagnatedSpecies = getStagnatedSpecies(aggregationFunction);
		
		// Add the stagnation fitness to the history
		// This should be done after the stagnated species are determined
		if (addToHistory) addToHistory();
		
		// Remove all stagnated species from the population
		stagnatedSpecies.forEach(population::removeSpecies);
		
		return stagnatedSpecies;
	}
	
	/**
	 * Gets a Collection of all species in the population that are stagnated.<br>
	 * This method respects the {@link com.unleqitq.jeat.config.StagnationConfig#speciesElitism} setting.<br>
	 * <b style="color:red;">Important: This method should be called before the history is updated.</b>
	 *
	 * @param aggregationFunction The function to calculate the fitness of a species. (This is for the general fitness of the species and not the stagnation fitness)
	 * @return A Collection of all species in the population that are stagnated.
	 */
	@NotNull
	public Collection<Species> getStagnatedSpecies(
		@NotNull ToDoubleFunction<Collection<Genome>> aggregationFunction) {
		Set<Species> stagnatedSpecies = new HashSet<>();
		
		// Add all stagnated species to the collection
		population.internalSpecies()
			.values()
			.stream()
			.filter(this::isStagnated)
			.forEach(stagnatedSpecies::add);
		
		// Remove the best species from the stagnated species list according to the elitism setting
		int elitism = population.jeat().config().stagnation.speciesElitism;
		if (elitism > 0)
			population.fittestSpecies(elitism, aggregationFunction).forEach(stagnatedSpecies::remove);
		
		return stagnatedSpecies;
	}
	
	/**
	 * Checks if a species is stagnated.
	 *
	 * @param species The species to check.
	 * @return {@code true} if the species is stagnated, {@code false} otherwise.
	 */
	public boolean isStagnated(@NotNull Species species) {
		int maxStagnation = jeat.config().stagnation.maxStagnation;
		// Species with fewer generations than the max stagnation are not stagnated
		if (species.stagnationHistory().size() < maxStagnation) return false;
		double fitness = species.stagnationFitness();
		
		double lastFitness =
			species.stagnationHistory().get(species.stagnationHistory().size() - maxStagnation);
		double improvementThreshold = jeat.config().stagnation.improvementThreshold;
		double totalImprovement = 0;
		for (int i = 1; i < maxStagnation + 1; i++) {
			double currentFitness;
			if (i == maxStagnation) currentFitness = fitness;
			else currentFitness =
				species.stagnationHistory().get(species.stagnationHistory().size() - maxStagnation + i);
			totalImprovement += Math.max(0, currentFitness - lastFitness);
			if (currentFitness > lastFitness) lastFitness = currentFitness;
		}
		
		return totalImprovement < improvementThreshold;
	}
	
	/**
	 * Gets the highest fitness of the species in the given time span in the history,
	 * offset by the given offset value.<br>
	 * If there are no values in the desired time span, {@link Double#NEGATIVE_INFINITY} is returned.
	 *
	 * @param species The species to check.
	 * @param timeSpan The time span to check.
	 * @param offset The offset to apply to the time span.
	 * @return The highest fitness of the species in the given time span.
	 */
	private double getHighestFitness(@NotNull Species species, int timeSpan, int offset) {
		// get the history of the species
		// newer values are at the end of the list
		List<Double> stagnationHistory = species.stagnationHistory();
		
		// get the sublist of the history that is relevant for the time span
		int endIndex = Math.max(0, stagnationHistory.size() - offset);
		int startIndex = Math.max(0, endIndex - timeSpan);
		// check if the clamped time span is valid
		if (startIndex >= endIndex) return Double.NEGATIVE_INFINITY;
		// get the relevant history
		List<Double> history = stagnationHistory.subList(startIndex, endIndex);
		
		// get the highest fitness in the history
		return history.stream().max(Double::compare).orElse(Double.NEGATIVE_INFINITY);
	}
	
	/**
	 * Gets the lowest fitness of the species in the given time span in the history,
	 * offset by the given offset value.<br>
	 * If there are no values in the desired time span, {@link Double#POSITIVE_INFINITY} is returned.
	 *
	 * @param species The species to check.
	 * @param timeSpan The time span to check.
	 * @param offset The offset to apply to the time span.
	 * @return The lowest fitness of the species in the given time span.
	 */
	private double getLowestFitness(@NotNull Species species, int timeSpan, int offset) {
		// get the history of the species
		// newer values are at the end of the list
		List<Double> stagnationHistory = species.stagnationHistory();
		
		// get the sublist of the history that is relevant for the time span
		int endIndex = Math.max(0, stagnationHistory.size() - offset);
		int startIndex = Math.max(0, endIndex - timeSpan);
		// check if the clamped time span is valid
		if (startIndex >= endIndex) return Double.POSITIVE_INFINITY;
		// get the relevant history
		List<Double> history = stagnationHistory.subList(startIndex, endIndex);
		
		// get the lowest fitness in the history
		return history.stream().min(Double::compare).orElse(Double.POSITIVE_INFINITY);
	}
	
}
