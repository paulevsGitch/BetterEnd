package ru.betterend.blocks;

import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class BrimstoneBlock extends BlockBase {
	public static final BooleanProperty ACTIVATED = BlockProperties.ACTIVE;
	
	public BrimstoneBlock() {
		super(FabricBlockSettings.copyOf(Blocks.END_STONE).materialColor(MaterialColor.BROWN).ticksRandomly());
		setDefaultState(stateManager.getDefaultState().with(ACTIVATED, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVATED);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.isClient()) {
			updateChunks((ClientWorld) world, pos);
		}
	}
	
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.isClient()) {
			updateChunks((ClientWorld) world, pos);
		}
	}
	
	private void updateChunks(ClientWorld world, BlockPos pos) {
		int y = pos.getY() >> 4;
		int x1 = (pos.getX() - 2) >> 4;
		int z1 = (pos.getZ() - 2) >> 4;
		int x2 = (pos.getX() + 2) >> 4;
		int z2 = (pos.getZ() + 2) >> 4;
		for (int x = x1; x <= x2; x++) {
			for (int z = z1; z <= z2; z++) {
				world.scheduleBlockRenders(x, y, z);
			}
		}
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		boolean deactivate = true;
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			if (world.getFluidState(pos.offset(dir)).getFluid().equals(Fluids.WATER)) {
				deactivate = false;
				break;
			}
		}
		if (state.get(ACTIVATED)) {
			if (deactivate) {
				world.setBlockState(pos, getDefaultState().with(ACTIVATED, false));
			}
			else if (state.get(ACTIVATED) && random.nextInt(16) == 0) {
				Direction dir = BlocksHelper.randomDirection(random);
				BlockPos side = pos.offset(dir);
				BlockState sideState = world.getBlockState(side);
				if (sideState.getBlock() instanceof SulphurCrystalBlock) {
					if (sideState.get(SulphurCrystalBlock.AGE) < 2 && sideState.get(SulphurCrystalBlock.WATERLOGGED)) {
						int age = sideState.get(SulphurCrystalBlock.AGE) + 1;
						world.setBlockState(side, sideState.with(SulphurCrystalBlock.AGE, age));
					}
				}
				else if (sideState.getFluidState().getFluid() == Fluids.WATER) {
					BlockState crystal = EndBlocks.SULPHUR_CRYSTAL.getDefaultState()
							.with(SulphurCrystalBlock.FACING, dir)
							.with(SulphurCrystalBlock.WATERLOGGED, true)
							.with(SulphurCrystalBlock.AGE, 0);
					world.setBlockState(side, crystal);
				}
			}
		}
		else if (!deactivate && !state.get(ACTIVATED)) {
			world.setBlockState(pos, getDefaultState().with(ACTIVATED, true));
		}
	}
}
