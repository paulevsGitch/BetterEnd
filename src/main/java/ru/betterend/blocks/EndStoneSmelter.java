package ru.betterend.blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockRenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalFacingBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import ru.betterend.blocks.basis.BaseBlockWithEntity;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;

public class EndStoneSmelter extends BaseBlockWithEntity {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty LIT = Properties.LIT;
	public static final String ID = "end_stone_smelter";

	public EndStoneSmelter() {
		super(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_GRAY).hardness(4F).resistance(100F)
				.requiresTool().sounds(SoundType.STONE));
		this.setDefaultState(this.stateManager.defaultBlockState().with(FACING, Direction.NORTH).with(LIT, false));
	}

	public ActionResult onUse(BlockState state, Level world, BlockPos pos, Player player, Hand hand,
			BlockHitResult hit) {
		if (world.isClientSide) {
			return ActionResult.SUCCESS;
		} else {
			this.openScreen(world, pos, player);
			return ActionResult.CONSUME;
		}
	}

	private void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EndStoneSmelterBlockEntity) {
			player.openHandledScreen((EndStoneSmelterBlockEntity) blockEntity);
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.defaultBlockState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new EndStoneSmelterBlockEntity();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = Lists.newArrayList(new ItemStack(this));
		BlockEntity blockEntity = builder.getNullable(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof EndStoneSmelterBlockEntity) {
			EndStoneSmelterBlockEntity smelterBlockEntity = (EndStoneSmelterBlockEntity) blockEntity;
			for (int i = 0; i < smelterBlockEntity.size(); i++) {
				ItemStack item = smelterBlockEntity.getStack(i);
				if (!item.isEmpty()) {
					drop.add(item);
				}
			}
		}
		return drop;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, Level world, BlockPos pos) {
		// TODO
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return (BlockState) state.with(FACING, rotation.rotate((Direction) state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation((Direction) state.getValue(FACING)));
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
				world.playLocalSound(x, y, z, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F,
						1.0F, false);
			}

			Direction direction = (Direction) state.getValue(FACING);
			Direction.Axis axis = direction.getAxis();
			double defOffset = random.nextDouble() * 0.6D - 0.3D;
			double offX = axis == Direction.Axis.X ? direction.getOffsetX() * 0.52D : defOffset;
			double offY = random.nextDouble() * 9.0D / 16.0D;
			double offZ = axis == Direction.Axis.Z ? direction.getOffsetZ() * 0.52D : defOffset;
			world.addParticle(ParticleTypes.SMOKE, x + offX, y + offY, z + offZ, 0.0D, 0.0D, 0.0D);
		}
	}
}
