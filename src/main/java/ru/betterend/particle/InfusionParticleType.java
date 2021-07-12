package ru.betterend.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import ru.bclib.util.ColorUtil;
import ru.betterend.registry.EndParticles;

public class InfusionParticleType extends ParticleType<InfusionParticleType> implements ParticleOptions {
	public static final Codec<InfusionParticleType> CODEC = ItemStack.CODEC.xmap(itemStack -> new InfusionParticleType(EndParticles.INFUSION, itemStack), infusionParticleType -> infusionParticleType.itemStack);
	
	public static final ParticleOptions.Deserializer<InfusionParticleType> PARAMETERS_FACTORY = new ParticleOptions.Deserializer<InfusionParticleType>() {
		public InfusionParticleType fromCommand(ParticleType<InfusionParticleType> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			ItemParser itemStringReader = new ItemParser(stringReader, false).parse();
			ItemStack itemStack = new ItemInput(itemStringReader.getItem(), itemStringReader.getNbt()).createItemStack(1, false);
			return new InfusionParticleType(particleType, itemStack);
		}
		
		public InfusionParticleType fromNetwork(ParticleType<InfusionParticleType> particleType, FriendlyByteBuf packetByteBuf) {
			return new InfusionParticleType(particleType, packetByteBuf.readItem());
		}
	};
	
	private final ParticleType<InfusionParticleType> type;
	private final ItemStack itemStack;
	
	public InfusionParticleType(ParticleType<InfusionParticleType> particleType, ItemStack stack) {
		super(true, PARAMETERS_FACTORY);
		this.type = particleType;
		this.itemStack = stack;
	}
	
	public InfusionParticleType(ItemStack stack) {
		this(EndParticles.INFUSION, stack);
	}
	
	@Environment(EnvType.CLIENT)
	public float[] getPalette() {
		int color = ColorUtil.extractColor(itemStack.getItem());
		return ColorUtil.toFloatArray(color);
	}
	
	@Override
	public ParticleType<?> getType() {
		return this.type;
	}
	
	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeItem(itemStack);
	}
	
	@Override
	public String writeToString() {
		return Registry.PARTICLE_TYPE.getKey(this).toString();
	}
	
	@Override
	public Codec<InfusionParticleType> codec() {
		return CODEC;
	}
}
