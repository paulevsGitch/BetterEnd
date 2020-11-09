package ru.betterend.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.rituals.InfusionRitual;

public class InfusionPedestalEntity extends PedestalBlockEntity {

	private InfusionRitual linkedRitual;
	
	public InfusionPedestalEntity() {
		super(EndBlockEntities.INFUSION_PEDESTAL);
	}
	
	@Override
	public void setLocation(World world, BlockPos pos) {
		super.setLocation(world, pos);
		if (hasRitual()) {
			this.linkedRitual.setLocation(world, pos);
		}
	}
	
	public void linkRitual(InfusionRitual ritual) {
		this.linkedRitual = ritual;
	}
	
	public InfusionRitual getRitual() {
		return this.linkedRitual;
	}
	
	public boolean hasRitual() {
		return this.linkedRitual != null;
	}
	
	@Override
	public void tick() {
		if (hasRitual()) {
			this.linkedRitual.tick();
		}
		super.tick();
	}
	
	@Override
	public CompoundTag toInitialChunkDataTag() {
		return this.toTag(new CompoundTag());
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		if (tag.contains("ritual")) {
			this.linkedRitual = new InfusionRitual(world, pos);
			this.linkedRitual.fromTag(tag.getCompound("ritual"));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		if (hasRitual()) {
			tag.put("ritual", linkedRitual.toTag(new CompoundTag()));
		}
		return tag;
	}
}
