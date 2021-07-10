package ru.betterend.item.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import io.netty.util.internal.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.TagAPI;
import ru.bclib.client.models.ItemModelProvider;
import ru.bclib.client.models.ModelsHelper;

import java.util.UUID;

public class EndHammerItem extends DiggerItem implements DynamicAttributeTool, ItemModelProvider {
	public final static UUID ATTACK_KNOCKBACK_MODIFIER_ID = Mth.createInsecureUUID(ThreadLocalRandom.current());
	
	private final Multimap<Attribute, AttributeModifier> attributeModifiers;
	
	public EndHammerItem(Tier material, float attackDamage, float attackSpeed, double knockback, Properties settings) {
		//we override all methods that access BlockTags.MINEABLE_WITH_PICKAXE in the superclass, so this should not matter
		super(attackDamage, attackSpeed, material, BlockTags.MINEABLE_WITH_PICKAXE, settings);
		
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage + material.getAttackDamageBonus(), AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(ATTACK_KNOCKBACK_MODIFIER_ID, "Weapon modifier", knockback, AttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}
	
	@Override
	public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player miner) {
		return state.getMaterial().equals(Material.STONE) || state.getMaterial().equals(Material.GLASS) || state.is(Blocks.DIAMOND_BLOCK) || state.is(Blocks.EMERALD_BLOCK) || state.is(Blocks.LAPIS_BLOCK) || state.is(Blocks.REDSTONE_BLOCK);
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, ((entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND)));
		
		return true;
	}
	
	@Override
	public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (state.getDestroySpeed(world, pos) != 0.0F) {
			stack.hurtAndBreak(1, miner, ((entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND)));
		}
		
		return true;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (state.getMaterial().equals(Material.GLASS)) {
			return this.getTier().getSpeed() * 2.0F;
		}
		if (isCorrectToolForDrops(state)) {
			float mult;
			if (state.is(Blocks.DIAMOND_BLOCK) || state.is(Blocks.EMERALD_BLOCK) || state.is(Blocks.LAPIS_BLOCK) || state.is(Blocks.REDSTONE_BLOCK)) {
				mult = this.getTier().getSpeed();
			}
			else {
				mult = this.getTier().getSpeed() / 2.0F;
			}
			return Math.max(mult, 1.0F);
		}
		return 1.0F;
	}
	
	@Override
	public float getMiningSpeedMultiplier(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (tag.equals(TagAPI.HAMMERS)) {
			return this.getDestroySpeed(stack, state);
		}
		return 1.0F;
	}
	
	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (tag.equals(TagAPI.HAMMERS)) {
			return this.getTier().getLevel();
		}
		return 0;
	}
	
	@Override
	public boolean isCorrectToolForDrops(BlockState state) {
		if (state.getMaterial().equals(Material.GLASS)) {
			return true;
		}
		if (!state.is(Blocks.REDSTONE_BLOCK) && !state.is(Blocks.DIAMOND_BLOCK) && !state.is(Blocks.EMERALD_BLOCK) && !state.is(Blocks.LAPIS_BLOCK) && !state.getMaterial().equals(Material.STONE)) {
			return false;
		}
		int level = this.getTier().getLevel();
		if (state.is(Blocks.IRON_ORE) || state.is(Blocks.LAPIS_BLOCK) || state.is(Blocks.LAPIS_ORE)) {
			return level >= 1;
		}
		if (state.is(Blocks.DIAMOND_BLOCK) && !state.is(Blocks.DIAMOND_ORE) || state.is(Blocks.EMERALD_ORE) || state.is(Blocks.EMERALD_BLOCK) || state.is(Blocks.GOLD_ORE) || state.is(Blocks.REDSTONE_ORE)) {
			return level >= 2;
		}
		if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(Blocks.RESPAWN_ANCHOR) || state.is(Blocks.ANCIENT_DEBRIS)) {
			return level >= 3;
		}
		return true;
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(slot);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return ModelsHelper.createHandheldItem(resourceLocation);
	}
}
