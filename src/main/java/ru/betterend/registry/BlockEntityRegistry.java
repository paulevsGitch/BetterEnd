package ru.betterend.registry;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.BlockBarrel;
import ru.betterend.blocks.basis.BlockChest;
import ru.betterend.blocks.basis.BlockSign;
import ru.betterend.blocks.entities.EBarrelBlockEntity;
import ru.betterend.blocks.entities.EChestBlockEntity;
import ru.betterend.blocks.entities.ESignBlockEntity;

public class BlockEntityRegistry
{
	public static final BlockEntityType<EChestBlockEntity> CHEST = BlockEntityType.Builder.create(EChestBlockEntity::new, getChests()).build(null);
	public static final BlockEntityType<EBarrelBlockEntity> BARREL = BlockEntityType.Builder.create(EBarrelBlockEntity::new, getBarrels()).build(null);
	public static final BlockEntityType<ESignBlockEntity> SIGN = BlockEntityType.Builder.create(ESignBlockEntity::new, getSigns()).build(null);
	
	public static void register() {
		RegisterBlockEntity("chest", CHEST);
		RegisterBlockEntity("barrel", BARREL);
		RegisterBlockEntity("sign", SIGN);
	}
	
	public static void RegisterBlockEntity(String name, BlockEntityType<? extends BlockEntity> type) {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(BetterEnd.MOD_ID, name), type);
	}

	private static Block[] getChests() {
		List<Block> result = Lists.newArrayList();
		ItemRegistry.getModBlocks().forEach((item) -> {
			if (item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof BlockChest) {
					result.add(block);
				}
			}
		});
		return result.toArray(new Block[] {});
	}
	
	private static Block[] getBarrels()
	{
		List<Block> result = Lists.newArrayList();
		ItemRegistry.getModBlocks().forEach((item) -> {
			if (item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof BlockBarrel) {
					result.add(block);
				}
			}
		});
		return result.toArray(new Block[] {});
	}
	
	private static Block[] getSigns()
	{
		List<Block> result = Lists.newArrayList();
		ItemRegistry.getModBlocks().forEach((item) -> {
			if (item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof BlockSign) {
					result.add(block);
				}
			}
		});
		return result.toArray(new Block[] {});
	}
}
