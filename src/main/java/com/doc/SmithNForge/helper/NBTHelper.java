package com.doc.SmithNForge.helper;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTHelper {

	public static void writeArrayToNBT(ItemStack[] stack, String arrayName, NBTTagCompound tag) {
		
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < stack.length; i++) {
			
			ItemStack iStack = stack[i];
			if (iStack != null) {
				
				NBTTagCompound cmp = new NBTTagCompound();
				cmp.setByte("Slot", (byte)i);
				stack[i].writeToNBT(cmp);
				list.appendTag(cmp);
			}
		}
		
		tag.setTag(arrayName, list);
	}
	
	public static void readArrayFromNBT(ItemStack[] stack, String arrayName, NBTTagCompound tag) {
		
		stack = new ItemStack[stack.length];
		NBTTagList list = tag.getTagList(arrayName, 10);
		
		for (int i = 0; i < list.tagCount(); i++) {
			
			NBTTagCompound tagList = (NBTTagCompound) list.getCompoundTagAt(i);
			byte slot = tagList.getByte("Slot");
			
			if (slot >= 0 && slot < stack.length) {

				stack[slot] = ItemStack.loadItemStackFromNBT(tagList);
			}
		}
	}
	
	public static int getHeatResistance(NBTTagCompound tag) {
		
		if (tag == null)
			return 0;
		
		if (tag.hasKey(new ItemStack(Items.nether_star, 1).getDisplayName()))
			return 512;
		else if (tag.hasKey(new ItemStack(Items.emerald, 1).getDisplayName()))
			return 256;
		else if (tag.hasKey(new ItemStack(Items.diamond, 1).getDisplayName()))
			return 128;
		else if (tag.hasKey(new ItemStack(Items.gold_ingot, 1).getDisplayName()))
			return 64;
		else if (tag.hasKey(new ItemStack(Items.iron_ingot, 1).getDisplayName()))
			return 32;
		else if (tag.hasKey(new ItemStack(Items.redstone, 1).getDisplayName()))
			return 16;
		
		return 0;
	}
}
