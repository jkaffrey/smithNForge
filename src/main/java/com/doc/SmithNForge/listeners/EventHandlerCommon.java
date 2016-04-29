package com.doc.SmithNForge.listeners;

import com.doc.SmithNForge.entity.ExtendedPlayer;
import com.doc.SmithNForge.recipes.UnlocalizedAbilityNames;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

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
	public void onLiving(PlayerTickEvent updateEvent) {

		if (updateEvent.player != null) {

			ExtendedPlayer props = ExtendedPlayer.get(updateEvent.player);

			props.clearEffects();

			for (ItemStack armourItem : updateEvent.player.inventory.armorInventory) {
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
					
					updateEvent.player.capabilities.setPlayerWalkSpeed(updateEvent.player.capabilities.getWalkSpeed() + WALK_INCREASE);
				} else {

					if (updateEvent.player.capabilities.getWalkSpeed() > 0.1f)
						updateEvent.player.capabilities.setPlayerWalkSpeed(updateEvent.player.capabilities.getWalkSpeed() - WALK_INCREASE);
					else
						updateEvent.player.capabilities.setPlayerWalkSpeed(0.1f);
				}
			}
		}
	}
}
