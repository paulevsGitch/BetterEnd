package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.IRenderTyped;
import ru.bclib.util.MHelper;
import ru.betterend.blocks.EndBlockProperties.LumecornShape;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class LumecornBlock extends BaseBlockNotFull implements IRenderTyped {
	public static final EnumProperty<LumecornShape> SHAPE = EnumProperty.create("shape", LumecornShape.class);
	private static final VoxelShape SHAPE_BOTTOM = Block.box(6, 0, 6, 10, 16, 10);
	private static final VoxelShape SHAPE_TOP = Block.box(6, 0, 6, 10, 8, 10);

	public LumecornBlock() {
		super(FabricBlockSettings.of(Material.WOOD)
				.breakByTool(FabricToolTags.AXES)
				.hardness(0.5F)
				.luminance(state -> state.getValue(SHAPE).getLight()));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return state.getValue(SHAPE) == LumecornShape.LIGHT_TOP ? SHAPE_TOP : SHAPE_BOTTOM;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		LumecornShape shape = state.getValue(SHAPE);
		if (shape == LumecornShape.BOTTOM_BIG || shape == LumecornShape.BOTTOM_SMALL) {
			return world.getBlockState(pos.below()).is(TagAPI.END_GROUND);
		}
		else if (shape == LumecornShape.LIGHT_TOP) {
			return world.getBlockState(pos.below()).is(this);
		}
		else {
			return world.getBlockState(pos.below()).is(this) && world.getBlockState(pos.above()).is(this);
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!canSurvive(state, world, pos)) {
			return Blocks.AIR.defaultBlockState();
		}
		else {
			return state;
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		LumecornShape shape = state.getValue(SHAPE);
		if (shape == LumecornShape.BOTTOM_BIG || shape == LumecornShape.BOTTOM_SMALL || shape == LumecornShape.MIDDLE) {
			return Collections.singletonList(new ItemStack(EndBlocks.LUMECORN_SEED, MHelper.randRange(1, 2, MHelper.RANDOM)));
		}
		return MHelper.RANDOM.nextBoolean() ? Collections.singletonList(new ItemStack(EndItems.LUMECORN_ROD)) : Collections.emptyList();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
		LumecornShape shape = state.getValue(SHAPE);
		if (shape == LumecornShape.BOTTOM_BIG || shape == LumecornShape.BOTTOM_SMALL || shape == LumecornShape.MIDDLE) {
			return new ItemStack(EndBlocks.LUMECORN_SEED);
		}
		return new ItemStack(EndItems.LUMECORN_ROD);
	}
}
