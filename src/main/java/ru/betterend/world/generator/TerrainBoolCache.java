package ru.betterend.world.generator;

public class TerrainBoolCache {
	private byte[] data = new byte[16384];

	public static int scaleCoordinate(int value) {
		return value >> 7;
	}

	private int getIndex(int x, int z) {
		return x << 7 | z;
	}

	public void setData(int x, int z, byte value) {
		data[getIndex(x & 127, z & 127)] = value;
	}

	public byte getData(int x, int z) {
		return data[getIndex(x & 127, z & 127)];
	}
}
