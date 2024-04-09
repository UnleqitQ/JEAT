package com.unleqitq.jeat.population;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.genetics.genome.Genome;
import com.unleqitq.jeat.genetics.species.Species;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents a population at a specific time.
 */
@Accessors (fluent = true)
@Getter
public class Generation {
	
	/**
	 * The JEAT instance
	 */
	@NotNull
	private final Jeat jeat;
	
	/**
	 * The {@link Population} the generation is a snapshot of.
	 */
	@NotNull
	private final Population population;
	
	/**
	 * The genomes of the generation.
	 */
	@NotNull
	private final Map<UUID, Genome> genomes;
	
	/**
	 * The species of the generation.
	 */
	@NotNull
	private final Map<UUID, Species> species;
	
	/**
	 * Creates a new generation.
	 *
	 * @param population The population the generation is a snapshot of.
	 * @param genomes The genomes of the generation.
	 * @param species The species of the generation.
	 */
	private Generation(@NotNull Population population, @NotNull Map<UUID, Genome> genomes,
		@NotNull Map<UUID, Species> species) {
		this.jeat = population.jeat();
		this.population = population;
		this.genomes = genomes;
		this.species = species;
	}
	
	/**
	 * Creates a new generation.
	 *
	 * @param population The population the generation is a snapshot of.
	 * @return The new generation.
	 */
	@NotNull
	public static Generation create(@NotNull Population population) {
		Map<UUID, Genome> genomes = population.genomes()
			.values()
			.stream()
			.collect(Collectors.toMap(Genome::id, g -> g.copy(true)));
		Map<UUID, Species> species = population.species()
			.values()
			.stream()
			.collect(Collectors.toMap(Species::id, s -> s.copy(true, genomes)));
		return new Generation(population, genomes, species);
	}
	
}
