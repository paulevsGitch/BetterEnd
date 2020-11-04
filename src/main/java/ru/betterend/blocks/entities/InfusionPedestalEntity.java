package ru.betterend.blocks.entities;

import ru.betterend.rituals.InfusionRitual;

public class InfusionPedestalEntity extends PedestalBlockEntity {

	private InfusionRitual activeRitual;
	
	public boolean hasRitual() {
		return this.activeRitual != null;
	}

}
