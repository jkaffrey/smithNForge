package com.doc.SmithNForge.blocks.tiles;

import org.lwjgl.opengl.GL11;

import com.doc.SmithNForge.UnlocalizedNames;
import com.doc.SmithNForge.helper.NumberHelper;
import com.doc.SmithNForge.recipes.IForgedRecipe;
import com.doc.SmithNForge.recipes.RecipeRegister;

import net.minecraft.block.BlockFire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IChatComponent;

public class TileEntityForge extends TileEntity implements IUpdatePlayerListBox, IInventory {

	public ItemStack[] recipeComponents = new ItemStack[12]; //recipe itemstack
	public ItemStack[] fuel = new ItemStack[3]; // 3 stacks of fuel
	public ItemStack toForge = null;

	public float currentHeat = 0;

	public int airSupply = 0;
	public int burnTimeOfFuel = 0;
	public int updateTime = 0;

	public void renderHUD(Minecraft mc, ScaledResolution res) {

		Tessellator tessellator = Tessellator.getInstance();

		int xc = res.getScaledWidth() / 2;
		int yc = res.getScaledHeight() / 2;

		float angle = -90;
		int radius = 24;
		int amt = 0;
		for(int i = 0; i < recipeComponents.length; i++) {
			if(recipeComponents[i] == null)
				break;
			amt++;
		}



		float anglePer = 360F / amt;

		//String toDraw = "Recipe Components";
		//mc.fontRendererObj.drawStringWithShadow(toDraw, xc - mc.fontRendererObj.getStringWidth(toDraw) / 2, yc + 35, 0xFFFFFF);
		mc.fontRendererObj.drawStringWithShadow("Heat: " + (int) (currentHeat) + "\u00b0 C", (float) (xc + Math.cos(angle * Math.PI / 180D) * radius + 35), (float) (yc + Math.sin(angle * Math.PI / 180D) * radius - 8), 0xFF0000);
		mc.fontRendererObj.drawStringWithShadow("Air: " + airSupply + " L", (float) (xc + Math.cos(angle * Math.PI / 180D) * radius + 35), (float) (yc + Math.sin(angle * Math.PI / 180D) * radius + 5), 0x00B3FF);

		if (amt > 0) {
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			for(int i = 0; i < amt; i++) {
				double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
				double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
				GL11.glTranslated(xPos, yPos, 0);
				mc.getRenderItem().renderItemIntoGUI(recipeComponents[i], 0, 0);
				GL11.glTranslated(-xPos, -yPos, 0);

				angle += anglePer;
			}
		}

		if (toForge != null) {

			mc.getRenderItem().renderItemIntoGUI(toForge, xc - 8, yc - 8);
		}

		for (int i  = 0; i < fuel.length; i++) {
			if (fuel[i] != null) {

				double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius;
				double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius;

				net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
				GL11.glTranslatef(0, 0, +10);
				mc.getRenderItem().renderItemIntoGUI(fuel[i], (int) (xPos + 35 + (i * 15)), (int) (yPos + 55));
				GL11.glTranslatef(0, 0, -10);
				net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

				mc.fontRendererObj.drawString("" + fuel[i].stackSize, (int) (xPos + 37 + (i * 16)), (int) (yPos + 61), 0xFFFFFF);
			}
		}

		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
	}


	@Override
	public void update() {

		updateTime++;
		if (updateTime % 20 != 0)
			return;

		getWorld().markBlockForUpdate(this.pos);

		boolean fireFlag = (this.worldObj.getBlockState(this.pos.down()).getBlock() instanceof BlockFire);

		if (burnTimeOfFuel <= 0 && fireFlag) {
			for (int i = 0; i < fuel.length; i++)
				if (fuel[i] != null && TileEntityFurnace.isItemFuel(fuel[i])) {

					burnTimeOfFuel = TileEntityFurnace.getItemBurnTime(fuel[i]) / 100;
					this.decrStackSize(i, 1);
					break;
				}
		}

		if (airSupply > 0 && burnTimeOfFuel > 0 && fireFlag) {

			currentHeat += NumberHelper.randomNumber(0, 0.05);
			airSupply--;
		} else {

			float toSubtract = NumberHelper.randomNumber(0, 0.075);
			if (currentHeat - toSubtract <= 0)  currentHeat = 0; else currentHeat -= toSubtract;
		}

		/*if (!this.worldObj.isRemote)
			System.out.printf("Temperature: %f :: Air: %f :: Burn Time:: %f\n", currentHeat, (float) airSupply, (float) burnTimeOfFuel);*/

		if (burnTimeOfFuel > 0) burnTimeOfFuel--; else burnTimeOfFuel = 0;

		if (updateTime % 200 == 0) {

			boolean isImbued = RecipeRegister.containsRequiredImbued(toForge, recipeComponents[0]);
			boolean isAllThe = RecipeRegister.containsRequiredItems(recipeComponents);

			boolean isHeatMet = false;
			IForgedRecipe recipe = RecipeRegister.getRecipe(recipeComponents[0]);

			if (recipe != null) {

				isHeatMet = recipe.requiredHeat() <= currentHeat;
			}

			if (isImbued && isAllThe && isHeatMet) {

				toForge.getTagCompound().setBoolean(recipe.effectName(), true);
				toForge.getTagCompound().setInteger("heat", recipe.outputHeat());
				recipeComponents = new ItemStack[12];
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);

		compound.setInteger("air", airSupply);
		compound.setInteger("burn", burnTimeOfFuel);
		compound.setFloat("heat", currentHeat);

		NBTTagList list = new NBTTagList();
		for (int i = 0; i < fuel.length; i++) {

			ItemStack iStack = fuel[i];
			if (iStack != null) {

				NBTTagCompound cmp = new NBTTagCompound();
				cmp.setByte("Slot", (byte)i);
				fuel[i].writeToNBT(cmp);
				list.appendTag(cmp);
			}
		}

		compound.setTag("Fuel", list);

		list = new NBTTagList();
		for (int i = 0; i < recipeComponents.length; i++) {

			ItemStack iStack = recipeComponents[i];
			if (iStack != null) {

				NBTTagCompound cmp = new NBTTagCompound();
				cmp.setByte("Slot", (byte)i);
				recipeComponents[i].writeToNBT(cmp);
				list.appendTag(cmp);
			}
		}

		compound.setTag("Recipe", list);

		if (toForge != null) {

			NBTTagCompound toForgeCmp = new NBTTagCompound();
			this.toForge.writeToNBT(toForgeCmp);
			compound.setTag("toForge", toForgeCmp);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {

		super.readFromNBT(compound);

		airSupply = compound.getInteger("air");
		burnTimeOfFuel = compound.getInteger("burn");

		currentHeat = compound.getFloat("heat");

		fuel = new ItemStack[fuel.length];
		NBTTagList list = compound.getTagList("Fuel", 10);

		for (int i = 0; i < list.tagCount(); i++) {

			NBTTagCompound tagList = (NBTTagCompound) list.getCompoundTagAt(i);
			byte slot = tagList.getByte("Slot");

			if (slot >= 0 && slot < fuel.length) {

				fuel[slot] = ItemStack.loadItemStackFromNBT(tagList);
			}
		}

		recipeComponents = new ItemStack[recipeComponents.length];
		list = compound.getTagList("Recipe", 10);

		for (int i = 0; i < list.tagCount(); i++) {

			NBTTagCompound tagList = (NBTTagCompound) list.getCompoundTagAt(i);
			byte slot = tagList.getByte("Slot");

			if (slot >= 0 && slot < recipeComponents.length) {

				recipeComponents[slot] = ItemStack.loadItemStackFromNBT(tagList);
			}
		}

		this.toForge = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("toForge"));
	}

	@Override
	public String getName() {

		return UnlocalizedNames.forgeTile + ".inventory";
	}

	@Override
	public boolean hasCustomName() {

		return false;
	}

	@Override
	public IChatComponent getDisplayName() {

		return null;
	}

	@Override
	public int getSizeInventory() {

		return fuel.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {

		if (index > getSizeInventory() - 1)
			return fuel[0];

		return fuel[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {

		if (this.fuel[index] != null) {

			ItemStack itemstack;

			if (this.fuel[index].stackSize <= count) {

				itemstack = this.fuel[index];
				this.fuel[index] = null;
				return itemstack;
			} else {

				itemstack = this.fuel[index].splitStack(count);

				if (this.fuel[index].stackSize == 0)
					this.fuel[index] = null;

				return itemstack;
			}
		}

		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {

		if (this.fuel[index] != null) {

			ItemStack itemstack = this.fuel[index];
			this.fuel[index] = null;
			return itemstack;
		}		

		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {

		fuel[index] = stack;
	}

	@Override
	public int getInventoryStackLimit() {

		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {

		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {

		return TileEntityFurnace.isItemFuel(stack);
	}

	@Override
	public int getField(int id) {

		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {

		return 0;
	}

	@Override
	public void clear() {

		for (int i = 0; i < fuel.length; i++)
			fuel[i] = null;
	}

	@Override
	public Packet getDescriptionPacket() {

		NBTTagCompound syncData = new NBTTagCompound();
		this.writeToNBT(syncData);
		return new S35PacketUpdateTileEntity(getPos(), -999, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}
}
