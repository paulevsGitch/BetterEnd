package ru.betterend.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import ru.betterend.blocks.RunedFlavolite;
import ru.betterend.registry.EndItems;
import ru.betterend.util.PortalFrameHelper;

public class EternalCrystal extends Item {

	public EternalCrystal() {
		super(EndItems.makeSettings());
	}
	
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (world.isClient) return ActionResult.CONSUME;
		BlockPos usedPos = context.getBlockPos();
		BlockState usedBlock = world.getBlockState(usedPos);
		if (usedBlock.getBlock() instanceof RunedFlavolite && !usedBlock.get(RunedFlavolite.ACTIVATED)) {
			if (PortalFrameHelper.checkPortalFrame((ServerWorld) world, usedPos, usedBlock.getBlock())) {
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}
}
