package ru.betterend.blocks;

import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fertilizable;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.basis.AttachedBlock;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;

public class SmallJellyshroomBlock extends AttachedBlock implements IRenderTypeable, Fertilizable {
	private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(Direction.class);

	public SmallJellyshroomBlock() {
		super(FabricBlockSettings.of(Material.PLANT).breakByTool(FabricToolTags.SHEARS).sounds(SoundType.NETHER_WART)
				.breakByHand(true).noCollision());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return BOUNDING_SHAPES.get(state.getValue(FACING));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null && tool.getItem().isIn(FabricToolTags.SHEARS)
				|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Lists.newArrayList(new ItemStack(this));
		} else {
			return Lists.newArrayList();
		}
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		BlockPos blockPos = pos.relative(direction.getOpposite());
		BlockState support = world.getBlockState(blockPos);
		return sideCoversSmallSquare(world, blockPos, direction) && support.isOpaque() && support.getLuminance() == 0;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}

	static {
		BOUNDING_SHAPES.put(Direction.UP, Block.createCuboidShape(3, 0, 3, 13, 16, 13));
		BOUNDING_SHAPES.put(Direction.DOWN, Block.createCuboidShape(3, 0, 3, 13, 16, 13));
		BOUNDING_SHAPES.put(Direction.NORTH, VoxelShapes.cuboid(0.0, 0.0, 0.5, 1.0, 1.0, 1.0));
		BOUNDING_SHAPES.put(Direction.SOUTH, VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0, 0.5));
		BOUNDING_SHAPES.put(Direction.WEST, VoxelShapes.cuboid(0.5, 0.0, 0.0, 1.0, 1.0, 1.0));
		BOUNDING_SHAPES.put(Direction.EAST, VoxelShapes.cuboid(0.0, 0.0, 0.0, 0.5, 1.0, 1.0));
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return state.getValue(FACING) == Direction.UP && world.getBlockState(pos.below()).isIn(EndTags.END_GROUND);
	}

	@Override
	public boolean canGrow(Level world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(16) == 0;
	}

	@Override
	public void grow(ServerLevel world, Random random, BlockPos pos, BlockState state) {
		BlocksHelper.setWithUpdate(world, pos, Blocks.AIR);
		EndFeatures.JELLYSHROOM.getFeature().place(world, null, random, pos, null);
	}
}
