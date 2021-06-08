package ru.betterend.item.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderingRegistry.ModelProvider;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderingRegistry.TextureProvider;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ru.betterend.item.CrystaliteArmor;
import ru.betterend.registry.EndItems;

@Environment(EnvType.CLIENT)
public class CrystaliteArmorProvider implements ModelProvider, TextureProvider {
	private final static ResourceLocation FIRST_LAYER = new ResourceLocation("textures/models/armor/crystalite_layer_1.png");
	private final static ResourceLocation SECOND_LAYER = new ResourceLocation("textures/models/armor/crystalite_layer_2.png");
	private final static CrystaliteHelmetModel HELMET_MODEL = new CrystaliteHelmetModel(1.0F);
	private final static CrystaliteChestplateModel CHEST_MODEL = new CrystaliteChestplateModel(1.0F, false);
	private final static CrystaliteChestplateModel CHEST_MODEL_SLIM = new CrystaliteChestplateModel(1.0F, true);
	private final static CrystaliteLeggingsModel LEGGINGS_MODEL = new CrystaliteLeggingsModel(1.0F);
	private final static CrystaliteBootsModel BOOTS_MODEL = new CrystaliteBootsModel(1.0F);
	
	@Override
	public @NotNull ResourceLocation getArmorTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot,
			boolean secondLayer, @Nullable String suffix, ResourceLocation defaultTexture) {
		if (!isStackValid(stack)) return defaultTexture;
		if (secondLayer) return SECOND_LAYER;
		return FIRST_LAYER;
	}

	@Override
	public @NotNull HumanoidModel<LivingEntity> getArmorModel(LivingEntity entity, ItemStack stack,
			EquipmentSlot slot, HumanoidModel<LivingEntity> defaultModel) {
		if (!isStackValid(stack)) return defaultModel;
		switch(slot) {
			case HEAD: {
				return HELMET_MODEL;
			}
			case CHEST: {
				if (entity instanceof AbstractClientPlayer &&
					((AbstractClientPlayer) entity).getModelName().equals("slim")) {
					CHEST_MODEL_SLIM.copyPropertiesTo(defaultModel);
					return CHEST_MODEL_SLIM;
				}
				CHEST_MODEL.copyPropertiesTo(defaultModel);
				return CHEST_MODEL;
			}
			case LEGS: {
				return LEGGINGS_MODEL;
			}
			case FEET: {
				BOOTS_MODEL.copyPropertiesTo(defaultModel);
				return BOOTS_MODEL;
			}
			default: {
				return defaultModel;
			}
		}
	}
	
	public Iterable<Item> getRenderedItems() {
		return Lists.newArrayList(
				EndItems.CRYSTALITE_HELMET,
				EndItems.CRYSTALITE_CHESTPLATE,
				EndItems.CRYSTALITE_ELYTRA,
				EndItems.CRYSTALITE_LEGGINGS,
				EndItems.CRYSTALITE_BOOTS);
	}
	
	private boolean isStackValid(ItemStack stack) {
		return stack.getItem() instanceof CrystaliteArmor;
	}
}
