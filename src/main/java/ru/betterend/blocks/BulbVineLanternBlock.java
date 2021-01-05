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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.basis.EndLanternBlock;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;

public class BulbVineLanternBlock extends EndLanternBlock implements IRenderTypeable {
	private static final VoxelShape SHAPE_CEIL = Block.createCuboidShape(4, 4, 4, 12, 16, 12);
	private static final VoxelShape SHAPE_FLOOR = Block.createCuboidShape(4, 0, 4, 12, 12, 12);
	
	public BulbVineLanternBlock() {
		this(FabricBlockSettings.of(Material.METAL)
				.sounds(BlockSoundGroup.LANTERN)
				.hardness(1)
				.resistance(1)
				.breakByTool(FabricToolTags.PICKAXES)
				.materialColor(MaterialColor.LIGHT_GRAY)
				.requiresTool()
				.luminance(15));
	}
	
	public BulbVineLanternBlock(FabricBlockSettings settings) {
		super(settings);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return state.get(IS_FLOOR) ? SHAPE_FLOOR : SHAPE_CEIL;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}
