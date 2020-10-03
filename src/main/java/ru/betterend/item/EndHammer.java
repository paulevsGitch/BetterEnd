package ru.betterend.item;

import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import io.netty.util.internal.ThreadLocalRandom;

import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.fabricmc.fabric.api.tool.attribute.v1.ToolManager;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EndHammer extends MiningToolItem implements DynamicAttributeTool {
	
	public final static UUID ATTACK_KNOCKBACK_MODIFIER_ID = MathHelper.randomUuid(ThreadLocalRandom.current());
	
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
	private ItemStack itemStack;

	public EndHammer(ToolMaterial material, float attackDamage, float attackSpeed, double knockback, Settings settings) {
		super(attackDamage, attackSpeed, material, Sets.newHashSet(), settings);

		Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", attackDamage + material.getAttackDamage(), EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, new EntityAttributeModifier(ATTACK_KNOCKBACK_MODIFIER_ID, "Weapon modifier", knockback, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
		this.itemStack = new ItemStack(this);
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		return !miner.isCreative() || state.getMaterial().equals(Material.STONE) || state.getMaterial().equals(Material.GLASS);
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damage(1, attacker, ((entity) -> {
			entity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
		}));
		
		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (state.getHardness(world, pos) != 0.0F) {
			stack.damage(1, miner, ((entity) -> {
				entity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
			}));
		}

		return true;
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		if (state.getMaterial().equals(Material.GLASS)) {
			return 10.0F;
		}
		return 1.0F;
	}
	
	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		return this.getMaterial().getMiningLevel();
	}

	@Override
	public boolean isEffectiveOn(BlockState state) {
		if (state.getMaterial().equals(Material.GLASS)) {
			return true;
		}
		if (!state.getMaterial().equals(Material.STONE)) {
			return false;
		}
		return ToolManager.handleIsEffectiveOnIgnoresVanilla(state, itemStack, null);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}
}
