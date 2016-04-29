package com.doc.SmithNForge.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.doc.SmithNForge.recipes.recipe.*;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeRegister {

	public static Map<ItemStack, IForgedRecipe> recipes = new HashMap<ItemStack, IForgedRecipe>();

	public static void register() {
		
		addRecipe(new JumpBoost().requiredItems()[0], new JumpBoost());
		addRecipe(new SpeedBoost().requiredItems()[0], new SpeedBoost());
	}
	
	public static IForgedRecipe getRecipe(ItemStack iStack) {
		
		if (iStack == null)
			return null;
		
		for (Entry<ItemStack, IForgedRecipe> recipe : recipes.entrySet()) {
			
			if (iStack.getItem() == (recipe.getValue().requiredItems()[0]).getItem())
				return recipe.getValue();
		}
		
		return null;
	}
	
	public static boolean containsRequiredItems(ItemStack[] itemsToAdd) {
		
		ItemStack keyedItem = itemsToAdd[0];
		IForgedRecipe recipe = getRecipe(keyedItem);
		
		if (keyedItem == null || recipe == null)
			return false;
		
		ItemStack[] whatINeed = recipe.requiredItems().clone();
		for (int i = 0; i < whatINeed.length; i++) {
			for (int j = 0; j < itemsToAdd.length; j++) {
				
				if (whatINeed[i] != null && itemsToAdd[j] != null)
					if (whatINeed[i].getItem() == itemsToAdd[j].getItem())
						whatINeed[i] = null;
			}
		}
		
		for (ItemStack iStack : whatINeed) {
			if (iStack != null)
				return false;
		}
		
		return true;
	}
	
	public static boolean containsRequiredImbued(ItemStack toForge, ItemStack keyedItem) {

		IForgedRecipe recipe = getRecipe(keyedItem);
		if (toForge == null || toForge.getTagCompound() == null || recipe == null) {

			return false;
		}

		NBTTagCompound cmp = toForge.getTagCompound();
		int currentVal = -1;


		if (recipe.requiredRedstone() > 0) {
			if (cmp.hasKey(new ItemStack(Items.redstone, 1).getDisplayName())) {
				if (cmp.getInteger(new ItemStack(Items.redstone, 1).getDisplayName()) < recipe.requiredRedstone())
					return false;
				
			} else
				return false;
		} 
		
		if (recipe.requiredIron() > 0) {
			if (cmp.hasKey(new ItemStack(Items.iron_ingot, 1).getDisplayName())) {
				if (cmp.getInteger(new ItemStack(Items.iron_ingot, 1).getDisplayName()) < recipe.requiredIron())
					return false;
				
			} else
				return false;
		} else if (recipe.requiredGold() > 0) {
			if (cmp.hasKey(new ItemStack(Items.gold_ingot, 1).getDisplayName())) {
				if (cmp.getInteger(new ItemStack(Items.gold_ingot, 1).getDisplayName()) < recipe.requiredGold())
					return false;
				
			} else
				return false;
		} 
		
		if (recipe.requiredDiamond() > 0) {
			if (cmp.hasKey(new ItemStack(Items.diamond, 1).getDisplayName())) {
				if (cmp.getInteger(new ItemStack(Items.diamond, 1).getDisplayName()) < recipe.requiredDiamond())
					return false;
				
			} else
				return false;
		} 
		if (recipe.requiredEmerald() > 0) {
			if (cmp.hasKey(new ItemStack(Items.emerald, 1).getDisplayName())) {
				if (cmp.getInteger(new ItemStack(Items.emerald, 1).getDisplayName()) < recipe.requiredEmerald())
					return false;
				
			} else
				return false;
		} 
		if (recipe.requiredStar() > 0) {
			if (cmp.hasKey(new ItemStack(Items.nether_star, 1).getDisplayName())) {
				if (cmp.getInteger(new ItemStack(Items.iron_ingot, 1).getDisplayName()) < recipe.requiredStar())
					return false;
				
			} else
				return false;
		}


		return true;
	}

	public static List getRecipeStack(ItemStack iStack) {

		if (recipes.containsKey(iStack)) {

			return Arrays.asList(getRecipe(iStack).requiredItems());
		}

		return new ArrayList<ItemStack>();
	}

	public static List getRecipeStack(IForgedRecipe recipe) {

		return Arrays.asList(recipe.requiredItems());
	}

	public static boolean addRecipe(ItemStack keyedItem, IForgedRecipe recipe) {

		if (recipes.containsKey(keyedItem))
			return false;

		recipes.put(keyedItem, recipe);

		return true;
	}
}
