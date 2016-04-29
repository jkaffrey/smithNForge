package com.doc.SmithNForge.recipes.recipe;

import com.doc.SmithNForge.recipes.IForgedRecipe;
import com.doc.SmithNForge.recipes.UnlocalizedAbilityNames;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Flight implements IForgedRecipe {

	@Override
	public ItemStack[] requiredItems() {
		
		return new ItemStack[] {
				
				new ItemStack(Items.blaze_rod),
				new ItemStack(Items.feather),
				new ItemStack(Items.arrow),
				new ItemStack(Items.golden_apple),
				new ItemStack(Blocks.diamond_block),
				new ItemStack(Items.ender_eye)
		};
	}

	@Override
	public int requiredHeat() {
		
		return 0;
	}

	@Override
	public int requiredRedstone() {
		
		return 0;
	}

	@Override
	public int requiredIron() {
		
		return 0;
	}

	@Override
	public int requiredGold() {
		
		return 3;
	}

	@Override
	public int requiredDiamond() {
		
		return 4;
	}

	@Override
	public int requiredEmerald() {
		
		return 1;
	}

	@Override
	public int requiredStar() {
		
		return 1;
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
		
		return UnlocalizedAbilityNames.flight;
	}

}
