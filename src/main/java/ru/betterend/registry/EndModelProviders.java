package ru.betterend.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.betterend.item.model.CrystaliteArmorProvider;
import shadow.fabric.api.client.rendering.v1.ArmorRenderingRegistry;

@Environment(EnvType.CLIENT)
public class EndModelProviders {

	public final static CrystaliteArmorProvider CRYSTALITE_PROVIDER = new CrystaliteArmorProvider();
	
	public final static void register() {
		ArmorRenderingRegistry.registerModel(CRYSTALITE_PROVIDER, CRYSTALITE_PROVIDER.getRenderedItems());
		ArmorRenderingRegistry.registerTexture(CRYSTALITE_PROVIDER, CRYSTALITE_PROVIDER.getRenderedItems());
	}
}
