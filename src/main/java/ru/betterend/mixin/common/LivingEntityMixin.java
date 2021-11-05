package ru.betterend.mixin.common;

import java.util.Collection;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.BetterEnd;
import ru.betterend.interfaces.FallFlyingItem;
import ru.betterend.interfaces.MobEffectApplier;
import ru.betterend.item.CrystaliteArmor;
import ru.betterend.registry.EndAttributes;

@Mixin(value=LivingEntity.class, priority=200)
public abstract class LivingEntityMixin extends Entity {
	
	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	
	@Shadow
	protected int fallFlyTicks;
	
	@Shadow
	public abstract boolean hasEffect(MobEffect mobEffect);
	
	@Shadow
	public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);
	
	@Shadow
	public abstract void calculateEntityAnimation(LivingEntity livingEntity, boolean b);
	
	@Shadow
	protected abstract SoundEvent getFallDamageSound(int i);
	
	@Shadow
	public abstract boolean isFallFlying();
	
	@Shadow
	public abstract AttributeMap getAttributes();
	
	private Entity lastAttacker;
	
	@Inject(method = "createLivingAttributes", at = @At("RETURN"), cancellable = true)
	private static void be_addLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> info) {
		EndAttributes.addLivingEntityAttributes(info.getReturnValue());
	}
	
	@Inject(method = "tickEffects", at = @At("HEAD"))
	protected void be_applyEffects(CallbackInfo info) {
		if (!level.isClientSide()) {
			LivingEntity owner = LivingEntity.class.cast(this);
			if (CrystaliteArmor.hasFullSet(owner)) {
				CrystaliteArmor.applySetEffect(owner);
			}
			getArmorSlots().forEach(itemStack -> {
				if (itemStack.getItem() instanceof MobEffectApplier) {
					((MobEffectApplier) itemStack.getItem()).applyEffect(owner);
				}
			});
		}
	}
	
	@Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
	public void be_canBeAffected(MobEffectInstance mobEffectInstance, CallbackInfoReturnable<Boolean> info) {
		try {
			if (mobEffectInstance.getEffect() == MobEffects.BLINDNESS && getAttributes().getValue(EndAttributes.BLINDNESS_RESISTANCE) > 0.0) {
				info.setReturnValue(false);
			}
		}
		catch (Exception ex) {
			BetterEnd.LOGGER.warning("Blindness resistance attribute haven't been registered.");
		}
	}
	
	@Inject(method = "hurt", at = @At("HEAD"))
	public void be_hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		this.lastAttacker = source.getEntity();
	}
	
	@ModifyArg(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"), index = 0)
	private double be_increaseKnockback(double value, double x, double z) {
		if (lastAttacker != null && lastAttacker instanceof LivingEntity) {
			LivingEntity attacker = (LivingEntity) lastAttacker;
			value += this.be_getKnockback(attacker.getMainHandItem().getItem());
		}
		return value;
	}

	// FlyFallingLib (part of Origin) redirected the call to updateFallFlying,
	// so we inject our code before the actual call and cancel the execution if the player is still
	// flying. That means we have to replicate all vanilla code that happens after the call to
	// updateFallFlying. We do this in vanillaAfterUpdateFallFlying
	@Inject(method="aiStep", cancellable = true, at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;updateFallFlying()V"))
	private void be_updateFallFlying_originFix(CallbackInfo info) {
		//run be_updateFallFlying instead
		if (!BetterEnd.RUNS_FALL_FLYING_LIB) return;

		ItemStack itemStack = getItemBySlot(EquipmentSlot.CHEST);
		if (!level.isClientSide && itemStack.getItem() instanceof FallFlyingItem) {
			boolean isFlying = getSharedFlag(7);
			if (isFlying && !onGround && !isPassenger() && !hasEffect(MobEffects.LEVITATION)) {
				if (ElytraItem.isFlyEnabled(itemStack)) {
					if ((fallFlyTicks + 1) % 20 == 0) {
						itemStack.hurtAndBreak(
								1,
								LivingEntity.class.cast(this),
								livingEntity -> livingEntity.broadcastBreakEvent(EquipmentSlot.CHEST)
						);
					}
					isFlying = true;
				}
				else {
					isFlying = false;
				}
			}
			else {
				isFlying = false;
			}
			setSharedFlag(7, isFlying);
			if (isFlying) {
				vanillaAfterUpdateFallFlying();
				info.cancel();
			}
		}

	}

	@Shadow protected abstract void removeFrost();
	@Shadow protected abstract void tryAddFrost();
	@Shadow protected abstract void pushEntities();
	@Shadow protected abstract void checkAutoSpinAttack(AABB aABB, AABB aABB2);
	@Shadow protected int autoSpinAttackTicks;

	private void vanillaAfterUpdateFallFlying(){
		LivingEntity self = (LivingEntity)(Object)this;
		AABB aABB = this.getBoundingBox();
		self.travel(new Vec3(self.xxa, self.yya, self.zza));
		this.level.getProfiler().pop();
		this.level.getProfiler().push("freezing");
		boolean bl2 = this.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES);
		int o;
		if (!this.level.isClientSide && !self.isDeadOrDying()) {
			o = this.getTicksFrozen();
			if (this.isInPowderSnow && this.canFreeze()) {
				this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), o + 1));
			} else {
				this.setTicksFrozen(Math.max(0, o - 2));
			}
		}

		this.removeFrost();
		this.tryAddFrost();
		if (!this.level.isClientSide && this.tickCount % 40 == 0 && this.isFullyFrozen() && this.canFreeze()) {
			o = bl2 ? 5 : 1;
			this.hurt(DamageSource.FREEZE, (float)o);
		}

		this.level.getProfiler().pop();
		this.level.getProfiler().push("push");
		if (this.autoSpinAttackTicks > 0) {
			--this.autoSpinAttackTicks;
			this.checkAutoSpinAttack(aABB, this.getBoundingBox());
		}

		this.pushEntities();
		this.level.getProfiler().pop();
		if (!this.level.isClientSide && self.isSensitiveToWater() && this.isInWaterRainOrBubble()) {
			this.hurt(DamageSource.DROWN, 1.0F);
		}
	}

	@Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
	private void be_updateFallFlying(CallbackInfo info) {
		//run be_updateFallFlying_originFix instead?
		if (BetterEnd.RUNS_FALL_FLYING_LIB) return;

		ItemStack itemStack = getItemBySlot(EquipmentSlot.CHEST);
		if (!level.isClientSide && itemStack.getItem() instanceof FallFlyingItem) {
			boolean isFlying = getSharedFlag(7);
			if (isFlying && !onGround && !isPassenger() && !hasEffect(MobEffects.LEVITATION)) {
				if (ElytraItem.isFlyEnabled(itemStack)) {
					if ((fallFlyTicks + 1) % 20 == 0) {
						itemStack.hurtAndBreak(
							1,
							LivingEntity.class.cast(this),
							livingEntity -> livingEntity.broadcastBreakEvent(EquipmentSlot.CHEST)
						);
					}
					isFlying = true;
				}
				else {
					isFlying = false;
				}
			}
			else {
				isFlying = false;
			}
			setSharedFlag(7, isFlying);
			info.cancel();
		}
	}
	
	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isFallFlying()Z", shift = Shift.AFTER), cancellable = true)
	public void be_travel(Vec3 vec3, CallbackInfo info) {
		ItemStack itemStack = getItemBySlot(EquipmentSlot.CHEST);
		if (isFallFlying() && itemStack.getItem() instanceof FallFlyingItem) {
			Vec3 moveDelta = getDeltaMovement();
			if (moveDelta.y > -0.5D) {
				fallDistance = 1.0F;
			}
			
			Vec3 lookAngle = getLookAngle();
			double d = 0.08D;
			float rotX = getXRot() * 0.017453292F;
			double k = Math.sqrt(lookAngle.x * lookAngle.x + lookAngle.z * lookAngle.z);
			double l = moveDelta.horizontalDistance();
			double lookLen = lookAngle.length();
			float n = Mth.cos(rotX);
			n = (float) (n * n * Math.min(1.0D, lookLen / 0.4D));
			moveDelta = getDeltaMovement().add(0.0D, d * (-1.0D + (double) n * 0.75D), 0.0D);
			double coef;
			if (moveDelta.y < 0.0D && k > 0.0D) {
				coef = moveDelta.y * -0.1D * (double) n;
				moveDelta = moveDelta.add(lookAngle.x * coef / k, coef, lookAngle.z * coef / k);
			}
			
			if (rotX < 0.0F && k > 0.0D) {
				coef = l * (double) (-Mth.sin(rotX)) * 0.04D;
				moveDelta = moveDelta.add(-lookAngle.x * coef / k, coef * 3.2D, -lookAngle.z * coef / k);
			}
			
			if (k > 0.0D) {
				moveDelta = moveDelta.add(
					(lookAngle.x / k * l - moveDelta.x) * 0.1D,
					0.0D,
					(lookAngle.z / k * l - moveDelta.z) * 0.1D
				);
			}
			moveDelta = moveDelta.multiply(0.9900000095367432D, 0.9800000190734863D, 0.9900000095367432D);
			double movementFactor = ((FallFlyingItem) itemStack.getItem()).getMovementFactor();
			moveDelta = moveDelta.multiply(movementFactor, 1.0D, movementFactor);
			setDeltaMovement(moveDelta);
			move(MoverType.SELF, moveDelta);
			if (!level.isClientSide) {
				if (horizontalCollision) {
					coef = moveDelta.horizontalDistance();
					double r = l - coef;
					float dmg = (float) (r * 10.0D - 3.0D);
					if (dmg > 0.0F) {
						playSound(getFallDamageSound((int) dmg), 1.0F, 1.0F);
						hurt(DamageSource.FLY_INTO_WALL, dmg);
					}
				}
				if (onGround) {
					setSharedFlag(7, false);
				}
			}
			
			calculateEntityAnimation(LivingEntity.class.cast(this), this instanceof FlyingAnimal);
			info.cancel();
		}
	}
	
	private double be_getKnockback(Item tool) {
		if (tool == null) return 0.0D;
		Collection<AttributeModifier> modifiers = tool.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND)
													  .get(Attributes.ATTACK_KNOCKBACK);
		if (modifiers.size() > 0) {
			return modifiers.iterator().next().getAmount();
		}
		return 0.0D;
	}
}
