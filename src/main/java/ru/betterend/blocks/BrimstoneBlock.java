package ru.betterend.blocks;

import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class BrimstoneBlock extends BlockBase {
	public static final BooleanProperty ACTIVATED = BlockProperties.ACTIVE;

	public BrimstoneBlock() {
		super(FabricBlockSettings.copyOf(Blocks.END_STONE).materialColor(MaterialColor.COLOR_BROWN).ticksRandomly());
		setDefaultState(stateManager.defaultBlockState().with(ACTIVATED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVATED);
	}

	@Override
	public void onPlaced(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
			ItemStack itemStack) {
		if (world.isClientSide()) {
			updateChunks((ClientLevel) world, pos);
		}
	}

	public void onBroken(LevelAccessor world, BlockPos pos, BlockState state) {
		if (world.isClientSide()) {
			updateChunks((ClientLevel) world, pos);
		}
	}

	private void updateChunks(ClientLevel world, BlockPos pos) {
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
	public void scheduledTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		boolean deactivate = true;
		for (Direction dir : BlocksHelper.DIRECTIONS) {
			if (world.getFluidState(pos.relative(dir)).getFluid().equals(Fluids.WATER)) {
				deactivate = false;
				break;
			}
		}
		if (state.getValue(ACTIVATED)) {
			if (deactivate) {
				world.setBlockAndUpdate(pos, getDefaultState().with(ACTIVATED, false));
			} else if (state.getValue(ACTIVATED) && random.nextInt(16) == 0) {
				Direction dir = BlocksHelper.randomDirection(random);
				BlockPos side = pos.relative(dir);
				BlockState sideState = world.getBlockState(side);
				if (sideState.getBlock() instanceof SulphurCrystalBlock) {
					if (sideState.get(SulphurCrystalBlock.AGE) < 2 && sideState.get(SulphurCrystalBlock.WATERLOGGED)) {
						int age = sideState.get(SulphurCrystalBlock.AGE) + 1;
						world.setBlockAndUpdate(side, sideState.with(SulphurCrystalBlock.AGE, age));
					}
				} else if (sideState.getFluidState().getFluid() == Fluids.WATER) {
					BlockState crystal = EndBlocks.SULPHUR_CRYSTAL.defaultBlockState()
							.with(SulphurCrystalBlock.FACING, dir).with(SulphurCrystalBlock.WATERLOGGED, true)
							.with(SulphurCrystalBlock.AGE, 0);
					world.setBlockAndUpdate(side, crystal);
				}
			}
		} else if (!deactivate && !state.getValue(ACTIVATED)) {
			world.setBlockAndUpdate(pos, getDefaultState().with(ACTIVATED, true));
		}
	}
}
