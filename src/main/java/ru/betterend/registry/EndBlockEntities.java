package ru.betterend.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.EndStoneSmelter;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.blocks.entities.*;

public class EndBlockEntities {
	public final static BlockEntityType<EndStoneSmelterBlockEntity> END_STONE_SMELTER = registerBlockEntity(EndStoneSmelter.ID,
			BlockEntityType.Builder.of(EndStoneSmelterBlockEntity::new, EndBlocks.END_STONE_SMELTER));
	public final static BlockEntityType<PedestalBlockEntity> PEDESTAL = registerBlockEntity("pedestal",
			BlockEntityType.Builder.of(PedestalBlockEntity::new, getPedestals()));
	public final static BlockEntityType<EternalPedestalEntity> ETERNAL_PEDESTAL = registerBlockEntity("eternal_pedestal",
			BlockEntityType.Builder.of(EternalPedestalEntity::new, EndBlocks.ETERNAL_PEDESTAL));
	public final static BlockEntityType<InfusionPedestalEntity> INFUSION_PEDESTAL = registerBlockEntity("infusion_pedestal",
			BlockEntityType.Builder.of(InfusionPedestalEntity::new, EndBlocks.INFUSION_PEDESTAL));
	public final static BlockEntityType<BlockEntityHydrothermalVent> HYDROTHERMAL_VENT = registerBlockEntity("hydrother_malvent",
			BlockEntityType.Builder.of(BlockEntityHydrothermalVent::new, EndBlocks.HYDROTHERMAL_VENT));

	public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, BlockEntityType.Builder<T> builder) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, BetterEnd.makeID(id), builder.build(null));
	}
	
	public static void register() {}
	
	static Block[] getPedestals() {
		return EndBlocks.getModBlocks().stream()
				.filter(block -> block instanceof PedestalBlock && !((PedestalBlock) block).hasUniqueEntity())
				.toArray(Block[]::new);
	}
}
