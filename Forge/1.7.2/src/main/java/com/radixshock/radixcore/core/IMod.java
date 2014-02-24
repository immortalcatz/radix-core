package com.radixshock.radixcore.core;

import com.radixshock.radixcore.lang.ILanguageLoaderHook;
import com.radixshock.radixcore.lang.ILanguageParser;
import com.radixshock.radixcore.lang.LanguageLoader;
import com.radixshock.radixcore.network.AbstractPacketCodec;
import com.radixshock.radixcore.network.AbstractPacketHandler;
import com.radixshock.radixcore.network.PacketPipeline;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public interface IMod 
{
	void preInit(FMLPreInitializationEvent event);
	
	void init(FMLInitializationEvent event);
	
	void postInit(FMLPostInitializationEvent event);

	void serverStarting(FMLServerStartingEvent event);
	
	void serverStopping(FMLServerStoppingEvent event);
	
	void initializeProxy();
	
	void initializeItems();
	
	void initializeBlocks();
	
	void initializeRecipes();
	
	void initializeSmeltings();
	
	void initializeAchievements();
	
	void initializeEntities();
	
	void initializeNetwork();

	String getShortModName();
	
	String getLongModName();
	
	String getVersion();
	
	String getUpdateURL();
	
	String getRedirectURL();
	
	ModLogger getLogger();
	
	AbstractPacketCodec getPacketCodec();
	
	AbstractPacketHandler getPacketHandler();
	
	PacketPipeline getPacketPipeline();
	
	Class getPacketTypeClass();
	
	Class getEventHookClass();
	
	LanguageLoader getLanguageLoader();
	
	ILanguageParser getLanguageParser();
	
	ILanguageLoaderHook getLanguageLoaderHook();
	
	boolean getLanguageLoaded();
	
	void setLanguageLoaded(boolean value);
}