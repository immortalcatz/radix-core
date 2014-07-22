/*******************************************************************************
 * UnenforcedCore.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.core;

import com.radixshock.radixcore.file.ModPropertiesManager;
import com.radixshock.radixcore.file.WorldPropertiesManager;
import com.radixshock.radixcore.lang.ILanguageLoaderHook;
import com.radixshock.radixcore.lang.ILanguageParser;
import com.radixshock.radixcore.lang.LanguageLoader;
import com.radixshock.radixcore.network.AbstractPacketHandler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

/**
 * Allows a core mod class to be loaded by RadixCore. You can override each
 * method as needed.
 */
public class UnenforcedCore implements IEnforcedCore
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{

	}

	@Override
	public void init(FMLInitializationEvent event)
	{

	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{

	}

	@Override
	public void serverStopping(FMLServerStoppingEvent event)
	{

	}

	@Override
	public void initializeProxy()
	{

	}

	@Override
	public void initializeItems()
	{

	}

	@Override
	public void initializeBlocks()
	{

	}

	@Override
	public void initializeRecipes()
	{

	}

	@Override
	public void initializeSmeltings()
	{

	}

	@Override
	public void initializeAchievements()
	{

	}

	@Override
	public void initializeEntities()
	{

	}

	@Override
	public void initializeNetwork()
	{

	}

	@Override
	public void initializeCommands(FMLServerStartingEvent event)
	{

	}

	@Override
	public String getShortModName()
	{
		return null;
	}

	@Override
	public String getLongModName()
	{
		return null;
	}

	@Override
	public String getVersion()
	{
		return null;
	}

	@Override
	public String getMinimumRadixCoreVersion() 
	{
		return null;
	}

	@Override
	public boolean getChecksForUpdates()
	{
		return false;
	}

	@Override
	public String getUpdateURL()
	{
		return null;
	}

	@Override
	public String getRedirectURL()
	{
		return null;
	}

	@Override
	public ModLogger getLogger()
	{
		return null;
	}

	@Override
	public Class getEventHookClass()
	{
		return null;
	}

	@Override
	public ModPropertiesManager getModPropertiesManager()
	{
		return null;
	}

	@Override
	public boolean getSetModPropertyCommandEnabled()
	{
		return false;
	}

	@Override
	public boolean getGetModPropertyCommandEnabled()
	{
		return false;
	}

	@Override
	public boolean getListModPropertiesCommandEnabled()
	{
		return false;
	}

	@Override
	public String getPropertyCommandPrefix()
	{
		return null;
	}

	@Override
	public LanguageLoader getLanguageLoader()
	{
		return null;
	}

	@Override
	public ILanguageParser getLanguageParser()
	{
		return null;
	}

	@Override
	public ILanguageLoaderHook getLanguageLoaderHook()
	{
		return null;
	}

	@Override
	public boolean getLanguageLoaded()
	{
		return false;
	}

	@Override
	public void setLanguageLoaded(boolean value)
	{

	}

	@Override
	public WorldPropertiesManager getWorldPropertiesManager() 
	{
		return null;
	}

	@Override
	public void onCreateNewWorldProperties(WorldPropertiesManager manager)
	{	
	}

	@Override
	public void onLoadWorldProperties(WorldPropertiesManager manager) 
	{	
	}

	@Override
	public void onSaveWorldProperties(WorldPropertiesManager manager) 
	{
	}

	@Override
	public AbstractPacketHandler getPacketHandler() 
	{
		return null;
	}

	@Override
	public void onUpdateWorldProperties(WorldPropertiesManager manager) 
	{
	}
}
