package com.doc.SmithNForge.helper;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class PacketRefresh {

	public static void dispatchTEToNearbyPlayers(TileEntity tile) {
		
		World world = tile.getWorld();
		List players = world.playerEntities;
		for(Object player : players)
			if(player instanceof EntityPlayerMP) {
				EntityPlayerMP mp = (EntityPlayerMP) player;
				if(mp != null && pointDistancePlane(mp.posX, mp.posZ, tile.getPos().getX() + 0.5, tile.getPos().getZ()+ 0.5) < 64)
					((EntityPlayerMP) player).playerNetServerHandler.sendPacket(tile.getDescriptionPacket());
			}
	}

	public static void dispatchTEToNearbyPlayers(World world, int x, int y, int z) {
		
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if(tile != null)
			dispatchTEToNearbyPlayers(tile);
	}

	public static float pointDistancePlane(double x1, double y1, double x2, double y2) {
		
		return (float) Math.hypot(x1 - x2, y1 - y2);
	}
}
