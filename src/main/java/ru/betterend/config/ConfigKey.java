package ru.betterend.config;

import net.minecraft.util.Identifier;

public class ConfigKey {
	private final String path[];
	private final String entry;
	private final boolean root;
	
	public ConfigKey(String entry, String... path) {
		this.validate(entry);
		this.path = path;
		this.entry = entry;
		this.root = path.length == 0 || (path.length == 1 && path[0].isEmpty());
	}
	
	public ConfigKey(String entry, Identifier path) {
		this(entry, path.getNamespace(), path.getPath());
	}

	public String[] getPath() {
		return path;
	}

	public String getEntry() {
		return entry;
	}
	
	public boolean isRoot() {
		return root;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + path.hashCode();
		result = prime * result + entry.hashCode();
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
		if (other.path.length != path.length) {
			return false;
		}
		for (int i = 0; i < path.length; i++) {
			if (!path[i].equals(other.path[i])) {
				return false;
			}
		}
		if (!entry.equals(other.entry)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		if (root) {
			return String.format("[root]:%s", entry);
		}
		String p = path[0];
		for (int i = 1; i < path.length; i++) {
			p += "." + path[i];
		}
		return String.format("%s:%s", p, entry);
	}
	
	private void validate(String entry) {
		if (entry == null) {
			throw new NullPointerException("Config key must be not null!");
		}
		if (entry.isEmpty()) {
			throw new IndexOutOfBoundsException("Config key must be not empty!");
		}
	}
}
