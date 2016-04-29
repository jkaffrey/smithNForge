package com.doc.SmithNForge.blocks.tiles;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySmith extends TileEntity {

	public ItemStack toSmith = null;
	public ItemStack toApply = null;

	public static Item[] upgradeItems = { 
			Items.redstone,
			Items.iron_ingot,
			Items.gold_ingot,
			Items.diamond,
			Items.emerald,
			Items.nether_star
	};

	public float percentComplete = 0.0f;

	@Override
	public void writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);

		if (toSmith != null) {

			NBTTagCompound toSmithCmp = new NBTTagCompound();
			this.toSmith.writeToNBT(toSmithCmp);
			compound.setTag("toSmith", toSmithCmp);
		}

		if (toApply != null) {

			NBTTagCompound toSmithCmp = new NBTTagCompound();
			this.toApply.writeToNBT(toSmithCmp);
			compound.setTag("toApply", toSmithCmp);
		}
		
		compound.setFloat("percent", percentComplete);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {

		super.readFromNBT(compound);
		
		this.toSmith = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("toSmith"));
		this.toApply = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("toApply"));
		this.percentComplete = compound.getFloat("percent");
	}
}
