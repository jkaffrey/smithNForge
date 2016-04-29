package com.doc.SmithNForge.blocks;

import com.doc.SmithNForge.UnlocalizedNames;
import com.doc.SmithNForge.blocks.tiles.TileEntityForge;
import com.doc.SmithNForge.blocks.tiles.TileEntityForge;
import com.doc.SmithNForge.helper.InventoryHelper;
import com.doc.SmithNForge.helper.NumberHelper;
import com.doc.SmithNForge.helper.PacketRefresh;
import com.doc.SmithNForge.items.ItemBellows;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockForge extends BlockContainer {

	public BlockForge() {

		super(Material.iron);
		this.setHardness(2.0f);
		this.setUnlocalizedName(UnlocalizedNames.forge);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {

		return new TileEntityForge();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {

		super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);

		TileEntity myTile = worldIn.getTileEntity(pos);
		if (!(myTile instanceof TileEntityForge))
			return false;

		TileEntityForge entityForge = (TileEntityForge) myTile;
		ItemStack onPlayer = playerIn.inventory.getCurrentItem();
		if (TileEntityFurnace.isItemFuel(onPlayer)) {
			
			int remaining = InventoryHelper.storePartialItemStack(onPlayer, entityForge);
			onPlayer.stackSize = remaining;
			return true;
		} else if (entityForge.toForge == null 
				&& playerIn.inventory.getCurrentItem() != null 
				&& playerIn.inventory.getCurrentItem().getItem() instanceof ItemArmor) {
			
			entityForge.toForge = onPlayer.copy();
			playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, onPlayer.splitStack(0));
			return true;
		} else if (onPlayer == null) {
			
			playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, entityForge.toForge);
			entityForge.toForge = null;
			return true;
		} else if (onPlayer != null && entityForge.toForge != null) {
			
			int toAddAt = InventoryHelper.getEmptyIndex(entityForge.recipeComponents);
			if (toAddAt >= 0) {
				
				entityForge.recipeComponents[toAddAt] = onPlayer;
				onPlayer.splitStack(1);
				
				return true;
			}
		}

		return false;
	}
	
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		
		super.onBlockClicked(worldIn, pos, playerIn);
		
		TileEntity myTile = worldIn.getTileEntity(pos);
		if (!(myTile instanceof TileEntityForge))
			return;

		TileEntityForge entityForge = (TileEntityForge) myTile;
		ItemStack onPlayer = playerIn.inventory.getCurrentItem();
		if (onPlayer != null && onPlayer.getItem() instanceof ItemBellows) {

			entityForge.airSupply += NumberHelper.randomNumber(100, 200);
			return;
		} 
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {

		TileEntity myTile = world.getTileEntity(pos);
		if (!(myTile instanceof TileEntityForge))
			return;

		TileEntityForge entitySmith = (TileEntityForge) myTile;
		//world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), entitySmith.toSmith));
		//world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), entitySmith.toApply));

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
