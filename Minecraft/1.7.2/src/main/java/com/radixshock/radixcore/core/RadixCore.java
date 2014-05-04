/*******************************************************************************
 * RadixCore.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.common.MinecraftForge;

import com.radixshock.radixcore.command.CommandGetModProperty;
import com.radixshock.radixcore.command.CommandListModProperties;
import com.radixshock.radixcore.command.CommandSetModProperty;
import com.radixshock.radixcore.lang.LanguageLoader;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The core of the RadixCore mod API.
 */
@Mod(modid="radixcore", name="RadixCore", version="1.1.0")
public class RadixCore extends UnenforcedCore
{
	@Instance("radixcore")
	private static RadixCore instance;
	private ModLogger logger;

	/** The current working directory. The .minecraft folder. */
	public String runningDirectory;

	/** A list of mods registered with RadixCore. */
	public static final List<IEnforcedCore> registeredMods = new ArrayList<IEnforcedCore>();

	/**
	 * Handles the FMLPreInitialization event and passes it to all loaded mods after
	 * initializing their proxies, items, and blocks.
	 * 
	 * @param 	event	The event.
	 */
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		instance = this;
		logger = new ModLogger(this);
		runningDirectory = System.getProperty("user.dir");

		logger.log("RadixCore version " + getVersion() + " is running from " + runningDirectory);

		try
		{
			FMLCommonHandler.instance().bus().register(new RadixEvents());
			MinecraftForge.EVENT_BUS.register(new RadixEvents());

			for (IEnforcedCore mod : registeredMods)
			{
				mod.preInit(event);
				mod.initializeProxy();
				mod.initializeItems();
				mod.initializeBlocks();

				if (mod.getEventHookClass() != null)
				{
					FMLCommonHandler.instance().bus().register(mod.getEventHookClass().newInstance());
					MinecraftForge.EVENT_BUS.register(mod.getEventHookClass().newInstance());
				}

				final LanguageLoader modLanguageLoader = mod.getLanguageLoader();

				if (modLanguageLoader != null)
				{
					modLanguageLoader.loadLanguage();
				}
			}
		}

		catch (Exception e)
		{
			quitWithException("Unexpected exception during pre-initialization.", e);
		}
	}

	/**
	 * Handles the FMLInitializationEvent and passes it to all registered mods.
	 * Initializes recipes, smeltings, achievements. entities, and the network on all mods.
	 * 
	 * @param 	event	The event.	
	 */
	@EventHandler
	public void onInit(FMLInitializationEvent event)
	{
		for (IEnforcedCore mod : registeredMods)
		{
			mod.init(event);
			mod.initializeRecipes();
			mod.initializeSmeltings();
			mod.initializeAchievements();
			mod.initializeEntities();
			mod.initializeNetwork();
		}
	}

	/**
	 * Handles the FMLPostInitializationEvent and passes it to all registered mods.
	 *
	 * @param 	event	The event.
	 */
	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event)
	{
		for (IEnforcedCore mod : registeredMods)
		{
			mod.postInit(event);
		}
	}

	/**
	 * Passes the FMLServerStartingEvent to all registered mods.
	 * 
	 * @param 	event	The event.
	 */
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		for (IEnforcedCore mod : registeredMods)
		{
			if (mod.getSetModPropertyCommandEnabled())
			{
				event.registerServerCommand(new CommandSetModProperty(mod));
			}
			
			if (mod.getGetModPropertyCommandEnabled())
			{
				event.registerServerCommand(new CommandGetModProperty(mod));
			}
			
			if (mod.getListModPropertiesCommandEnabled())
			{
				event.registerServerCommand(new CommandListModProperties(mod));
			}
			
			mod.serverStarting(event);
			mod.initializeCommands(event);
		}
	}

	/**
	 * Passes the FMLServerStoppingEvent to all registered mods.
	 * 
	 * @param 	event	The event.
	 */
	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event)
	{
		for (IEnforcedCore mod : registeredMods)
		{
			mod.serverStopping(event);
		}
	}

	/**
	 * @return	An instance of RadixCore.
	 */
	public static RadixCore getInstance()
	{
		return instance;
	}

	/**
	 * Stops the game and writes the error to the Forge crash log.
	 * 
	 * @param 	description	A string providing a short description of the problem.
	 */
	@SideOnly(Side.CLIENT)
	public void quitWithDescription(String description)
	{
		final Writer stackTrace = new StringWriter();
		final Exception exception = new Exception();

		PrintWriter stackTraceWriter = new PrintWriter(stackTrace);
		exception.printStackTrace(stackTraceWriter);

		logger.log(Level.FINER, "Radix Core: An exception occurred.\n>>>>>" + description + "<<<<<\n" + stackTrace.toString());
		System.out.println("Radix Core: An exception occurred.\n>>>>>" + description + "<<<<<\n" + stackTrace.toString());

		final CrashReport crashReport = new CrashReport("RADIX CORE: " + description, exception);
		Minecraft.getMinecraft().crashed(crashReport);
		Minecraft.getMinecraft().displayCrashReport(crashReport);
	}

	/**
	 * Stops the game and writes the error to the Forge crash log.
	 * 
	 * @param 	description	A string providing a short description of the problem.
	 * @param 	exception	The exception that caused this method to be called.
	 */
	@SideOnly(Side.CLIENT)
	public void quitWithException(String description, Exception exception)
	{
		final Writer stackTrace = new StringWriter();
		final PrintWriter stackTraceWriter = new PrintWriter(stackTrace);
		exception.printStackTrace(stackTraceWriter);

		logger.log(Level.FINER, "Radix Core: An exception occurred.\n>>>>>" + description + "<<<<<\n" + stackTrace.toString());
		System.out.println("Radix Core: An exception occurred.\n>>>>>" + description + "<<<<<\n" + stackTrace.toString());

		final CrashReport crashReport = new CrashReport("RADIX CORE: " + description, exception);
		Minecraft.getMinecraft().crashed(crashReport);
		Minecraft.getMinecraft().displayCrashReport(crashReport);
	}
	
	public String getShortModName() 
	{
		return getLongModName();
	}
	
	public String getLongModName() 
	{
		return "RadixCore";
	}
	
	public String getVersion() 
	{
		return "1.1.0";
	}
	
	public boolean getChecksForUpdates() 
	{
		return true;
	}
	
	public String getUpdateURL() 
	{
		return "http://pastebin.com/raw.php?i=fWd8huwd";
	}

	public String getRedirectURL() 
	{
		return "http://goo.gl/cRzaJ0";
	}

	public ModLogger getLogger() 
	{
		return logger;
	}
}
