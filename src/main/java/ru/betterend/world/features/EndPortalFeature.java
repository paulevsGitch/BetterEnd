package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import ru.betterend.blocks.EndPortalBlock;
import ru.betterend.blocks.RunedFlavolite;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.util.BlocksHelper;

public class EndPortalFeature extends Feature<EndPortalFeatureConfig> {

	public EndPortalFeature(Block frameBlock) {
		super(EndPortalFeatureConfig.CODEC);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			EndPortalFeatureConfig config) {
		
		BlockState portalFrame = config.frameBlock.getDefaultState().with(RunedFlavolite.ACTIVATED, config.activated);
		BlockState portalBlock = BlockRegistry.END_PORTAL_BLOCK.getDefaultState().with(EndPortalBlock.AXIS, config.axis);
		BlockPos bottomCorner = pos;
		BlockPos topCorner;
		if (config.axis.equals(Direction.Axis.X)) {
			topCorner = bottomCorner.add(0, 4, 3);
		} else {
			topCorner = bottomCorner.add(3, 4, 0);
		}
		
		for(BlockPos position : BlockPos.iterate(bottomCorner, topCorner)) {
			if (position.equals(bottomCorner) || position.equals(topCorner) ||
				position.getX() == bottomCorner.getX() && position.getZ() == bottomCorner.getZ() ||
				position.getX() == topCorner.getX() && position.getZ() == topCorner.getZ()) {
				
				BlocksHelper.setWithoutUpdate(world, position, portalFrame);
			} else if (config.axis.equals(Direction.Axis.X)) {
				if (position.getX() == bottomCorner.getX() && position.getY() == bottomCorner.getY() ||
					position.getX() == topCorner.getX() && position.getY() == topCorner.getY()) {
					
					BlocksHelper.setWithoutUpdate(world, position, portalFrame);
				} else if (config.activated) {
					BlocksHelper.setWithoutUpdate(world, position, portalBlock);
				}
			} else {
				if (position.getZ() == bottomCorner.getZ() && position.getY() == bottomCorner.getY() ||
					position.getZ() == topCorner.getZ() && position.getY() == topCorner.getY()) {
						
					BlocksHelper.setWithoutUpdate(world, position, portalFrame);
				} else if (config.activated) {
					BlocksHelper.setWithoutUpdate(world, position, portalBlock);
				}
			}
		}
		
		return true;
	}
}
