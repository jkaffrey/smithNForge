package com.doc.SmithNForge.recipes;

import net.minecraft.item.ItemStack;

public interface IForgedRecipe {

	public ItemStack[] requiredItems();
	
	/*
	 * Required level of heat the forge must reach
	 * before it can start on the recipe
	 */
	public int requiredHeat();
	
	public int requiredRedstone();
	
	public int requiredIron();
	
	public int requiredGold();
	
	public int requiredDiamond();
	
	public int requiredEmerald();
	
	public int requiredStar();

	public int requiredTime();
	
	/*
	 * Heat on item after recipe completed
	 * holding item while hot will damage the player
	 * or catch them on fire.
	 * Placing heated item in water will cool it quicker
	 */
	public int outputHeat();
	
	public String effectName();
}
