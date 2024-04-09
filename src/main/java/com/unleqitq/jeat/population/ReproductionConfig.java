package com.unleqitq.jeat.population;

import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.ToDoubleFunction;

/**
 * This class contains the configuration for the reproduction of the population.
 */
@Builder
public class ReproductionConfig {
	
	/**
	 * Whether to use sexual or asexual reproduction.<br>
	 * If {@code true}, sexual reproduction will be used,
	 * otherwise asexual reproduction will be used.
	 */
	@Builder.Default
	public boolean sexualReproduction = true;
	
	/**
	 * The function to calculate the combined fitness of all genomes in a species.
	 */
	@Builder.Default
	@NotNull
	public ToDoubleFunction<Collection<Genome>> aggregationFunction =
		genomes -> genomes.stream().mapToDouble(Genome::fitness).average().orElse(0);
	
	/**
	 * The minimal size of a species to be allowed to reproduce.
	 */
	@Builder.Default
	public int minimalSpeciesSize = 10;
	
	/**
	 * The minimal amount of species to be used for reproduction.<br>
	 * If the amount of species is smaller than this value,
	 * first {@link #discardRatio} will be reduced,
	 * then {@link #minimalSpeciesSize} will be increased to reach this value.
	 */
	@Builder.Default
	public int minimalSpeciesAmount = 5;
	
	/**
	 * The ratio of the genomes of a species that will be kept for the next generation.
	 * (depending on the fitness of the genomes)<br>
	 * The rest will be used for the reproduction but will not be kept for the next generation.
	 */
	@Builder.Default
	public double survivalRate = 0.1;
	
	/**
	 * The ratio of the genomes of a species that will be used for reproduction. (the best genomes)
	 */
	@Builder.Default
	public double parentRatio = 0.2;
	
	/**
	 * The ratio of species that will be discarded before reproduction. (the worst species)
	 */
	@Builder.Default
	public double discardRatio = 0.1;
	
	/**
	 * The ratio of genomes that will have a copy of themselves
	 * in the next generation that is not mutated. (the best genomes)<br>
	 * This is used to make sure that the population does not get worse over time.<br>
	 * The genomes are selected by the fitness of the genomes
	 * and completely independent of the species.
	 */
	@Builder.Default
	public double elitismRatio = 0.05;
	
	/**
	 * Settings for sexual reproduction. (only used if {@link ReproductionConfig#sexualReproduction} is {@code true})
	 */
	@Builder.Default
	@Nullable
	public SexualReproductionConfig sexualReproductionConfig =
		SexualReproductionConfig.builder().build();
	
	/**
	 * This class contains the configuration for sexual reproduction.<br>
	 * This is only used if {@link ReproductionConfig#sexualReproduction} is {@code true}.
	 */
	@Builder
	public static class SexualReproductionConfig {
		
		/**
		 * The probability of selecting a genome from the same species for mating. (if possible)
		 */
		@Builder.Default
		public double interspeciesMatingProbability = 0.1;
		
		/**
		 * Whether the genomes should be swapped before mating if the second genome is fitter than the first genome.<br>
		 * This is only useful if the crossover is not commutative. (e.g. if the order of the parents matters)<br>
		 * By default the crossover is commutative. (the order of the parents does not matter)
		 */
		@Builder.Default
		public boolean swapParents = false;
		
	}
	
}
