package ru.betterend.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.blocks.entities.ESignBlockEntity;
import ru.betterend.client.gui.BlockSignEditScreen;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin
{
	@Shadow
	private Minecraft client;

	@Shadow
	private ClientLevel world;

	@Inject(method = "onSignEditorOpen", at = @At(value = "HEAD"), cancellable = true)
	public void be_openSignEditor(ClientboundOpenSignEditorPacket packet, CallbackInfo info) {
		PacketUtils.ensureRunningOnSameThread(packet, ClientPacketListener.class.cast(this), client);
		BlockEntity blockEntity = world.getBlockEntity(packet.getPos());
		if (blockEntity instanceof ESignBlockEntity) {
			ESignBlockEntity sign = (ESignBlockEntity) blockEntity;
			client.setScreen(new BlockSignEditScreen(sign));
			info.cancel();
		}
	}
}