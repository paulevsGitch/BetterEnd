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
import ru.betterend.blocks.EternalPedestal;
import ru.betterend.blocks.InfusionPedestal;
import ru.betterend.blocks.basis.EndBarrelBlock;
import ru.betterend.blocks.basis.EndChestBlock;
import ru.betterend.blocks.basis.EndFurnaceBlock;
import ru.betterend.blocks.basis.EndSignBlock;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.blocks.entities.BlockEntityHydrothermalVent;
import ru.betterend.blocks.entities.EBarrelBlockEntity;
import ru.betterend.blocks.entities.EChestBlockEntity;
import ru.betterend.blocks.entities.EFurnaceBlockEntity;
import ru.betterend.blocks.entities.ESignBlockEntity;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;
import ru.betterend.blocks.entities.EternalPedestalEntity;
import ru.betterend.blocks.entities.InfusionPedestalEntity;
import ru.betterend.blocks.entities.PedestalBlockEntity;

public class EndBlockEntities {
	public final static BlockEntityType<EndStoneSmelterBlockEntity> END_STONE_SMELTER = registerBlockEntity(EndStoneSmelter.ID,
			BlockEntityType.Builder.create(EndStoneSmelterBlockEntity::new, EndBlocks.END_STONE_SMELTER));
	public final static BlockEntityType<PedestalBlockEntity> PEDESTAL = registerBlockEntity("pedestal",
			BlockEntityType.Builder.create(PedestalBlockEntity::new, getPedestals()));
	public final static BlockEntityType<EternalPedestalEntity> ETERNAL_PEDESTAL = registerBlockEntity("eternal_pedestal",
			BlockEntityType.Builder.create(EternalPedestalEntity::new, EndBlocks.ETERNAL_PEDESTAL));
	public final static BlockEntityType<InfusionPedestalEntity> INFUSION_PEDESTAL = registerBlockEntity("infusion_pedestal",
			BlockEntityType.Builder.create(InfusionPedestalEntity::new, EndBlocks.INFUSION_PEDESTAL));
	public static final BlockEntityType<EChestBlockEntity> CHEST = registerBlockEntity("chest", 
			BlockEntityType.Builder.create(EChestBlockEntity::new, getChests()));
	public static final BlockEntityType<EBarrelBlockEntity> BARREL = registerBlockEntity("barrel",
			BlockEntityType.Builder.create(EBarrelBlockEntity::new, getBarrels()));
	public static final BlockEntityType<ESignBlockEntity> SIGN = registerBlockEntity("sign",
			BlockEntityType.Builder.create(ESignBlockEntity::new, getSigns()));
	public final static BlockEntityType<BlockEntityHydrothermalVent> HYDROTHERMAL_VENT = registerBlockEntity("hydrother_malvent",
			BlockEntityType.Builder.create(BlockEntityHydrothermalVent::new, EndBlocks.HYDROTHERMAL_VENT));
	public static final BlockEntityType<EFurnaceBlockEntity> FURNACE = registerBlockEntity("furnace",
			BlockEntityType.Builder.create(EFurnaceBlockEntity::new, getFurnaces()));

	public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, BlockEntityType.Builder<T> builder) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, BetterEnd.makeID(id), builder.build(null));
	}
	
	public static void register() {}

	static Block[] getChests() {
		List<Block> result = Lists.newArrayList();
		EndItems.getModBlocks().forEach((item) -> {
			if (item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof EndChestBlock) {
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
				if (block instanceof EndBarrelBlock) {
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
				if (block instanceof EndSignBlock) {
					result.add(block);
				}
			}
		});
		return result.toArray(new Block[] {});
	}
	
	static Block[] getPedestals() {
		List<Block> result = Lists.newArrayList();
		EndItems.getModBlocks().forEach((item) -> {
			if (item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof EternalPedestal ||
					block instanceof InfusionPedestal) return;
				if (block instanceof PedestalBlock) {
					result.add(block);
				}
			}
		});
		return result.toArray(new Block[] {});
	}
	
	static Block[] getFurnaces() {
		List<Block> result = Lists.newArrayList();
		EndItems.getModBlocks().forEach((item) -> {
			if (item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof EndFurnaceBlock) {
					result.add(block);
				}
			}
		});
		return result.toArray(new Block[] {});
	}
}
