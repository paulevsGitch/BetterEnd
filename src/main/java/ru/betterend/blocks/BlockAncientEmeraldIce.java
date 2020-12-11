package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndParticles;
import ru.betterend.util.BlocksHelper;

public class BlockAncientEmeraldIce extends BlockBase {
	public BlockAncientEmeraldIce() {
		super(FabricBlockSettings.copyOf(Blocks.BLUE_ICE).ticksRandomly());
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		Direction dir = BlocksHelper.randomDirection(random);
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
		for (int i = 0; i < 20; i++) {
			int side = random.nextInt(3);
			double x = (side == 0 ? random.nextDouble() : random.nextBoolean() ? 0 : 1) + pos.getX();
			double y = (side == 1 ? random.nextDouble() : random.nextBoolean() ? 0 : 1) + pos.getY();
			double z = (side == 2 ? random.nextDouble() : random.nextBoolean() ? 0 : 1) + pos.getZ();
			world.addParticle(EndParticles.SNOWFLAKE, x, y, z, 0, 0, 0);
		}
	}
}
