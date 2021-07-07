package ru.betterend.registry;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import ru.betterend.BetterEnd;
import ru.betterend.entity.model.*;
import ru.betterend.entity.render.*;
import ru.betterend.item.model.*;

public class EndEntitiesRenders {

	public static final ModelLayerLocation DRAGONFLY_MODEL = registerMain("dragonfly");
	public static final ModelLayerLocation END_SLIME_SHELL_MODEL = registerMain("endslime_shell");
	public static final ModelLayerLocation END_SLIME_MODEL = registerMain("endslime");
	public static final ModelLayerLocation END_FISH_MODEL = registerMain("endfish");
	public static final ModelLayerLocation CUBOZOA_MODEL = registerMain("cubozoa");
	public static final ModelLayerLocation SILK_MOTH_MODEL = registerMain("silkmoth");

	//Not sure if this should go to another registry
	public static final ModelLayerLocation ARMORED_ELYTRA = registerMain("armored_elytra");
	public static final ModelLayerLocation CRYSTALITE_CHESTPLATE = registerMain("crystalite_chestplate");
	public static final ModelLayerLocation CRYSTALITE_CHESTPLATE_THIN = registerMain("crystalite_chestplate_thin");
	public static final ModelLayerLocation CRYSTALITE_HELMET = registerMain("crystalite_helmet");
	public static final ModelLayerLocation CRYSTALITE_LEGGINGS = registerMain("crystalite_leggings");
	public static final ModelLayerLocation CRYSTALITE_BOOTS = registerMain("crystalite_boots");

	public static void register() {
		register(EndEntities.DRAGONFLY, RendererEntityDragonfly.class);
		register(EndEntities.END_SLIME, RendererEntityEndSlime.class);
		register(EndEntities.END_FISH, RendererEntityEndFish.class);
		register(EndEntities.SHADOW_WALKER, RendererEntityShadowWalker.class);
		register(EndEntities.CUBOZOA, RendererEntityCubozoa.class);
		register(EndEntities.SILK_MOTH, SilkMothEntityRenderer.class);

		EntityModelLayerRegistry.registerModelLayer(DRAGONFLY_MODEL, DragonflyEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(END_SLIME_SHELL_MODEL, EndSlimeEntityModel::getShellOnlyTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(END_SLIME_MODEL, EndSlimeEntityModel::getCompleteTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(END_FISH_MODEL, EndFishEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CUBOZOA_MODEL, CubozoaEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SILK_MOTH_MODEL, SilkMothEntityModel::getTexturedModelData);

		EntityModelLayerRegistry.registerModelLayer(ARMORED_ELYTRA, ArmoredElytraModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CRYSTALITE_CHESTPLATE, CrystaliteChestplateModel::getRegularTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CRYSTALITE_CHESTPLATE_THIN, CrystaliteChestplateModel::getThinTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CRYSTALITE_HELMET, CrystaliteHelmetModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CRYSTALITE_LEGGINGS, CrystaliteLeggingsModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CRYSTALITE_BOOTS, CrystaliteBootsModel::getTexturedModelData);
	}

	private static void register(EntityType<?> type, Class<? extends MobRenderer<?, ?>> renderer) {
		EntityRendererRegistry.INSTANCE.register(type, (context) -> {
			MobRenderer render = null;
			try {
				render = renderer.getConstructor(context.getClass()).newInstance(context);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return render;
		});
	}

	private static ModelLayerLocation registerMain(String id){
		return new ModelLayerLocation(new ResourceLocation(BetterEnd.MOD_ID, id), "main");
	}
}
