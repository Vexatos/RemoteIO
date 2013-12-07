package com.dmillerw.remoteIO;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import com.dmillerw.remoteIO.block.BlockHandler;
import com.dmillerw.remoteIO.core.proxy.ISidedProxy;
import com.dmillerw.remoteIO.ender.storage.SharedRegistry;
import com.dmillerw.remoteIO.lib.ModInfo;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid=ModInfo.ID, name=ModInfo.NAME, version=ModInfo.VERSION, dependencies="after:EnderStorage")
@NetworkMod(channels={ModInfo.ID}, serverSideRequired=true, clientSideRequired=false)
public class RemoteIO {

	@Instance(ModInfo.ID)
	public static RemoteIO instance;
	
	@SidedProxy(serverSide=ModInfo.COMMON_PROXY, clientSide=ModInfo.CLIENT_PROXY)
	public static ISidedProxy proxy;

	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		BlockHandler.handleConfig(config);
		BlockHandler.initializeBlocks();
		
		MinecraftForge.EVENT_BUS.register(SharedRegistry.instance());
		
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		SharedRegistry.instance().onServerStarting(event);
	}
	
}
