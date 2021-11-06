package ru.betterend.item.material;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

import java.util.function.Supplier;

public enum EndArmorMaterial implements ArmorMaterial {
	THALLASIUM("thallasium", 17, new int[] {1, 4, 5, 2}, 12, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
		return Ingredient.of(EndBlocks.THALLASIUM.ingot);
	}), TERMINITE("terminite", 26, new int[] {3, 6, 7, 3}, 14, SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0.05F, () -> {
		return Ingredient.of(EndBlocks.TERMINITE.ingot);
	}), AETERNIUM("aeternium", 40, new int[] {4, 7, 9, 4}, 18, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.5F, 0.2F, () -> {
		return Ingredient.of(EndItems.AETERNIUM_INGOT);
	}), CRYSTALITE("crystalite", 30, new int[] {3, 6, 8, 3}, 24, SoundEvents.ARMOR_EQUIP_DIAMOND, 1.2F, 0.1F, () -> {
		return Ingredient.of(EndBlocks.TERMINITE.ingot);
	});
	
	private static final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};
	private final String name;
	private final int durabilityMultiplier;
	private final int[] protectionAmounts;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockbackResistance;
	@SuppressWarnings("deprecation")
	private final LazyLoadedValue<Ingredient> repairIngredient;
	
	@SuppressWarnings("deprecation")
	private EndArmorMaterial(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionAmounts = protectionAmounts;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
	}
	
	@Override
	public int getDurabilityForSlot(EquipmentSlot slot) {
		return BASE_DURABILITY[slot.getIndex()] * this.durabilityMultiplier;
	}
	
	@Override
	public int getDefenseForSlot(EquipmentSlot slot) {
		return this.protectionAmounts[slot.getIndex()];
	}
	
	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}
	
	@Override
	public SoundEvent getEquipSound() {
		return this.equipSound;
	}
	
	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public float getToughness() {
		return this.toughness;
	}
	
	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
	
}
