package com.doc.SmithNForge.proxy;

import com.doc.SmithNForge.listeners.EventHandlerClient;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class SNFClientProxy extends SNFCommonProxy  {

	@Override
    public void preInit(FMLPreInitializationEvent e) {
		
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {
    	
        super.init(e);
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
    	
        super.postInit(e);
    }
}
