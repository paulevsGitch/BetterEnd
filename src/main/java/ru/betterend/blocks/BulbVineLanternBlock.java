package ru.betterend.blocks;

import java.io.Reader;
import java.util.Map;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.betterend.blocks.basis.EndLanternBlock;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class BulbVineLanternBlock extends EndLanternBlock implements IRenderTypeable, BlockPatterned {
	private static final VoxelShape SHAPE_CEIL = Block.box(4, 4, 4, 12, 16, 12);
	private static final VoxelShape SHAPE_FLOOR = Block.box(4, 0, 4, 12, 12, 12);
	
	public BulbVineLanternBlock() {
		this(FabricBlockSettings.of(Material.METAL)
				.hardness(1)
				.resistance(1)
				.breakByTool(FabricToolTags.PICKAXES)
				.materialColor(MaterialColor.COLOR_LIGHT_GRAY)
				.luminance(15)
				.requiresCorrectToolForDrops()
				.sound(SoundType.LANTERN));
	}
	
	public BulbVineLanternBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
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
	public String getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
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
