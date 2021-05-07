package ru.betterend.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import ru.betterend.BetterEnd;
import ru.betterend.interfaces.MultiModelItem;
import ru.betterend.registry.EndItems;

public class ArmoredElytra extends ArmorItem implements MultiModelItem {

	private final ResourceLocation wingTexture;
	private final Item repairItem;
	private final double movementFactor;

	public ArmoredElytra(String name, ArmorMaterial material, Item repairItem, int durability, double movementFactor, boolean fireproof) {
		super(material, EquipmentSlot.CHEST, fireproof ?
				EndItems.makeItemSettings().durability(durability).rarity(Rarity.RARE).fireResistant() :
				EndItems.makeItemSettings().durability(durability).rarity(Rarity.RARE));
		this.wingTexture = BetterEnd.makeID("textures/entity/" + name + ".png");
		this.repairItem = repairItem;
		this.movementFactor = movementFactor;
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
		return (int) ((double) super.getDefense() / 1.25);
	}

	@Override
	public float getToughness() {
		return super.getToughness() / 1.25F;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void registerModelPredicate() {
		FabricModelPredicateProviderRegistry.register(this, new ResourceLocation("broken"),
				(itemStack, clientLevel, livingEntity) -> ElytraItem.isFlyEnabled(itemStack) ? 0.0F : 1.0F);
	}
}
