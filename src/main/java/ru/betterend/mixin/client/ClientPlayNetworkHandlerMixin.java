package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import ru.betterend.blocks.entities.ESignBlockEntity;
import ru.betterend.client.gui.BlockSignEditScreen;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin
{
	@Shadow
	private Minecraft minecraft;

	@Shadow
	private ClientLevel level;

	@Inject(method = "handleOpenSignEditor", at = @At(value = "HEAD"), cancellable = true)
	public void be_openSignEditor(ClientboundOpenSignEditorPacket packet, CallbackInfo info) {
		PacketUtils.ensureRunningOnSameThread(packet, ClientPacketListener.class.cast(this), minecraft);
		BlockEntity blockEntity = level.getBlockEntity(packet.getPos());
		if (blockEntity instanceof ESignBlockEntity) {
			ESignBlockEntity sign = (ESignBlockEntity) blockEntity;
			minecraft.setScreen(new BlockSignEditScreen(sign));
			info.cancel();
		}
	}
}