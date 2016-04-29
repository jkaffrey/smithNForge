package com.doc.SmithNForge.entity;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants;

public class ExtendedPlayer implements IExtendedEntityProperties {

	public final static String EXT_PROP_NAME = "SNF_Entities";
	private final EntityPlayer player;

	public List<String> effects = new LinkedList<String>();

	public ExtendedPlayer(EntityPlayer player) {

		this.player = player;
		effects.add("dummy");
	}

	public void clearEffects() {
		
		effects.clear();
		effects.add("dummy");
	}
	
	public static final void register(EntityPlayer player) {

		player.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer(player));
	}

	public static final ExtendedPlayer get(EntityPlayer player) {

		return (ExtendedPlayer) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {

		NBTTagList tagList = new NBTTagList();
		for(int i = 0; i < effects.size(); i++) {

			String s = effects.get(i);
			if(s != null) {

				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("effect" + i, s);
				tagList.appendTag(tag);
			}
		}

		compound.setTag("currentEffects", tagList);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {

		effects.clear();

		NBTTagList tagList = compound.getTagList("currentEffects", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < tagList.tagCount(); i++) {
			
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			String s = tag.getString("effect" + i);
			effects.add(i, s);
		}
	}

	@Override
	public void init(Entity entity, World world) {}
}
