package ru.betterend.blocks.entities;

import java.util.List;

import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import ru.betterend.blocks.HydrothermalVentBlock;
import ru.betterend.item.ArmoredElytra;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndParticles;

public class BlockEntityHydrothermalVent extends BlockEntity implements TickableBlockEntity {

	private final static Vec3 POSITIVE_Y = new Vec3(0.0f, 1.0f, 0.0f);

	public BlockEntityHydrothermalVent() {
		super(EndBlockEntities.HYDROTHERMAL_VENT);
	}

	@Override
	public void tick() {
		if (level != null) {
			if (level.random.nextInt(20) == 0) {
				BlockState state = getBlockState();
				if (state.is(EndBlocks.HYDROTHERMAL_VENT) && state.getValue(HydrothermalVentBlock.ACTIVATED)) {
					double x = worldPosition.getX() + level.random.nextDouble();
					double y = worldPosition.getY() + 0.9 + level.random.nextDouble() * 0.3;
					double z = worldPosition.getZ() + level.random.nextDouble();
					if (state.getValue(HydrothermalVentBlock.WATERLOGGED)) {
						level.addParticle(EndParticles.GEYSER_PARTICLE, x, y, z, 0, 0, 0);
					} else {
						level.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
					}
				}
			}
			MutableBlockPos mutable = worldPosition.mutable().move(Direction.UP);
			AABB box = new AABB(mutable.offset(-1, 0, -1), mutable.offset(1, 25, 1));
			List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, box);
			if (entities.size() > 0) {
				while (mutable.getY() < box.maxY) {
					BlockState blockState = level.getBlockState(mutable);
					if (blockState.isSolidRender(level, mutable)) break;
					if (blockState.isAir()) {
						float force = (float) ((1.0 - (mutable.getY() / box.maxY)) / 5.0);
						entities.stream().filter(entity -> (int) entity.getY() == mutable.getY() &&
								hasElytra(entity) && entity.isFallFlying())
								.forEach(entity -> entity.moveRelative(force, POSITIVE_Y));
					}
					mutable.move(Direction.UP);
				}
			}
		}
	}

	private boolean hasElytra(LivingEntity entity) {
		Item item = entity.getItemBySlot(EquipmentSlot.CHEST).getItem();
		return item instanceof ElytraItem || item instanceof ArmoredElytra;
	}
}
