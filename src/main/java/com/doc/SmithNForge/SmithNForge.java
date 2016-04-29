package com.doc.SmithNForge;

import com.doc.SmithNForge.blocks.BlockForge;
import com.doc.SmithNForge.blocks.BlockSmith;
import com.doc.SmithNForge.blocks.tiles.TileEntityForge;
import com.doc.SmithNForge.blocks.tiles.TileEntitySmith;
import com.doc.SmithNForge.items.ItemBellows;
import com.doc.SmithNForge.items.ItemHammer;
import com.doc.SmithNForge.proxy.SNFCommonProxy;
import com.doc.SmithNForge.recipes.RecipeRegister;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = SmithNForge.MODID, version = SmithNForge.VERSION)
public class SmithNForge {
	
	@SidedProxy(clientSide="com.doc.SmithNForge.proxy.SNFClientProxy", serverSide="com.doc.SmithNForge.proxy.SNFCommonProxy")
	public static SNFCommonProxy proxy;
	
    public static final String MODID = "smithnforge";
    public static final String VERSION = "0.0.1";
    
    //blocks
    BlockForge blockForge;
    BlockSmith blockSmith;
    
    //items
    ItemHammer itemHammer;
    ItemBellows itemBellows;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	
        proxy.preInit(e);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
		
    	proxy.init(event);
    	
    	GameRegistry.registerTileEntity(TileEntitySmith.class, UnlocalizedNames.smithTile);
    	GameRegistry.registerTileEntity(TileEntityForge.class, UnlocalizedNames.forgeTile);
    	
    	blockForge = new BlockForge();
    	blockSmith = new BlockSmith();
    	
    	itemHammer = new ItemHammer();
    	itemBellows = new ItemBellows();
    	
    	GameRegistry.registerBlock(blockForge, UnlocalizedNames.forge);
    	GameRegistry.registerBlock(blockSmith, UnlocalizedNames.smith);
    	
    	GameRegistry.registerItem(itemHammer, UnlocalizedNames.hammer);
    	GameRegistry.registerItem(itemBellows, UnlocalizedNames.bellows);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	
        proxy.postInit(e);
        
        RecipeRegister.register();
    }
}
