package com.doc.SmithNForge.listeners;

import com.doc.SmithNForge.blocks.tiles.TileEntityForge;
import com.doc.SmithNForge.blocks.tiles.TileEntitySmith;
import com.doc.SmithNForge.recipes.UnlocalizedAbilityNames;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerClient {

	@SubscribeEvent
	public void handleTooltip(ItemTooltipEvent event) {
		
		if (event.itemStack != null && event.itemStack.hasTagCompound()) {
			
			if (event.itemStack.getTagCompound().hasKey("snf_imbued")){
				
				event.toolTip.add("Imbued with: ");
			}
			
			for (Item iStack : TileEntitySmith.upgradeItems) {
				
				ItemStack asStack = new ItemStack(iStack, 1);
				if (event.itemStack.getTagCompound().hasKey(asStack.getDisplayName())) {
				
					event.toolTip.add(event.itemStack.getTagCompound().getInteger(asStack.getDisplayName()) + " " + asStack.getDisplayName());
				}
			}
			
			if (event.itemStack.getTagCompound().hasKey("heat_resistant")) {
				
				event.toolTip.add("Heat resistant up to " + event.itemStack.getTagCompound().getInteger("heat_resistant") + "\u00b0 C");
			}
			
			if (event.itemStack.getTagCompound().hasKey("snf_imbued")){
				event.toolTip.add("Abilites: ");
			}
			
			for (String s : UnlocalizedAbilityNames.allAbilities) {
				
				if (event.itemStack.getTagCompound().hasKey(s))
					event.toolTip.add(s);
			}
			
			if (event.itemStack.getTagCompound().hasKey("heat")){
				event.toolTip.add("Heat: " + event.itemStack.getTagCompound().getInteger("heat") + "\u00b0 C");
			}
		}
	}
	
	@SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {

		Minecraft mc = Minecraft.getMinecraft();
		Profiler profiler = mc.mcProfiler;
		ItemStack equippedStack = mc.thePlayer.getCurrentEquippedItem();
		if(event.type == ElementType.ALL) {
			profiler.startSection("botania-hud");
			MovingObjectPosition pos = mc.objectMouseOver;

			if (pos != null) {
				
				TileEntity tile = mc.theWorld.getTileEntity(new BlockPos(pos.getBlockPos().getX(), pos.getBlockPos().getY(), pos.getBlockPos().getZ()));
				if(tile != null && tile instanceof TileEntityForge)
					((TileEntityForge) tile).renderHUD(mc, event.resolution);
			}
		}
    }
}
