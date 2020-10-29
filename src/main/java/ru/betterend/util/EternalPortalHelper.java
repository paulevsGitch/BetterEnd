package ru.betterend.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.blocks.EternalPedestal;
import ru.betterend.registry.EndBlocks;

public class EternalPortalHelper {
	private final static Map<Integer, Integer> structureMap = new HashMap<Integer, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put(0, 7);
			put(1, 1);
			put(1, 11);
			put(11, 1);
			put(11, 11);
			put(12, 7);
		}
	};	
	private final static Block PEDESTAL = EndBlocks.ETERNAL_PEDESTAL;
	private final static BooleanProperty ACTIVE = EternalPedestal.ACTIVATED;
	
	private static int centerX = 6;
	private static int centerZ = 6;
	
	public static boolean checkPortalStructure(World world, BlockPos pos) {
		if (!world.getBlockState(pos).isOf(PEDESTAL)) return false;
		return false;
	}
	
	private static BlockPos finedCorner(World world, BlockPos pos) {
		
		return pos;
	}
}
