package ru.betterend.item.tool;

import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import io.netty.util.internal.ThreadLocalRandom;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.attribute.EntityAttribute;
import net.minecraft.world.entity.attribute.EntityAttributeModifier;
import net.minecraft.world.entity.attribute.EntityAttributes;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MiningToolItem;
import net.minecraft.world.item.Tier;
import net.minecraft.tags.Tag;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndTags;

public class EndHammerItem extends MiningToolItem implements DynamicAttributeTool, Patterned {
	public final static UUID ATTACK_KNOCKBACK_MODIFIER_ID = Mth.randomUuid(ThreadLocalRandom.current());

	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public EndHammerItem(Tier material, float attackDamage, float attackSpeed, double knockback, Properties settings) {
		super(attackDamage, attackSpeed, material, Sets.newHashSet(), settings);

		Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
				new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier",
						attackDamage + material.getAttackDamage(), EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID,
				"Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, new EntityAttributeModifier(ATTACK_KNOCKBACK_MODIFIER_ID,
				"Weapon modifier", knockback, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public boolean canMine(BlockState state, Level world, BlockPos pos, PlayerEntity miner) {
		return state.getMaterial().equals(Material.STONE) || state.getMaterial().equals(Material.GLASS)
				|| state.is(Blocks.DIAMOND_BLOCK) || state.is(Blocks.EMERALD_BLOCK) || state.is(Blocks.LAPIS_BLOCK)
				|| state.is(Blocks.REDSTONE_BLOCK);
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damage(1, attacker, ((entity) -> {
			entity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
		}));

		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (state.getHardness(world, pos) != 0.0F) {
			stack.damage(1, miner, ((entity) -> {
				entity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
			}));
		}

		return true;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (state.getMaterial().equals(Material.GLASS)) {
			return this.getMaterial().getDestroySpeed() * 2.0F;
		}
		if (isCorrectToolForDrops(state)) {
			float mult = 1.0F;
			if (state.is(Blocks.DIAMOND_BLOCK) || state.is(Blocks.EMERALD_BLOCK) || state.is(Blocks.LAPIS_BLOCK)
					|| state.is(Blocks.REDSTONE_BLOCK)) {
				mult = this.getMaterial().getDestroySpeed();
			} else {
				mult = this.getMaterial().getDestroySpeed() / 2.0F;
			}
			return mult > 1.0F ? mult : 1.0F;
		}
		return 1.0F;
	}

	@Override
	public float getDestroySpeed(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (tag.equals(EndTags.HAMMERS)) {
			return this.getDestroySpeed(stack, state);
		}
		return 1.0F;
	}

	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (tag.equals(EndTags.HAMMERS)) {
			return this.getTier().getLevel();
		}
		return 0;
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState state) {
		if (state.getMaterial().equals(Material.GLASS)) {
			return true;
		}
		if (!state.is(Blocks.REDSTONE_BLOCK) && !state.is(Blocks.DIAMOND_BLOCK) && !state.is(Blocks.EMERALD_BLOCK)
				&& !state.is(Blocks.LAPIS_BLOCK) && !state.getMaterial().equals(Material.STONE)) {
			return false;
		}
		int level = this.getTier().getLevel();
		if (state.is(Blocks.IRON_ORE) || state.is(Blocks.LAPIS_BLOCK) || state.is(Blocks.LAPIS_ORE)) {
			return level >= 1;
		}
		if (state.is(Blocks.DIAMOND_BLOCK) && !state.is(Blocks.DIAMOND_ORE) || state.is(Blocks.EMERALD_ORE)
				|| state.is(Blocks.EMERALD_BLOCK) || state.is(Blocks.GOLD_ORE) || state.is(Blocks.REDSTONE_ORE)) {
			return level >= 2;
		}
		if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(Blocks.RESPAWN_ANCHOR)
				|| state.is(Blocks.ANCIENT_DEBRIS)) {
			return level >= 3;
		}
		return true;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}

	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_HANDHELD, name);
	}
}
