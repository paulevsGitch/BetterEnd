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
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndFurnaceBlock extends FurnaceBlock implements BlockPatterned, IRenderTypeable {
	public EndFurnaceBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).luminance((state) -> {
			return state.get(LIT) ? 13 : 0;
		}));
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
		Identifier blockId = Registry.BLOCK.getId(this);
		Map<String, String> map = Maps.newHashMap();
		map.put("%top%", blockId.getPath() + "_top");
		map.put("%side%", blockId.getPath() + "_side");
		if (block.contains("_on")) {
			map.put("%front%", blockId.getPath() + "_front_on");
			map.put("%glow%", blockId.getPath() + "_glow");
			return Patterns.createJson(Patterns.BLOCK_FURNACE_GLOW, map);
		}
		else {
			map.put("%front%", blockId.getPath() + "_front");
			return Patterns.createJson(Patterns.BLOCK_FURNACE, map);
		}
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_FURNACE;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}
