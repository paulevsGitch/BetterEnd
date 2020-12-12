package ru.betterend.config;

import org.jetbrains.annotations.NotNull;

public class ConfigKey {
	private final String owner;
	private final String category;
	private final String entry;
	
	public ConfigKey(@NotNull String owner, @NotNull String category, @NotNull String entry) {
		this.validate(owner, category, entry);
		this.owner = owner;
		this.category = category;
		this.entry = entry;
	}

	public String getOwner() {
		return owner;
	}

	public String getCategory() {
		return category;
	}

	public String getEntry() {
		return entry;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + category.hashCode();
		result = prime * result + entry.hashCode();
		result = prime * result + owner.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ConfigKey)) {
			return false;
		}
		ConfigKey other = (ConfigKey) obj;
		if (category == null) {
			if (other.category != null) {
				return false;
			}
		} else if (!category.equals(other.category)) {
			return false;
		}
		if (entry == null) {
			if (other.entry != null) {
				return false;
			}
		} else if (!entry.equals(other.entry)) {
			return false;
		}
		if (owner == null) {
			if (other.owner != null) {
				return false;
			}
		} else if (!owner.equals(other.owner)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s:%s", owner, category, entry);
	}

	private void validate(String owner, String category, String entry) {
		if (owner == null) {
			throw new NullPointerException("Failed to create ConfigKey: 'owner' can't be null.");
		}
		if (category == null) {
			throw new NullPointerException("Failed to create ConfigKey: 'category' can't be null.");
		}
		if (entry == null) {
			throw new NullPointerException("Failed to create ConfigKey: 'entry' can't be null.");
		}
	}
}
