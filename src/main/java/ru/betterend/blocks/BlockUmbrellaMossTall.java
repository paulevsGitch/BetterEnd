package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockDoublePlant;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.util.BlocksHelper;

public class BlockUmbrellaMossTall extends BlockDoublePlant {
	public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 3);
	
	public BlockUmbrellaMossTall() {
		super(12);
	}
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(BlockRegistry.UMBRELLA_MOSS));
		world.spawnEntity(item);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		int rot = world.random.nextInt(4);
		BlocksHelper.setWithoutUpdate(world, pos, this.getDefaultState().with(ROTATION, rot));
		BlocksHelper.setWithoutUpdate(world, pos.up(), this.getDefaultState().with(ROTATION, rot).with(TOP, true));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		super.appendProperties(stateManager);
		stateManager.add(ROTATION);
	}
}
