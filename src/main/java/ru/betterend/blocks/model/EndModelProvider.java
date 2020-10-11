package ru.betterend.blocks.model;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;

public class EndModelProvider implements ModelResourceProvider {

	private static final Map<Identifier, UnbakedModel> MODELS;
	
	public static final UnbakedModel FLAVOLITE_BLOCK = new BaseBlockModel("block/flavolite");
	public static final UnbakedModel FLAVOLITE_SLAB = new SlabModel("block/flavolite");
	public static final UnbakedModel FLAVOLITE_SLAB_TOP = new SlabTopModel("block/flavolite");
	
	public static void registerModel(String path, UnbakedModel model) {
		MODELS.put(BetterEnd.makeID(path), model);
	}
	
	@Override
	public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context) throws ModelProviderException {
		if (MODELS.containsKey(resourceId)) {
			return MODELS.get(resourceId);
		}
		return null;
	}
	
	static {
		MODELS = Maps.newHashMap();
		
		registerModel("item/flavolite", FLAVOLITE_BLOCK);
		registerModel("block/flavolite", FLAVOLITE_BLOCK);
		registerModel("item/flavolite_slab", FLAVOLITE_SLAB);
		registerModel("block/flavolite_slab", FLAVOLITE_SLAB);
		registerModel("block/flavolite_slab_top", FLAVOLITE_SLAB_TOP);
	}
}
