package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndParticles;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class AncientEmeraldIceBlock extends BlockBase {
	public AncientEmeraldIceBlock() {
		super(FabricBlockSettings.copyOf(Blocks.BLUE_ICE).ticksRandomly());
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		Direction dir = BlocksHelper.randomDirection(random);
		
		if (random.nextBoolean()) {
			int x = MHelper.randRange(-2, 2, random);
			int y = MHelper.randRange(-2, 2, random);
			int z = MHelper.randRange(-2, 2, random);
			BlockPos p = pos.add(x, y, z);
			if (world.getBlockState(p).isOf(Blocks.WATER)) {
				world.setBlockState(p, EndBlocks.EMERALD_ICE.getDefaultState());
				makeParticles(world, p, random);
			}
		}
		
		pos = pos.offset(dir);
		state = world.getBlockState(pos);
		if (state.isOf(Blocks.WATER)) {
			world.setBlockState(pos, EndBlocks.EMERALD_ICE.getDefaultState());
			makeParticles(world, pos, random);
		}
		else if (state.isOf(EndBlocks.EMERALD_ICE)) {
			world.setBlockState(pos, EndBlocks.DENSE_EMERALD_ICE.getDefaultState());
			makeParticles(world, pos, random);
		}
	}
	
	private void makeParticles(ServerWorld world, BlockPos pos, Random random) {
		world.spawnParticles(EndParticles.SNOWFLAKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 20, 0.5, 0.5, 0.5, 0);
	}
}
