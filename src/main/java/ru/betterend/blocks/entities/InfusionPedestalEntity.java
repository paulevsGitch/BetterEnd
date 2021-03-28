package ru.betterend.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
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
			linkedRitual.setLocation(world, pos);
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
	
	@Override
	public void tick() {
		if (hasRitual()) {
			linkedRitual.tick();
		}
		super.tick();
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
			linkedRitual = new InfusionRitual(world, pos);
			linkedRitual.fromTag(tag.getCompound("ritual"));
		}
	}
}
