package ru.betterend.item.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

public class CrystaliteArmorModel extends BipedEntityModel<LivingEntity> {

	public ModelPart helmet;
	public ModelPart chestplate;
	public ModelPart leggings;
	public ModelPart boots;
	
	public CrystaliteArmorModel() {
		super(1.0F);
	}
}
