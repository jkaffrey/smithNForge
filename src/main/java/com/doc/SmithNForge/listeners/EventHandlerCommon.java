package com.doc.SmithNForge.listeners;

import com.doc.SmithNForge.entity.ExtendedPlayer;
import com.doc.SmithNForge.recipes.UnlocalizedAbilityNames;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerCommon {

	public final float WALK_INCREASE = 0.1f;

	@SubscribeEvent
	public void onJump(LivingJumpEvent jumpEvent) {


		if (jumpEvent.entity instanceof EntityPlayer) {

			EntityPlayer ePlayer = (EntityPlayer) jumpEvent.entity;
			ExtendedPlayer props = ExtendedPlayer.get(ePlayer);

			for (String name : props.effects)
				if (name != null && name.equalsIgnoreCase(UnlocalizedAbilityNames.jumpBoost)) {


					jumpEvent.entity.addVelocity(0, 0.25f, 0);
				}
		}

	}

	@SubscribeEvent
	public void entityConstruct(EntityConstructing event) {

		if (event.entity instanceof EntityPlayer && ExtendedPlayer.get((EntityPlayer) event.entity) == null)
			ExtendedPlayer.register((EntityPlayer) event.entity);
	}

	@SubscribeEvent
	public void onLiving(LivingUpdateEvent updateEvent) {

		if (updateEvent.entity != null && updateEvent.entity instanceof EntityPlayer) {

			EntityPlayer ePlayer = (EntityPlayer) updateEvent.entity;
			ExtendedPlayer props = ExtendedPlayer.get(ePlayer);

			props.clearEffects();

			for (ItemStack armourItem : ePlayer.inventory.armorInventory) {
				for (String name : UnlocalizedAbilityNames.allAbilities) {
					if (armourItem != null && armourItem.hasTagCompound()) {

						if (armourItem.getTagCompound().hasKey("cracked"))
								break;
						
						if (armourItem.getTagCompound().hasKey(name)) {

							props.effects.add(name);
						} 
					}
				}
			}

			for (String effects : props.effects) {

				if (effects.equalsIgnoreCase(UnlocalizedAbilityNames.speedBoost)) {
					
					ePlayer.capabilities.setPlayerWalkSpeed(ePlayer.capabilities.getWalkSpeed() + WALK_INCREASE);
				} else {

					if (ePlayer.capabilities.getWalkSpeed() > 0.1f)
						ePlayer.capabilities.setPlayerWalkSpeed(ePlayer.capabilities.getWalkSpeed() - WALK_INCREASE);
					else
						ePlayer.capabilities.setPlayerWalkSpeed(0.1f);
				}
			}
		}
	}
}
