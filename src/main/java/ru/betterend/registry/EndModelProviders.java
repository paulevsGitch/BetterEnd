package ru.betterend.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.betterend.item.model.CrystaliteArmorProvider;

@Environment(EnvType.CLIENT)
public class EndModelProviders {

	public final static CrystaliteArmorProvider CRYSTALITE_PROVIDER = new CrystaliteArmorProvider();
	
	public final static void register() {
		//TODO: Needs Fix in 1.17
		throw new RuntimeException("Needs Fix for 1.17");
		//ArmorRenderingRegistry.registerModel(CRYSTALITE_PROVIDER, CRYSTALITE_PROVIDER.getRenderedItems());
		//ArmorRenderingRegistry.registerTexture(CRYSTALITE_PROVIDER, CRYSTALITE_PROVIDER.getRenderedItems());
	}
}
