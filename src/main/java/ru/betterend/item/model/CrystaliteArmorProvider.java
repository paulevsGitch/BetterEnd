package ru.betterend.item.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderingRegistry.ModelProvider;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderingRegistry.TextureProvider;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import ru.betterend.BetterEnd;
import ru.betterend.registry.EndItems;

@Environment(EnvType.CLIENT)
public class CrystaliteArmorProvider implements ModelProvider, TextureProvider {
	private final static Identifier FIRST_LAYER = BetterEnd.makeID("textures/model/armor/crystalite_layer_1.png");
	private final static Identifier SECOND_LAYER = BetterEnd.makeID("textures/model/armor/crystalite_layer_2.png");
	private final static CrystaliteHelmetModel HELMET_MODEL = new CrystaliteHelmetModel(1.0F);
	private final static CrystaliteChestplateModel CHEST_MODEL = new CrystaliteChestplateModel(1.0F, false);
	private final static CrystaliteChestplateModel CHEST_MODEL_SLIM = new CrystaliteChestplateModel(1.0F, true);
	private final static CrystaliteLeggingsModel LEGGINGS_MODEL = new CrystaliteLeggingsModel(1.0F);
	private final static CrystaliteBootsModel BOOTS_MODEL = new CrystaliteBootsModel(1.0F);
	
	@Override
	public @NotNull Identifier getArmorTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot,
			boolean secondLayer, @Nullable String suffix, Identifier defaultTexture) {
		if (!isStackValid(stack)) return defaultTexture;
		if (secondLayer) return SECOND_LAYER;
		return FIRST_LAYER;
	}

	@Override
	public @NotNull BipedEntityModel<LivingEntity> getArmorModel(LivingEntity entity, ItemStack stack,
			EquipmentSlot slot, BipedEntityModel<LivingEntity> defaultModel) {
		if (!isStackValid(stack)) return defaultModel;
		switch(slot) {
			case HEAD: {
				return HELMET_MODEL;
			}
			case CHEST: {
				
				if (entity instanceof AbstractClientPlayerEntity &&
					((AbstractClientPlayerEntity) entity).getModel().equals("slim")) {
					return CHEST_MODEL_SLIM;
				}
				return CHEST_MODEL;
			}
			case LEGS: {
				return LEGGINGS_MODEL;
			}
			case FEET: {
				return BOOTS_MODEL;
			}
			default: {
				return defaultModel;
			}
		}
	}
	
	public Iterable<Item> getRenderedItems() {
		return Lists.newArrayList(EndItems.CRYSTALITE_HELMET, EndItems.CRYSTALITE_CHESTPLATE, EndItems.CRYSTALITE_LEGGINGS, EndItems.CRYSTALITE_BOOTS);
	}
	
	private boolean isStackValid(ItemStack stack) {
		return stack.getItem() == EndItems.CRYSTALITE_HELMET ||
			   stack.getItem() == EndItems.CRYSTALITE_CHESTPLATE ||
			   stack.getItem() == EndItems.CRYSTALITE_LEGGINGS ||
			   stack.getItem() == EndItems.CRYSTALITE_BOOTS;
	}
}
