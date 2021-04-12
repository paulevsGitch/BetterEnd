package ru.betterend.blocks.basis;

import java.util.EnumMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.level.BlockGetter;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.util.MHelper;

public class FurBlock extends AttachedBlock implements IRenderTypeable {
	private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(Direction.class);
	private final ItemLike drop;
	private final int dropChance;

	public FurBlock(ItemLike drop, int light, int dropChance, boolean wet) {
		super(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).breakByTool(FabricToolTags.SHEARS)
				.sounds(wet ? SoundType.WET_GRASS : SoundType.GRASS).luminance(light).breakByHand(true).noCollision());
		this.drop = drop;
		this.dropChance = dropChance;
	}

	public FurBlock(ItemLike drop, int dropChance) {
		super(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).breakByTool(FabricToolTags.SHEARS)
				.sounds(SoundType.GRASS).breakByHand(true).noCollision());
		this.drop = drop;
		this.dropChance = dropChance;
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
		} else if (dropChance < 1 || MHelper.RANDOM.nextInt(dropChance) == 0) {
			return Lists.newArrayList(new ItemStack(drop));
		} else {
			return Lists.newArrayList();
		}
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}

	static {
		BOUNDING_SHAPES.put(Direction.UP, VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.5, 1.0));
		BOUNDING_SHAPES.put(Direction.DOWN, VoxelShapes.cuboid(0.0, 0.5, 0.0, 1.0, 1.0, 1.0));
		BOUNDING_SHAPES.put(Direction.NORTH, VoxelShapes.cuboid(0.0, 0.0, 0.5, 1.0, 1.0, 1.0));
		BOUNDING_SHAPES.put(Direction.SOUTH, VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0, 0.5));
		BOUNDING_SHAPES.put(Direction.WEST, VoxelShapes.cuboid(0.5, 0.0, 0.0, 1.0, 1.0, 1.0));
		BOUNDING_SHAPES.put(Direction.EAST, VoxelShapes.cuboid(0.0, 0.0, 0.0, 0.5, 1.0, 1.0));
	}
}
