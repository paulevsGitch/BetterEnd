package ru.betterend.blocks;

import java.io.Reader;
import java.util.Map;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import ru.betterend.blocks.basis.EndLanternBlock;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class BulbVineLanternBlock extends EndLanternBlock implements IRenderTypeable, BlockPatterned {
	private static final VoxelShape SHAPE_CEIL = Block.createCuboidShape(4, 4, 4, 12, 16, 12);
	private static final VoxelShape SHAPE_FLOOR = Block.createCuboidShape(4, 0, 4, 12, 12, 12);

	public BulbVineLanternBlock() {
		this(FabricBlockSettings.of(Material.METAL).sounds(SoundType.LANTERN).hardness(1).resistance(1)
				.breakByTool(FabricToolTags.PICKAXES).materialColor(MaterialColor.LIGHT_GRAY).requiresTool()
				.luminance(15));
	}

	public BulbVineLanternBlock(FabricBlockSettings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return state.getValue(IS_FLOOR) ? SHAPE_FLOOR : SHAPE_CEIL;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}

	@Override
	public String getStatesPattern(Reader data) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(data, blockId.getPath(), blockId.getPath());
	}

	@Override
	public String getModelPattern(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		Map<String, String> map = Maps.newHashMap();
		map.put("%glow%", getGlowTexture());
		map.put("%metal%", getMetalTexture(blockId));
		if (block.contains("item") || block.contains("ceil")) {
			return Patterns.createJson(Patterns.BLOCK_BULB_LANTERN_CEIL, map);
		} else {
			return Patterns.createJson(Patterns.BLOCK_BULB_LANTERN_FLOOR, map);
		}
	}

	protected String getMetalTexture(ResourceLocation blockId) {
		String name = blockId.getPath();
		name = name.substring(0, name.indexOf('_'));
		return name + "_bulb_vine_lantern_metal";
	}

	protected String getGlowTexture() {
		return "bulb_vine_lantern_bulb";
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_BULB_LANTERN;
	}
}
