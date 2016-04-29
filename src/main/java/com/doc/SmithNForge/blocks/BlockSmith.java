package com.doc.SmithNForge.blocks;

import com.doc.SmithNForge.UnlocalizedNames;
import com.doc.SmithNForge.blocks.tiles.TileEntitySmith;
import com.doc.SmithNForge.helper.ChatHelper;
import com.doc.SmithNForge.helper.NBTHelper;
import com.doc.SmithNForge.helper.NumberHelper;
import com.doc.SmithNForge.items.ItemHammer;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class BlockSmith extends BlockContainer {

	public BlockSmith() {

		super(Material.iron);
		this.setHardness(2.0f);
		this.setUnlocalizedName(UnlocalizedNames.smith);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	private boolean isUpgradeItem(ItemStack toUseForUpgrade) {

		for (Item toCheck : TileEntitySmith.upgradeItems) {

			if (toUseForUpgrade.getItem() == toCheck)
				return true;
		}

		return false;
	}

	public float getValuePerItem(ItemStack iStack) {

		if (iStack == null)
			return 0.0f;
		
		if (iStack.getItem() == Items.redstone)
			return NumberHelper.randomNumber(0.05, 0.01);
		else if (iStack.getItem() == Items.iron_ingot)
			return NumberHelper.randomNumber(0.025, 0.008);
		else if (iStack.getItem() == Items.gold_ingot)
			return NumberHelper.randomNumber(0.025, 0.005);
		else if (iStack.getItem() == Items.diamond)
			return NumberHelper.randomNumber(0.02, 0.009);
		else if (iStack.getItem() == Items.emerald)
			return NumberHelper.randomNumber(0.02, 0.008);
		else if (iStack.getItem() == Items.nether_star)
			return NumberHelper.randomNumber(0.01, 0);

		return 0.0f;
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer player) {

		super.onBlockClicked(worldIn, pos, player);

		TileEntity myTile = worldIn.getTileEntity(pos);
		if (!(myTile instanceof TileEntitySmith))
			return;

		TileEntitySmith entitySmith = (TileEntitySmith) myTile;
		ItemStack onPlayer = player.inventory.getCurrentItem();
		if (onPlayer != null && onPlayer.getItem() instanceof ItemHammer) {

			if (!(worldIn.getBlockState(pos.down()).getBlock() instanceof BlockFire)) {
			
				if (!worldIn.isRemote)
					ChatHelper.sendNoSpamMessages(new IChatComponent[] {
							new ChatComponentText("[Smith 'N Forge] Missing heat source underneath.")
					});
				
				return;
			}
			
			if (entitySmith.toApply == null || entitySmith.toSmith == null) {
				
				if (!worldIn.isRemote)
					ChatHelper.sendNoSpamMessages(new IChatComponent[] {
							new ChatComponentText("[Smith 'N Forge] Incomplete smithing.")
					});
				
				return;
			}
			
			entitySmith.percentComplete += getValuePerItem(entitySmith.toApply);

			if (entitySmith.percentComplete >= 1) {

				entitySmith.percentComplete = 0;
				
				ItemStack iStack = entitySmith.toSmith;
				if (iStack.hasTagCompound()) {
					
					NBTTagCompound tagCmp = iStack.getTagCompound();
					int totalImbued = 1;
					if (tagCmp.hasKey(entitySmith.toApply.getDisplayName()))
							totalImbued = tagCmp.getInteger(entitySmith.toApply.getDisplayName()) + 1;
					tagCmp.setInteger(entitySmith.toApply.getDisplayName(), totalImbued);
					tagCmp.setBoolean("snf_imbued", true);
					tagCmp.setInteger("heat_resistant", NBTHelper.getHeatResistance(tagCmp));
				} else {
					
					NBTTagCompound tagCmp = new NBTTagCompound();
					int totalImbued = 1;
					if (tagCmp.hasKey(entitySmith.toApply.getDisplayName()))
							totalImbued = tagCmp.getInteger(entitySmith.toApply.getDisplayName()) + 1;
					tagCmp.setInteger(entitySmith.toApply.getDisplayName(), totalImbued);
					iStack.setTagCompound(tagCmp);
					tagCmp.setBoolean("snf_imbued", true);
					tagCmp.setInteger("heat_resistant", NBTHelper.getHeatResistance(tagCmp));
				}
				
				if (!worldIn.isRemote) worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), iStack));
				entitySmith.toApply = null;
				entitySmith.toSmith = null;
				
				if (!worldIn.isRemote)
					ChatHelper.sendNoSpamMessages(new IChatComponent[] {
							new ChatComponentText("[Smith 'N Forge] Finshied!")
					});
				
				return;
				
			}
			if (!worldIn.isRemote) {

				if (entitySmith.percentComplete != 0)
					ChatHelper.sendNoSpamMessages(new IChatComponent[] {
							new ChatComponentText("[Smith 'N Forge] Current progress: " + NumberHelper.rounded(entitySmith.percentComplete * 100) + "%")
					});
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) {

		super.onBlockActivated(worldIn, pos, state, player, side, hitX, hitY, hitZ);

		TileEntity myTile = worldIn.getTileEntity(pos);
		if (!(myTile instanceof TileEntitySmith))
			return false;

		TileEntitySmith entitySmith = (TileEntitySmith) myTile;

		if (entitySmith.toSmith == null 
				&& player.inventory.getCurrentItem() != null 
				&& player.inventory.getCurrentItem().getItem() instanceof ItemArmor) {

			ItemStack onPlayer = player.inventory.getCurrentItem();
			entitySmith.toSmith = onPlayer.copy();

			onPlayer.splitStack(1);

			player.inventory.setInventorySlotContents(player.inventory.currentItem, onPlayer);
			return true;
		} else if (entitySmith.toSmith != null 
				&& entitySmith.toApply == null 
				&& player.inventory.getCurrentItem() != null 
				&& isUpgradeItem(player.inventory.getCurrentItem())) {

			ItemStack onPlayer = player.inventory.getCurrentItem();
			onPlayer.splitStack(1);

			entitySmith.toApply = new ItemStack(onPlayer.getItem(), 1);

			player.inventory.setInventorySlotContents(player.inventory.currentItem, onPlayer);
			return true;
		}

		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {

		return new TileEntitySmith();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {

		TileEntity myTile = world.getTileEntity(pos);
		if (!(myTile instanceof TileEntitySmith))
			return;

		TileEntitySmith entitySmith = (TileEntitySmith) myTile;
		world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), entitySmith.toSmith));
		world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), entitySmith.toApply));

		super.breakBlock(world, pos, state);	
		world.removeTileEntity(pos);
	}

	@Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {

		super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);

		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
	}
}
