package ru.betterend.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.rituals.EternalRitual;

public class EternalPedestalEntity extends PedestalBlockEntity {
	private EternalRitual linkedRitual;
	
	public EternalPedestalEntity(BlockPos blockPos, BlockState blockState) {
		super(EndBlockEntities.ETERNAL_PEDESTAL, blockPos, blockState);
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
	public void setLevel(Level level) {
		super.setLevel(level);
		if (hasRitual()) {
			linkedRitual.setWorld(level);
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag tag) {
		if (hasRitual()) {
			tag.put("ritual", linkedRitual.toTag(new CompoundTag()));
		}
		super.saveAdditional(tag);
	}
	
	@Override
	protected void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		if (tag.contains("ritual")) {
			linkedRitual = new EternalRitual(level);
			linkedRitual.fromTag(tag.getCompound("ritual"));
		}
	}
}
