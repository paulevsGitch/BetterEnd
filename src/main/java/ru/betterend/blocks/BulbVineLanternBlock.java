package ru.betterend.blocks;

import java.io.Reader;
import java.util.Map;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.basis.EndLanternBlock;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class BulbVineLanternBlock extends EndLanternBlock implements IRenderTypeable, BlockPatterned {
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
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterns.createJson(data, blockId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		Map<String, String> map = Maps.newHashMap();
		map.put("%glow%", getGlowTexture());
		map.put("%metal%", getMetalTexture(blockId));
		if (block.contains("item") || block.contains("ceil")) {
			return Patterns.createJson(Patterns.BLOCK_BULB_LANTERN_CEIL, map);
		}
		else {
			return Patterns.createJson(Patterns.BLOCK_BULB_LANTERN_FLOOR, map);
		}
	}
	
	protected String getMetalTexture(Identifier blockId) {
		String name = blockId.getPath();
		name = name.substring(0, name.indexOf('_'));
		return name + "_bulb_vine_lantern_metal";
	}
	
	protected String getGlowTexture() {
		return "bulb_vine_lantern_bulb";
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_BULB_LANTERN;
	}
}
