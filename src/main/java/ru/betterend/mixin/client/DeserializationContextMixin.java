package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.render.model.json.ModelVariantMap.DeserializationContext;
import net.minecraft.util.Identifier;
import ru.betterend.interfaces.IdentifiedContext;

@Mixin(DeserializationContext.class)
public class DeserializationContextMixin implements IdentifiedContext {

	private Identifier contextId;
	
	@Override
	public Identifier getContextId() {
		return this.contextId;
	}

	@Override
	public void setContextId(Identifier id) {
		this.contextId = id;
	}
}
