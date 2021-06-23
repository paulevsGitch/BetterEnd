package ru.betterend.blocks;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import ru.bclib.blocks.BaseBlockWithEntity;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;
import ru.betterend.registry.EndBlockEntities;

import java.util.List;
import java.util.Random;

public class EndStoneSmelter extends BaseBlockWithEntity {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final String ID = "end_stone_smelter";

	public EndStoneSmelter() {
		super(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_GRAY)
				.hardness(4F)
				.resistance(100F)
				.requiresCorrectToolForDrops()
				.sound(SoundType.STONE));
		this.registerDefaultState(this.stateDefinition.any()
											  .setValue(FACING, Direction.NORTH)
											  .setValue(LIT, false));
	}
	
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			this.openScreen(world, pos, player);
			return InteractionResult.CONSUME;
		}
	}

	private void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EndStoneSmelterBlockEntity) {
			player.openMenu((EndStoneSmelterBlockEntity) blockEntity);
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new EndStoneSmelterBlockEntity(blockPos, blockState);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = Lists.newArrayList(new ItemStack(this));
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof EndStoneSmelterBlockEntity) {
			EndStoneSmelterBlockEntity smelterBlockEntity = (EndStoneSmelterBlockEntity) blockEntity;
			for (int i = 0; i < smelterBlockEntity.getContainerSize(); i++) {
				ItemStack item = smelterBlockEntity.getItem(i);
				if (!item.isEmpty()) {
					drop.add(item);
				}
			}
		}
		return drop;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		//TODO
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT);
	}
	
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		if (state.getValue(LIT)) {
			double x = pos.getX() + 0.5D;
			double y = pos.getY();
			double z = pos.getZ() + 0.5D;
			if (random.nextDouble() < 0.1D) {
			   world.playLocalSound(x, y, z, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = state.getValue(FACING);
			Direction.Axis axis = direction.getAxis();
			double defOffset = random.nextDouble() * 0.6D - 0.3D;
			double offX = axis == Direction.Axis.X ? direction.getStepX() * 0.52D : defOffset;
			double offY = random.nextDouble() * 9.0D / 16.0D;
			double offZ = axis == Direction.Axis.Z ? direction.getStepZ() * 0.52D : defOffset;
			world.addParticle(ParticleTypes.SMOKE, x + offX, y + offY, z + offZ, 0.0D, 0.0D, 0.0D);
		}
	}


	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockEntityType<T> blockEntityType) {
		return level.isClientSide() ? null : createTickerHelper(blockEntityType, EndBlockEntities.END_STONE_SMELTER, EndStoneSmelterBlockEntity::tick);
	}
}
