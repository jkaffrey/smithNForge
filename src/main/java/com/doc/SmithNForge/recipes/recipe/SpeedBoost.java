package com.doc.SmithNForge.recipes.recipe;

import com.doc.SmithNForge.UnlocalizedNames;
import com.doc.SmithNForge.recipes.IForgedRecipe;
import com.doc.SmithNForge.recipes.UnlocalizedAbilityNames;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class SpeedBoost implements IForgedRecipe {

	@Override
	public ItemStack[] requiredItems() {
		
		return new ItemStack[] {
				new ItemStack(Items.sugar),
				new ItemStack(Items.bow),
				new ItemStack(Items.arrow)
		};
	}

	@Override
	public int requiredHeat() {
		
		return 0;
	}

	@Override
	public int requiredRedstone() {
		
		return 1;
	}

	@Override
	public int requiredIron() {
		
		return 1;
	}

	@Override
	public int requiredGold() {
		
		return 0;
	}

	@Override
	public int requiredDiamond() {
		
		return 1;
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
	public int outputHeat() {
		
		return 0;
	}

	@Override
	public String effectName() {
		
		return UnlocalizedAbilityNames.speedBoost;
	}

	@Override
	public int requiredTime() {
		// TODO Auto-generated method stub
		return 0;
	}

}
