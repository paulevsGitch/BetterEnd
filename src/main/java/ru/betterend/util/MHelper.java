package ru.betterend.util;

import java.util.Random;

public class MHelper {
	public static final float PI2 = (float) (Math.PI * 2);
	private static final int ALPHA = 255 << 24;
	public static final Random RANDOM = new Random();

	public static int color(int r, int g, int b) {
		return ALPHA | (r << 16) | (g << 8) | b;
	}

	public static int randRange(int min, int max, Random random) {
		return min + random.nextInt(max - min + 1);
	}
	
	public static double randRange(double min, double max, Random random) {
		return min + random.nextDouble() * (max - min);
	}

	public static float randRange(float min, float max, Random random) {
		return min + random.nextFloat() * (max - min);
	}

	public static byte setBit(byte source, int pos, boolean value) {
		return value ? setBitTrue(source, pos) : setBitFalse(source, pos);
	}

	public static byte setBitTrue(byte source, int pos) {
		source |= 1 << pos;
		return source;
	}

	public static byte setBitFalse(byte source, int pos) {
		source &= ~(1 << pos);
		return source;
	}

	public static boolean getBit(byte source, int pos) {
		return ((source >> pos) & 1) == 1;
	}

	public static int floor(float x) {
		return x < 0 ? (int) (x - 1) : (int) x;
	}

	public static float wrap(float x, float side) {
		return x - floor(x / side) * side;
	}

	public static int floor(double x) {
		return x < 0 ? (int) (x - 1) : (int) x;
	}

	public static int min(int a, int b) {
		return a < b ? a : b;
	}
	
	public static int max(int a, int b) {
		return a > b ? a : b;
	}
	
	public static float min(float a, float b) {
		return a < b ? a : b;
	}
	
	public static float max(float a, float b) {
		return a > b ? a : b;
	}
	
	public static float max(float a, float b, float c) {
		return max(a, max(b, c));
	}
	
	public static boolean isEven(int num) {
		return (num & 1) == 0;
	}
	
	public static float lengthSqr(float x, float y, float z) {
		return x * x + y * y + z * z;
	}
	
	public static double lengthSqr(double x, double y, double z) {
		return x * x + y * y + z * z;
	}
	
	public static float length(float x, float y, float z) {
		return (float) Math.sqrt(lengthSqr(x, y, z));
	}
	
	public static float lengthSqr(float x, float y) {
		return x * x + y * y;
	}
	
	public static double lengthSqr(double x, double y) {
		return x * x + y * y;
	}
	
	public static float length(float x, float y) {
		return (float) Math.sqrt(lengthSqr(x, y));
	}
	
	public static double length(double x, double y) {
		return Math.sqrt(lengthSqr(x, y));
	}
	
	public static float dot(float x1, float y1, float z1, float x2, float y2, float z2) {
		return x1 * x2 + y1 * y2 + z1 * z2;
	}
	
	public static float dot(float x1, float y1, float x2, float y2) {
		return x1 * x2 + y1 * y2;
	}
	
	public static int getSeed(int seed, int x, int y) {
		int h = seed + x * 374761393 + y * 668265263;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}

	public static int getSeed(int seed, int x, int y, int z) {
		int h = seed + x * 374761393 + y * 668265263 + z;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	public static <T> void shuffle(T[] array, Random random) {
		for (int i = 0; i < array.length; i++) {
			int i2 = random.nextInt(array.length);
			T element = array[i];
			array[i] = array[i2];
			array[i2] = element;
		}
	}

	public static int pow2(int i) {
		return i * i;
	}
}
