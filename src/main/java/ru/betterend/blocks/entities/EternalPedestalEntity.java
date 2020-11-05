package ru.betterend.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.rituals.EternalRitual;

public class EternalPedestalEntity extends PedestalBlockEntity {
	private EternalRitual linkedRitual;
	
	public boolean hasRitual() {
		return this.linkedRitual != null;
	}
	
	public void linkRitual(EternalRitual ritual) {
		this.linkedRitual = ritual;
	}
	
	public EternalRitual getRitual() {
		return this.linkedRitual;
	}
	
	@Override
	public void setLocation(World world, BlockPos pos) {
		super.setLocation(world, pos);
		if (hasRitual()) {
			this.linkedRitual.setWorld(world);
		}
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		if (tag.contains("ritual")) {
			this.linkedRitual = new EternalRitual(world);
			this.linkedRitual.fromTag(tag.getCompound("ritual"));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		if (this.hasRitual()) {
			tag.put("ritual", linkedRitual.toTag(new CompoundTag()));
		}
		return super.toTag(tag);
	}
}
