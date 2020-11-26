package ru.betterend.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStringReader;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;
import ru.betterend.registry.EndParticles;
import ru.betterend.util.ColorUtil;

public class InfusionParticleType extends ParticleType<InfusionParticleType> implements ParticleEffect {
	public static final Codec<InfusionParticleType> CODEC = ItemStack.CODEC.xmap(itemStack -> {
		return new InfusionParticleType(EndParticles.INFUSION, itemStack);
	}, infusionParticleType -> {
		return infusionParticleType.itemStack;
	});
	public static final ParticleEffect.Factory<InfusionParticleType> PARAMETERS_FACTORY = new ParticleEffect.Factory<InfusionParticleType>() {
		public InfusionParticleType read(ParticleType<InfusionParticleType> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			ItemStringReader itemStringReader = new ItemStringReader(stringReader, false).consume();
			ItemStack itemStack = new ItemStackArgument(itemStringReader.getItem(), itemStringReader.getTag()).createStack(1, false);
			return new InfusionParticleType(particleType, itemStack);
		}

		public InfusionParticleType read(ParticleType<InfusionParticleType> particleType, PacketByteBuf packetByteBuf) {
			return new InfusionParticleType(particleType, packetByteBuf.readItemStack());
		}
	};
	
	private ParticleType<InfusionParticleType> type;
	private ItemStack itemStack;
	
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
	public void write(PacketByteBuf buffer) {
		buffer.writeItemStack(itemStack);
	}

	@Override
	public String asString() {
		return Registry.PARTICLE_TYPE.getId(this).toString();
	}

	@Override
	public Codec<InfusionParticleType> getCodec() {
		return CODEC;
	}
}
