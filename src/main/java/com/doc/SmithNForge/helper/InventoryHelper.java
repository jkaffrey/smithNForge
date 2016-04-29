package com.doc.SmithNForge.helper;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryHelper {

	public static int getEmptyIndex(ItemStack[] stack) {
		
		for (int i = 0; i < stack.length; i++)
			if (stack[i] == null)
				return i;
		
		return -1;
	}
	
	public static boolean inventoryHasRoom(ItemStack iStack, IInventory inventory) {

		if (inventory != null) {
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				if (inventory.getStackInSlot(i) == null)
					return true;
			}
		}

		return false;
	}

	 public static int getFirstEmptyStack(IInventory inventory) {
		 
	        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
	            if (inventory.getStackInSlot(i) == null) {
	                return i;
	            }
	        }

	        return -1;
	    }
	
	public static int storeItemStack(ItemStack itemStack, IInventory inventory) {
		
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
        	
            if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).getItem() == itemStack.getItem() && inventory.getStackInSlot(i).isStackable() && inventory.getStackInSlot(i).stackSize < inventory.getStackInSlot(i).getMaxStackSize() && inventory.getStackInSlot(i).stackSize < inventory.getInventoryStackLimit() && (!inventory.getStackInSlot(i).getHasSubtypes() || inventory.getStackInSlot(i).getMetadata() == itemStack.getMetadata()) && ItemStack.areItemStackTagsEqual(inventory.getStackInSlot(i), itemStack)) {
                return i;
            }
        }

        return -1;
    }
	
	public static int storePartialItemStack(ItemStack itemStack, IInventory inventory) {
		
        Item item = itemStack.getItem();
        int i = itemStack.stackSize;
        int j = storeItemStack(itemStack, inventory);

        if (j < 0)
            j = getFirstEmptyStack(inventory);

        if (j < 0)
            return i;
        else {
            if (inventory.getStackInSlot(j) == null) {
                inventory.setInventorySlotContents(j, new ItemStack(item, 0, itemStack.getMetadata()));

                if (itemStack.hasTagCompound()) {
                	
                    inventory.getStackInSlot(j).setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
                }
            }

            int k = i;

            if (i > inventory.getStackInSlot(j).getMaxStackSize() - inventory.getStackInSlot(j).stackSize) {
            	
                k = inventory.getStackInSlot(j).getMaxStackSize() - inventory.getStackInSlot(j).stackSize;
            }

            if (k > inventory.getInventoryStackLimit() - inventory.getStackInSlot(j).stackSize) {
            	
                k = inventory.getInventoryStackLimit() - inventory.getStackInSlot(j).stackSize;
            }

            if (k == 0)
                return i;
            else {
                i -= k;
                inventory.getStackInSlot(j).stackSize += k;
                inventory.getStackInSlot(j).animationsToGo = 5;
                return i;
            }
        }
    }
}