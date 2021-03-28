package ru.betterend.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.rituals.EternalRitual;

public class EternalPedestalEntity extends PedestalBlockEntity {
	private EternalRitual linkedRitual;
	
	public EternalPedestalEntity() {
		super(EndBlockEntities.ETERNAL_PEDESTAL);
	}
	
	public boolean hasRitual() {
		return linkedRitual != null;
	}
	
	public void linkRitual(EternalRitual ritual) {
		this.linkedRitual = ritual;
	}
	
	public EternalRitual getRitual() {
		return linkedRitual;
	}
	
	@Override
	public void setLocation(World world, BlockPos pos) {
		super.setLocation(world, pos);
		if (hasRitual()) {
			linkedRitual.setWorld(world);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		if (hasRitual()) {
			tag.put("ritual", linkedRitual.toTag(new CompoundTag()));
		}
		return super.toTag(tag);
	}

	@Override
	protected void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		if (tag.contains("ritual")) {
			linkedRitual = new EternalRitual(world);
			linkedRitual.fromTag(tag.getCompound("ritual"));
		}
	}
}
