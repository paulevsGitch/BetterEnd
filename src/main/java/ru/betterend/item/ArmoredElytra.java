package ru.betterend.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import ru.betterend.BetterEnd;
import ru.betterend.interfaces.BreakableItem;
import ru.betterend.patterns.ModelProvider;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndItems;

import java.util.UUID;

public class ArmoredElytra extends EndArmorItem implements MultiModelItem {

	private static final UUID ARMOR_MODIFIER_UUID_CHEST = ARMOR_MODIFIER_UUID_PER_SLOT[2];

	private final ResourceLocation wingTexture;
	private final Item repairItem;
	private final double movementFactor;
	private final float toughness;
	private final int defense;

	public ArmoredElytra(String name, ArmorMaterial material, Item repairItem, int durability, double movementFactor, boolean fireproof) {
		super(material, EquipmentSlot.CHEST, fireproof ?
				EndItems.makeItemSettings().durability(durability).rarity(Rarity.EPIC).fireResistant() :
				EndItems.makeItemSettings().durability(durability).rarity(Rarity.EPIC));
		this.wingTexture = BetterEnd.makeID("textures/entity/" + name + ".png");
		this.repairItem = repairItem;
		this.movementFactor = movementFactor;
		this.defense = (int) ((double) material.getDefenseForSlot(EquipmentSlot.CHEST) / 1.75);
		this.toughness = material.getToughness() / 1.75F;
		addAttributeModifier(Attributes.ARMOR, new AttributeModifier(ARMOR_MODIFIER_UUID_CHEST,
				"Armor modifier", defense, AttributeModifier.Operation.ADDITION));
		addAttributeModifier(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ARMOR_MODIFIER_UUID_CHEST,
				"Armor toughness", toughness, AttributeModifier.Operation.ADDITION));
	}

	public double getMovementFactor() {
		return movementFactor;
	}

	@Environment(EnvType.CLIENT)
	public ResourceLocation getWingTexture() {
		return wingTexture;
	}

	@Override
	public boolean isValidRepairItem(ItemStack itemStack, ItemStack itemStack2) {
		return super.isValidRepairItem(itemStack, itemStack2) || itemStack2.getItem() == repairItem;
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
	@Environment(EnvType.CLIENT)
	public void registerModelPredicate() {
		FabricModelPredicateProviderRegistry.register(this, new ResourceLocation("broken"),
				(itemStack, clientLevel, livingEntity) -> ElytraItem.isFlyEnabled(itemStack) ? 0.0F : 1.0F);
	}
}
