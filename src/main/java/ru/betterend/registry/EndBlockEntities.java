package ru.betterend.registry;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.EndStoneSmelter;
import ru.betterend.blocks.basis.BlockBarrel;
import ru.betterend.blocks.basis.BlockChest;
import ru.betterend.blocks.basis.BlockSign;
import ru.betterend.blocks.entities.EBarrelBlockEntity;
import ru.betterend.blocks.entities.EChestBlockEntity;
import ru.betterend.blocks.entities.ESignBlockEntity;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;
import ru.betterend.blocks.entities.PedestalBlockEntity;

public class EndBlockEntities {
	public final static BlockEntityType<EndStoneSmelterBlockEntity> END_STONE_SMELTER = registerBlockEntity(EndStoneSmelter.ID,
			BlockEntityType.Builder.create(EndStoneSmelterBlockEntity::new, EndBlocks.END_STONE_SMELTER));
	public final static BlockEntityType<PedestalBlockEntity> ETERNAL_PEDESTAL = registerBlockEntity("eternal_pedestal",
			BlockEntityType.Builder.create(PedestalBlockEntity::new, EndBlocks.ETERNAL_PEDESTAL));
	public static final BlockEntityType<EChestBlockEntity> CHEST = registerBlockEntity("chest", 
			BlockEntityType.Builder.create(EChestBlockEntity::new, getChests()));
	public static final BlockEntityType<EBarrelBlockEntity> BARREL = registerBlockEntity("barrel",
			BlockEntityType.Builder.create(EBarrelBlockEntity::new, getBarrels()));
	public static final BlockEntityType<ESignBlockEntity> SIGN = registerBlockEntity("sign",
			BlockEntityType.Builder.create(ESignBlockEntity::new, getSigns()));

	public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, BlockEntityType.Builder<T> builder) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, BetterEnd.makeID(id), builder.build(null));
	}
	
	public static void register() {}

	static Block[] getChests() {
		List<Block> result = Lists.newArrayList();
		EndItems.getModBlocks().forEach((item) -> {
			if (item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof BlockChest) {
					result.add(block);
				}
			}
		});
		return result.toArray(new Block[] {});
	}
	
	static Block[] getBarrels() {
		List<Block> result = Lists.newArrayList();
		EndItems.getModBlocks().forEach((item) -> {
			if (item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof BlockBarrel) {
					result.add(block);
				}
			}
		});
		return result.toArray(new Block[] {});
	}
	
	static Block[] getSigns() {
		List<Block> result = Lists.newArrayList();
		EndItems.getModBlocks().forEach((item) -> {
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
