package com.unleqitq.jeat.genetics.species;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeciesStore {
	
	/**
	 * The species in the store
	 */
	@NotNull
	private final Map<UUID, Species> species = new HashMap<>();
	
	
	public SpeciesStore() {
	}
	
	/**
	 * Add a species to the store
	 * @param species The species to add
	 */
	public void add(@NotNull Species species) {
		this.species.put(species.id(), species);
	}
	
	/**
	 * Remove a species from the store
	 * @param species The species to remove
	 */
	public void remove(@NotNull Species species) {
		this.species.remove(species.id());
	}
	
	/**
	 * Remove a species from the store
	 * @param id The id of the species to remove
	 */
	public void remove(@NotNull UUID id) {
		Species species = this.species.remove(id);
	}
	
	/**
	 * Get a species from the store
	 * @param id The id of the species to get
	 * @return The species with the given id, or null if it doesn't exist
	 */
	@Nullable
	public Species get(@NotNull UUID id) {
		return species.get(id);
	}
	
	/**
	 * Get all species in the store
	 * @return A collection of all species in the store
	 */
	@NotNull
	public Collection<Species> species() {
		return species.values();
	}
	
	/**
	 * Get the number of species in the store
	 * @return The number of species in the store
	 */
	public int size() {
		return species.size();
	}
	
}
