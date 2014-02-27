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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.radixshock.radixcore.lang.ILanguageLoaderHook;
import com.radixshock.radixcore.lang.ILanguageParser;
import com.radixshock.radixcore.lang.LanguageLoader;
import com.radixshock.radixcore.network.AbstractPacketCodec;
import com.radixshock.radixcore.network.AbstractPacketHandler;
import com.radixshock.radixcore.network.PacketPipeline;

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
@Mod(modid="radixcore", name="RadixCore", version="1.0.0")
public class RadixCore implements IMod
{
	@Instance("radixcore")
	private static RadixCore instance;
	private ModLogger logger;

	/** The current working directory. The .minecraft folder. */
	public String runningDirectory;
	
	/** A list of mods registered with RadixCore. */
	public static final List<IMod> registeredMods = new ArrayList<IMod>();

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
			
			for (IMod mod : registeredMods)
			{
				final LanguageLoader modLanguageLoader = mod.getLanguageLoader();
				getLogger().log("Pre-initializing " + mod.getLongModName() + "...");

				mod.preInit(event);
				mod.initializeProxy();
				mod.initializeItems();
				mod.initializeBlocks();
				
				FMLCommonHandler.instance().bus().register(mod.getEventHookClass().newInstance());
				MinecraftForge.EVENT_BUS.register(mod.getEventHookClass().newInstance());
				
				if (modLanguageLoader != null)
				{
					modLanguageLoader.loadLanguage();
				}
			}
		}

		catch (Exception e)
		{
			quitWithException("Exception while registering event hook class.", e);
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
		for (IMod mod : registeredMods)
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
		for (IMod mod : registeredMods)
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
		for (IMod mod : registeredMods)
		{
			mod.serverStarting(event);
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
		for (IMod mod : registeredMods)
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
	
	@Override
	public void preInit(FMLPreInitializationEvent event) { throw new NotImplementedException(); }

	@Override
	public void init(FMLInitializationEvent event) { throw new NotImplementedException(); }

	@Override
	public void postInit(FMLPostInitializationEvent event) { throw new NotImplementedException(); }

	@Override
	public void serverStarting(FMLServerStartingEvent event) { throw new NotImplementedException(); }

	@Override
	public void serverStopping(FMLServerStoppingEvent event) { throw new NotImplementedException(); }

	@Override
	public String getShortModName() 
	{
		return getLongModName();
	}

	@Override
	public String getLongModName() 
	{
		return "RadixCore";
	}

	@Override
	public String getVersion() 
	{
		return "1.0.0";
	}

	@Override
	public boolean getChecksForUpdates() 
	{
		return true;
	}

	@Override
	public String getUpdateURL() 
	{
		return "http://pastebin.com/raw.php?i=fWd8huwd";
	}

	@Override
	public String getRedirectURL() 
	{
		return "{REDIR}";
	}

	@Override
	public ModLogger getLogger() 
	{
		return logger;
	}

	@Override
	public AbstractPacketCodec getPacketCodec() 
	{
		return null;
	}

	@Override
	public AbstractPacketHandler getPacketHandler() 
	{
		return null;
	}

	@Override
	public PacketPipeline getPacketPipeline() 
	{
		return null;
	}

	@Override
	public Class getPacketTypeClass() 
	{
		return null;
	}

	@Override
	public Class getEventHookClass()
	{
		return null;
	}

	@Override
	public void initializeProxy() { throw new NotImplementedException(); }

	@Override
	public void initializeItems() { throw new NotImplementedException(); }

	@Override
	public void initializeBlocks() { throw new NotImplementedException(); }

	@Override
	public void initializeRecipes() { throw new NotImplementedException(); }

	@Override
	public void initializeSmeltings() { throw new NotImplementedException(); }

	@Override
	public void initializeAchievements() { throw new NotImplementedException(); }

	@Override
	public void initializeEntities() { throw new NotImplementedException(); }

	@Override
	public void initializeNetwork() { throw new NotImplementedException(); }

	@Override
	public boolean getLanguageLoaded() { return false; }

	@Override
	public void setLanguageLoaded(boolean value) { throw new NotImplementedException(); }

	@Override
	public ILanguageLoaderHook getLanguageLoaderHook() { return null; }

	@Override
	public LanguageLoader getLanguageLoader() { return null; }

	@Override
	public ILanguageParser getLanguageParser() { return null; }
}
