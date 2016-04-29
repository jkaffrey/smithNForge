package com.doc.SmithNForge.recipes.recipe;

import com.doc.SmithNForge.recipes.IForgedRecipe;
import com.doc.SmithNForge.recipes.UnlocalizedAbilityNames;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Magnet implements IForgedRecipe {

	@Override
	public ItemStack[] requiredItems() {
		
		return new ItemStack[] {
				
				new ItemStack(Items.potato),
				new ItemStack(Items.ender_pearl)
		};
	}

	@Override
	public int requiredHeat() {
		
		return 0;
	}

	@Override
	public int requiredRedstone() {
		
		return 2;
	}

	@Override
	public int requiredIron() {
		
		return 2;
	}

	@Override
	public int requiredGold() {
		
		return 4;
	}

	@Override
	public int requiredDiamond() {
		
		return 0;
	}

	@Override
	public int requiredEmerald() {
		
		return 0;
	}

	@Override
	public int requiredStar() {
		
		return 0;
	}

	@Override
	public int requiredTime() {
		
		return 0;
	}

	@Override
	public int outputHeat() {
		
		return 0;
	}

	@Override
	public String effectName() {
		
		return UnlocalizedAbilityNames.magnet;
	}

}
