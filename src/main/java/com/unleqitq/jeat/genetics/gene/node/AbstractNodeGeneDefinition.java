package com.unleqitq.jeat.genetics.gene.node;

import com.unleqitq.jeat.genetics.gene.GeneDefinition;
import com.unleqitq.jeat.internal.GlobalSettings;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Accessors (fluent = true)
@Getter
public abstract class AbstractNodeGeneDefinition<S extends AbstractNodeGeneDefinition<S, G>, G extends AbstractNodeGene<G, S>>
	extends GeneDefinition<UUID, S, G> {
	
	protected final double x;
	protected final boolean input;
	@Nullable
	protected final String name;
	
	protected AbstractNodeGeneDefinition(@NotNull UUID id, double x, boolean input,
		@Nullable String name) {
		super(id);
		if (GlobalSettings.NODE_X_BOUNDS != GlobalSettings.ErrorHandling.IGNORE) {
			String message = null;
			if (input) {
				if (x > 0) {
					message = "Input nodes should not have a x value greater than 0";
				}
				else if (x < -1) {
					message = "Input nodes should not have a x value less than -1";
				}
			}
			else {
				if (x <= 0) {
					message = "Hidden and output nodes should have a x value greater than 0";
				}
				else if (x > 1) {
					message = "Hidden and output nodes should not have a x value greater than 1";
				}
			}
			if (message != null) {
				switch (GlobalSettings.NODE_X_BOUNDS) {
					case THROW -> throw new IllegalArgumentException(message);
					case WARN -> System.err.println("(JEAT) [WARN] " + message);
				}
			}
		}
		this.x = x;
		this.input = input;
		this.name = name;
	}
	
	protected AbstractNodeGeneDefinition(double x, boolean input, @Nullable String name) {
		this(UUID.randomUUID(), x, input, name);
	}
	
}
