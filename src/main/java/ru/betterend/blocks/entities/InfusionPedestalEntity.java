package ru.betterend.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.rituals.InfusionRitual;

public class InfusionPedestalEntity extends PedestalBlockEntity {

	private InfusionRitual linkedRitual;
	
	public InfusionPedestalEntity(BlockPos blockPos, BlockState blockState) {
		super(EndBlockEntities.INFUSION_PEDESTAL, blockPos, blockState);
	}

	@Override
	public void setLevel(Level world){
		super.setLevel(world);
		if (hasRitual()) {
			linkedRitual.setLocation(world, this.getBlockPos());
		} else {
			linkRitual(new InfusionRitual(this, world, this.getBlockPos()));
		}
	}
	
	public void setLevelAndPosition(Level world, BlockPos pos) {
		if (hasRitual()) {
			linkedRitual.setLocation(world, pos);
		} else {
			linkRitual(new InfusionRitual(this, world, pos));
		}
	}
	
	public void linkRitual(InfusionRitual ritual) {
		linkedRitual = ritual;
	}
	
	public InfusionRitual getRitual() {
		return linkedRitual;
	}
	
	public boolean hasRitual() {
		return linkedRitual != null;
	}

	public static void tick(Level tickLevel, BlockPos tickPos, BlockState tickState, InfusionPedestalEntity blockEntity) {
		if (blockEntity.hasRitual()) {
			blockEntity.linkedRitual.tick();
		}
		PedestalBlockEntity.tick(tickLevel, tickPos, tickState, blockEntity);
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		if (hasRitual()) {
			tag.put("ritual", linkedRitual.toTag(new CompoundTag()));
		}
		return super.save(tag);
	}

	@Override
	protected void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		if (tag.contains("ritual")) {
			linkedRitual = new InfusionRitual(this, level, worldPosition);
			linkedRitual.fromTag(tag.getCompound("ritual"));
		}
	}
}
