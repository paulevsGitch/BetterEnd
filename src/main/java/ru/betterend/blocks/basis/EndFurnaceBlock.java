package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Map;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import ru.betterend.blocks.entities.EFurnaceBlockEntity;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndFurnaceBlock extends FurnaceBlock implements BlockPatterned {
	public EndFurnaceBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new EFurnaceBlockEntity();
	}
	
	@Override
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EFurnaceBlockEntity) {
			player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
			player.incrementStat(Stats.INTERACT_WITH_FURNACE);
		}
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterns.createJson(data, blockId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		String add = block.contains("_on") ? "_on" : "";
		Identifier blockId = Registry.BLOCK.getId(this);
		Map<String, String> map = Maps.newHashMap();
		map.put("%top%", blockId.getPath() + "_top");
		map.put("%front%", blockId.getPath() + "_front" + add);
		map.put("%side%", blockId.getPath() + "_side");
		map.put("%bottom%", blockId.getPath() + "_bottom");
		return Patterns.createJson(Patterns.BLOCK_FURNACE, map);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_FURNACE;
	}
}
