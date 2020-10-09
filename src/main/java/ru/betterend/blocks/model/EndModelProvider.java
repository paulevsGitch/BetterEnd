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
	
	public static final UnbakedModel BASE_BLOCK_MODEL = new BaseBlockModel("block/flavolite");
	
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
		
		registerModel("block/flavolite", BASE_BLOCK_MODEL);
		registerModel("item/flavolite", BASE_BLOCK_MODEL);
	}
}
