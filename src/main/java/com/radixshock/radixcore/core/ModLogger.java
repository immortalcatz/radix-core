/*******************************************************************************
 * ModLogger.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.core;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;

/**
 * Logger that has the ability to log any sort of object or even exceptions to the FMLLog.
 */
public final class ModLogger
{
	private final IEnforcedCore mod;
	private final Logger logger = FMLLog.getLogger();

	/**
	 * Constructor
	 * 
	 * @param mod The logger's owner IMod.
	 */
	public ModLogger(IEnforcedCore mod)
	{
		this.mod = mod;
	}

	/**
	 * Writes the specified object's string representation to System.out.
	 * 
	 * @param objects The object(s) to write to System.out.
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
				((Throwable) objects[0]).printStackTrace();
			}

			logger.info(mod.getLongModName() + " " + side.toString() + ": " + objectsToString);
		}

		catch (final NullPointerException e) // Object provided is null.
		{
			logger.info(mod.getLongModName() + " " + side.toString() + ": null");
		}
	}
}
