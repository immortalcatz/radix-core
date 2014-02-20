package com.radixshock.core;

import com.radixshock.network.AbstractPacketCodec;
import com.radixshock.network.AbstractPacketHandler;
import com.radixshock.network.PacketPipeline;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public interface IMod 
{
	void preInit();
	
	void init();
	
	void postInit();

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
}