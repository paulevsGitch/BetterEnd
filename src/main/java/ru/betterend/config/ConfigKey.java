package ru.betterend.config;

import net.minecraft.util.Identifier;

public class ConfigKey {
	private final Identifier category;
	private final Identifier parameter;
	
	public ConfigKey(Identifier category, Identifier parameter) {
		this.category = category;
		this.parameter = parameter;
	}

	public Identifier getCategory() {
		return category;
	}

	public Identifier getParameter() {
		return parameter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
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
		if (parameter == null) {
			if (other.parameter != null) {
				return false;
			}
		} else if (!parameter.equals(other.parameter)) {
			return false;
		}
		return true;
	}
}
