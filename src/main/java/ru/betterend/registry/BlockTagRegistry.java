package ru.betterend.registry;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.tag.Tag.Identified;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.util.TagHelper;

public class BlockTagRegistry {
	public static final Tag.Identified<Block> END_GROUND = makeTag("end_ground");
	
	private static Tag.Identified<Block> makeTag(String name) {
		return (Identified<Block>) TagRegistry.block(new Identifier(BetterEnd.MOD_ID, name));
	}
	
	public static void register() {
		TagHelper.addTag(END_GROUND, BlockRegistry.END_MOSS, BlockRegistry.END_MYCELIUM);
	}
}
