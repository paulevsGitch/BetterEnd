package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;

public class BlockBulbVineLantern extends BlockBaseNotFull implements IRenderTypeable {
	private static final VoxelShape SHAPE_CEIL = Block.createCuboidShape(4, 4, 4, 12, 16, 12);
	
	public BlockBulbVineLantern() {
		super(FabricBlockSettings.of(Material.METAL)
				.sounds(BlockSoundGroup.LANTERN)
				.hardness(1)
				.resistance(1)
				.breakByTool(FabricToolTags.PICKAXES)
				.materialColor(MaterialColor.LIGHT_GRAY)
				.requiresTool()
				.luminance(15));
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return sideCoversSmallSquare(world, pos.up(), Direction.DOWN);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPE_CEIL;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}
