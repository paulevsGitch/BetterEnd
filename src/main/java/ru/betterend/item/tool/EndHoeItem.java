package ru.betterend.item.tool;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;
import ru.betterend.client.models.ItemModelProvider;
import ru.betterend.client.models.ModelsHelper;

public class EndHoeItem extends HoeItem implements ItemModelProvider {
	public EndHoeItem(Tier material, int attackDamage, float attackSpeed, Properties settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return ModelsHelper.createHandheldItem(resourceLocation.getPath());
	}
}
