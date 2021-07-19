package ru.betterend.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import ru.betterend.blocks.HydrothermalVentBlock;
import ru.betterend.interfaces.FallFlyingItem;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndParticles;

import java.util.List;

public class BlockEntityHydrothermalVent extends BlockEntity {
	private final static Vec3 POSITIVE_Y = new Vec3(0.0f, 1.0f, 0.0f);
	private static final MutableBlockPos POS = new MutableBlockPos();
	
	public BlockEntityHydrothermalVent(BlockPos blockPos, BlockState blockState) {
		super(EndBlockEntities.HYDROTHERMAL_VENT, blockPos, blockState);
	}
	
	public static <T extends BlockEntity> void tick(Level level, BlockPos worldPosition, BlockState state, T uncastedEntity) {
		if (level != null && uncastedEntity instanceof BlockEntityHydrothermalVent && state.is(EndBlocks.HYDROTHERMAL_VENT)) {
			BlockEntityHydrothermalVent blockEntity = (BlockEntityHydrothermalVent) uncastedEntity;
			if (level.isClientSide()) {
				clientTick(level, worldPosition, state, blockEntity);
			}
			//else {
			serverTick(level, worldPosition, state, blockEntity);
			//}
		}
	}
	
	private static void clientTick(Level level, BlockPos worldPosition, BlockState state, BlockEntityHydrothermalVent blockEntity) {
		boolean active = state.getValue(HydrothermalVentBlock.ACTIVATED);
		if (active && level.random.nextInt(20) == 0 && state.getValue(HydrothermalVentBlock.WATERLOGGED)) {
			double x = worldPosition.getX() + level.random.nextDouble();
			double y = worldPosition.getY() + 0.9 + level.random.nextDouble() * 0.3;
			double z = worldPosition.getZ() + level.random.nextDouble();
			level.addParticle(EndParticles.GEYSER_PARTICLE, x, y, z, 0, 0, 0);
		}
	}
	
	private static void serverTick(Level level, BlockPos worldPosition, BlockState state, BlockEntityHydrothermalVent blockEntity) {
		boolean active = state.getValue(HydrothermalVentBlock.ACTIVATED);
		POS.set(worldPosition).move(Direction.UP);
		int height = active ? 85 : 25;
		AABB box = new AABB(POS.offset(-1, 0, -1), POS.offset(1, height, 1));
		List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, box);
		if (entities.size() > 0) {
			while (POS.getY() < box.maxY) {
				BlockState blockState = level.getBlockState(POS);
				if (blockState.isSolidRender(level, POS)) break;
				if (blockState.isAir()) {
					double mult = active ? 3.0 : 5.0;
					float force = (float) ((1.0 - (POS.getY() / box.maxY)) / mult);
					entities.stream()
							.filter(entity -> (int) entity.getY() == POS.getY() && blockEntity.hasElytra(entity) && entity
								.isFallFlying())
							.forEach(entity -> entity.moveRelative(force, POSITIVE_Y));
				}
				POS.move(Direction.UP);
			}
		}
	}
	
	private boolean hasElytra(LivingEntity entity) {
		Item item = entity.getItemBySlot(EquipmentSlot.CHEST).getItem();
		return item instanceof ElytraItem || item instanceof FallFlyingItem;
	}
}
