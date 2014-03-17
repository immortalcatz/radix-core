/*******************************************************************************
 * IMod.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.core;

import com.radixshock.radixcore.enums.EnumNetworkType;
import com.radixshock.radixcore.file.ModPropertiesManager;
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

/**
 * Allows a core mod class to be loaded by RadixCore. All methods are required to be 
 * inserted into the core class.
 */
public interface IEnforcedCore 
{
	/** 
	 * Passes the FMLPreInitializationEvent to your mod.
	 * @param	event The event.
	 */
	void preInit(FMLPreInitializationEvent event);
	
	/** 
	 * Passes the FMLInitializationEvent to your mod.
	 * @param	event The event. 
	 */
	void init(FMLInitializationEvent event);
	
	/** 
	 * Passes the FMLPostInitializationEvent to your mod.
	 * @param	event The event.
	 */
	void postInit(FMLPostInitializationEvent event);

	/** 
	 * Passes the FMLServerStartingEvent to your mod.
	 * @param	event The event.
	 */
	void serverStarting(FMLServerStartingEvent event);
	
	/**
	 * Passes the FMLServerStoppingEvent to your mod.
	 * @param	event The event.
	 */
	void serverStopping(FMLServerStoppingEvent event);
	
	/**
	 * Initialize your proxy here.
	 */
	void initializeProxy();
	
	/**
	 * Initialize and register your items and creative tab(s) here. This is called automatically.
	 */
	void initializeItems();
	
	/**
	 * Initialize and register your blocks here. This is called automaticaly.
	 */
	void initializeBlocks();
	
	/**
	 * Register your recipes here. This is called automatically.
	 */
	void initializeRecipes();
	
	/**
	 * Register your smeltings here. This is called automatically.
	 */
	void initializeSmeltings();
	
	/**
	 * Initialize and register your achievements here. This is called automatically.
	 */
	void initializeAchievements();
	
	/**
	 * Register your mod entities here. This is called automatically.
	 */
	void initializeEntities();
	
	/**
	 * Initialize your mod's networking system components here. 
	 * Always initialize your packet pipeline, unless you're not using a networking system. <p>
	 * 
	 * If using the Legacy networking type, initialize your codec, handler, and add channels as well as register the packet class. <p>
	 * 
	 * If using the SelfContained networking type, do all of the above while registering all packet classes you've defined yourself <p>
	 * <b>instead of<b> the Packet class.
	 */
	void initializeNetwork();

	/**
	 * @return	Your mod's short name. This should be the name of your assets folder, and will also become the name
	 * 		   	of your mod's folder that will be created in config. For MCA, it is "MCA".
	 */
	String getShortModName();
	
	/**
	 * @return	Your mod's long name. This is used when logging information to the ModLogger.
	 */
	String getLongModName();
	
	/**
	 * @return	Your mod's current version.
	 */
	String getVersion();
	
	/**
	 * @return	True if you want your mod to check for updates. False if otherwise.
	 */
	boolean getChecksForUpdates();
	
	/**
	 * @return	The URL containing your mod's update data. The URL should be raw data, no HTML.
	 * 		   	The first line of the update data should be the Minecraft versions your mod is currently available for.
	 * 		   	The second and last line of the update data should be your mod's current version.
	 */
	String getUpdateURL();
	
	/**
	 * @return	The URL to display when an update is available.
	 */
	String getRedirectURL();
	
	/**
	 * @return	Return an instance of your mod's logger here.
	 */
	ModLogger getLogger();
	
	/**
	 * @return	The method you will be using to interface with the networking system.
	 */
	EnumNetworkType getNetworkSystemType();
	
	/**
	 * @return	Return an instance of your mod's packet codec here. Null if you don't need one.
	 */
	AbstractPacketCodec getPacketCodec();
	
	/**
	 * @return	Return an instance of your mod's packet handler here. Null if you don't need one.
	 */
	AbstractPacketHandler getPacketHandler();
	
	/**
	 * @return	Return an instance of your mod's packet pipeline here. Null if you don't need one.
	 */
	PacketPipeline getPacketPipeline();
	
	/**
	 * @return	Return the enum type that contains the types of packets you are going to send. Null if you don't do this.
	 */
	Class getPacketTypeClass();
	
	/**
	 * @return	An instance of your mod's ModPropertiesManager. Null if you don't use one.
	 */
	ModPropertiesManager getModPropertiesManager();
	
	/**
	 * @return	True if RadixCore should define the SetModProperty command for your mod.
	 * 			For security reasons, this command can only be used from the console on a server.
	 */
	boolean getSetModPropertyCommandEnabled();
	
	/**
	 * @return	True if RadixCore should define the GetModProperty command for your mod.
	 */
	boolean getGetModPropertyCommandEnabled();
	
	/**
	 * @return	True if RadixCore should define the ListModProperties command for your mod.
	 */
	boolean getListModPropertiesCommandEnabled();
	
	/**
	 * @return	The prefix of your mod's commands, if you use it. This will be applied to the mod property commands.
	 * MCA's prefix is "mca."
	 */
	String getPropertyCommandPrefix();
	
	/**
	 * @return	Return the class that contains your mod's event hooks here. Null if you don't have any event hooks.
	 */
	Class getEventHookClass();
	
	/**
	 * @return	Return an instance of your mod's language loader here. Null if you don't need one.
	 */
	LanguageLoader getLanguageLoader();
	
	/**
	 * @return	Return an instance of your mod's language parser here. Null if you don't have one.
	 */
	ILanguageParser getLanguageParser();
	
	/**
	 * @return	Return an instance of your mod's langauge loader hook here. Null if you don't have one.
	 */
	ILanguageLoaderHook getLanguageLoaderHook();
	
	/**
	 * @return	Return a boolean that can be used to keep track of your language loader's status here.
	 * 			For MCA, this is <code>languageLoaded</code>
	 */
	boolean getLanguageLoaded();
	
	/**
	 * Set your <code>languageLoaded</code> variable to the provided value here.
	 * 
	 * @param 	value	The value to set <code>languageLoaded</code> to.
	 */
	void setLanguageLoaded(boolean value);
}