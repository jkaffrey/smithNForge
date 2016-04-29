package com.doc.SmithNForge.recipes.recipe;

import com.doc.SmithNForge.recipes.IForgedRecipe;
import com.doc.SmithNForge.recipes.RecipeUnlocNames;
import com.doc.SmithNForge.recipes.UnlocalizedAbilityNames;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class JumpBoost implements IForgedRecipe {

	@Override
	public ItemStack[] requiredItems() {

		return new ItemStack[] {
				new ItemStack(Items.rabbit_foot, 1),
				new ItemStack(Items.bow, 1),
				new ItemStack(Items.apple, 1)
		};
	}

	@Override
	public int requiredHeat() {

		return 16;
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

		return 0;
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
	public int outputHeat() {

		return 0;
	}

	@Override
	public String effectName() {

		return UnlocalizedAbilityNames.jumpBoost;
	}

	@Override
	public int requiredTime() {
		// TODO Auto-generated method stub
		return 0;
	}

}
