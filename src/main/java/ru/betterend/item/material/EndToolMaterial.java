package ru.betterend.item.material;

import java.util.function.Supplier;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;
import ru.betterend.registry.EndItems;

public enum EndToolMaterial implements ToolMaterial {
	THALLASIUM(2, 320, 6.5F, 4.5F, 13, () -> {
		return Ingredient.ofItems(EndItems.AETERNIUM_INGOT);
	}),
	TERMINITE(3, 1230, 8.5F, 3.0F, 14, () -> {
		return Ingredient.ofItems(EndItems.TERMINITE_INGOT);
	}),
	AETERNIUM(5, 2196, 10.0F, 4.5F, 18, () -> {
		return Ingredient.ofItems(EndItems.AETERNIUM_INGOT);
	});

	private final int durability;
	private final float miningSpeed;
	private final float attackDamage;
	private final int miningLevel;
	private final int enchantability;
	private final Lazy<Ingredient> repairIngredient;
	
	private EndToolMaterial(int miningLevel, int durability, float miningSpeed, float attackDamage, int enchantability,
			Supplier<Ingredient> repairIngredient) {

		this.durability = durability;
		this.miningSpeed = miningSpeed;
		this.attackDamage = attackDamage;
		this.miningLevel = miningLevel;
		this.enchantability = enchantability;
		this.repairIngredient = new Lazy<>(repairIngredient);
	}

	@Override
	public int getDurability() {
		return this.durability;
	}

	@Override
	public float getMiningSpeedMultiplier() {
		return this.miningSpeed;
	}

	@Override
	public float getAttackDamage() {
		return this.attackDamage;
	}

	@Override
	public int getMiningLevel() {
		return this.miningLevel;
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

}
