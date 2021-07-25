package ru.betterend;

import ru.bclib.api.datafixer.DataFixerAPI;
import ru.bclib.api.datafixer.Patch;

public class DataFixer {
	public static void register() {
		DataFixerAPI.registerPatch(() -> {
			return new BetterEndPatch();
		});
	}
	
	private static final class BetterEndPatch extends Patch {
		protected BetterEndPatch() {
			super(BetterEnd.MOD_ID, "0.11.0");
			System.out.println("BE Patch!");
		}
	}
}
