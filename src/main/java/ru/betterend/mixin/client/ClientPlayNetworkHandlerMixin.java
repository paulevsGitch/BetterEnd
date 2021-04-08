package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.SignEditorOpenS2CPacket;
import ru.betterend.blocks.entities.ESignBlockEntity;
import ru.betterend.client.gui.BlockSignEditScreen;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Shadow
	private MinecraftClient client;

	@Shadow
	private ClientLevel world;

	@Inject(method = "onSignEditorOpen", at = @At(value = "HEAD"), cancellable = true)
	public void be_openSignEditor(SignEditorOpenS2CPacket packet, CallbackInfo info) {
		NetworkThreadUtils.forceMainThread(packet, ClientPlayNetworkHandler.class.cast(this), client);
		BlockEntity blockEntity = world.getBlockEntity(packet.getPos());
		if (blockEntity instanceof ESignBlockEntity) {
			ESignBlockEntity sign = (ESignBlockEntity) blockEntity;
			client.openScreen(new BlockSignEditScreen(sign));
			info.cancel();
		}
	}
}