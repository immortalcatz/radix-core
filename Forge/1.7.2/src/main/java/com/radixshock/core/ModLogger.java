package com.radixshock.core;

import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;

public final class ModLogger 
{
	private IMod mod;
	private final Logger logger = FMLLog.getLogger();
	
	public ModLogger(IMod mod)
	{
		this.mod = mod;
	}
	
	/**
	 * Writes the specified object's string representation to System.out.
	 * 
	 * @param 	objects	The object(s) to write to System.out.
	 */
	public void log(Object... objects)
	{
		final Side side = FMLCommonHandler.instance().getEffectiveSide();
		String objectsToString = "";

		try
		{
			for (int index = 0; index < objects.length; index++)
			{
				final boolean useComma = index > 0;
				objectsToString = useComma ? objectsToString + ", " + objects[index].toString() : objectsToString + objects[index].toString();
			}

			if (objects[0] instanceof Throwable)
			{
				((Throwable)objects[0]).printStackTrace();
			}

			logger.info(mod.getLongModName() + " " + side.toString() + ": " + objectsToString);
			
			final MinecraftServer server = MinecraftServer.getServer();

			if (server != null && server.isDedicatedServer())
			{
				MinecraftServer.getServer().logInfo(mod.getShortModName() + ": " + objectsToString);
			}
		}

		catch (NullPointerException e) //Object provided is null.
		{
			logger.info(mod.getLongModName() + " " + side.toString() + ": null");

			final MinecraftServer server = MinecraftServer.getServer();

			if (server != null &&  server.isDedicatedServer())
			{
				MinecraftServer.getServer().logDebug(mod.getLongModName() + " " + side.toString() + ": null");
			}
		}
	}
}
