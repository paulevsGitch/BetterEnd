package ru.betterend.blocks;

import java.util.EnumMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.basis.BlockAttached;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;

public class BlockSmallJellyshroom extends BlockAttached implements IRenderTypeable {
	private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(Direction.class);
	
	public BlockSmallJellyshroom() {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.NETHER_WART)
				.breakByHand(true)
				.noCollision());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return BOUNDING_SHAPES.get(state.get(FACING));
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootContextParameters.TOOL);
		if (tool != null && tool.getItem().isIn(FabricToolTags.SHEARS) || EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Lists.newArrayList(new ItemStack(this));
		}
		else {
			return Lists.newArrayList();
		}
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
}
