package ru.betterend.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import ru.betterend.BetterEnd;
import ru.betterend.interfaces.FallFlyingItem;
import ru.betterend.interfaces.MultiModelItem;
import ru.betterend.registry.EndItems;

public class CrystaliteElytra extends CrystaliteArmor implements MultiModelItem, FallFlyingItem {
	
	private final ResourceLocation wingTexture;
	private final double movementFactor;
	private final int defense;
	private final float toughness;
	
	public CrystaliteElytra(int durability, double movementFactor) {
		super(EquipmentSlot.CHEST, EndItems.makeEndItemSettings().durability(durability).rarity(Rarity.EPIC));
		this.wingTexture = BetterEnd.makeID("textures/entity/elytra_crystalite.png");
		this.movementFactor = movementFactor;
		this.defense = (int) ((double) material.getDefenseForSlot(EquipmentSlot.CHEST) / 1.75);
		this.toughness = material.getToughness() / 1.75F;
		addAttributeModifier(
			Attributes.ARMOR,
			new AttributeModifier(ARMOR_MODIFIER_UUID_PER_SLOT[2],
				"Armor modifier",
				defense,
				AttributeModifier.Operation.ADDITION
			)
		);
		addAttributeModifier(
			Attributes.ARMOR_TOUGHNESS,
			new AttributeModifier(ARMOR_MODIFIER_UUID_PER_SLOT[2],
				"Armor toughness",
				toughness,
				AttributeModifier.Operation.ADDITION
			)
		);
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack itemStack, ItemStack itemStack2) {
		return super.isValidRepairItem(itemStack, itemStack2) || itemStack2.getItem() == EndItems.ENCHANTED_MEMBRANE;
	}
	
	@Override
	public double getMovementFactor() {
		return movementFactor;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ResourceLocation getModelTexture() {
		return wingTexture;
	}
	
	@Override
	public int getDefense() {
		return defense;
	}
	
	@Override
	public float getToughness() {
		return toughness;
	}
	
	@Override
	public void registerModelPredicate() {
		FabricModelPredicateProviderRegistry.register(
			this,
			new ResourceLocation("broken"),
			(itemStack, clientLevel, livingEntity, i) -> ElytraItem.isFlyEnabled(itemStack) ? 0.0F : 1.0F
		);
	}
}
