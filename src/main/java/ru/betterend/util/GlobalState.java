package ru.betterend.util;

import net.minecraft.core.BlockPos.MutableBlockPos;

import java.util.concurrent.ConcurrentHashMap;

public class GlobalState {
	private static final ConcurrentHashMap<Thread, GlobalState> statePool = new ConcurrentHashMap<>();
	public static void clearStatePool(){
		statePool.clear();
	}
	public static GlobalState stateForThread() { return statePool.computeIfAbsent(Thread.currentThread(), t-> new GlobalState()); }

	public final MutableBlockPos POS = new MutableBlockPos();
}
