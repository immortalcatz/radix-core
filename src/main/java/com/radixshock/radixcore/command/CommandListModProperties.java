/*******************************************************************************
 * CommandListModProperties.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.command;

import java.lang.reflect.Field;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import com.radixshock.radixcore.constant.Font.Color;
import com.radixshock.radixcore.constant.Font.Format;
import com.radixshock.radixcore.core.IEnforcedCore;
import com.radixshock.radixcore.file.ModPropertiesManager;

/**
 * Defines the ListModProperties command.
 */
public class CommandListModProperties extends CommandBase
{
	private final IEnforcedCore mod;

	public CommandListModProperties(IEnforcedCore mod)
	{
		this.mod = mod;
	}

	@Override
	public String getCommandName()
	{
		return mod.getPropertyCommandPrefix() + "listmodproperties";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return mod.getPropertyCommandPrefix() + "listmodproperties";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] arguments)
	{
		try
		{
			final ModPropertiesManager modPropertiesManager = mod.getModPropertiesManager();
			final Class modPropertiesListClass = mod.getModPropertiesManager().modPropertiesInstance.getClass();

			sender.addChatMessage(new ChatComponentText(""));
			sender.addChatMessage(new ChatComponentText(Color.GREEN + "--- " + mod.getLongModName() + " Properties" + " ---"));
			for (final Field field : modPropertiesListClass.getDeclaredFields())
			{
				sender.addChatMessage(new ChatComponentText(Color.YELLOW + field.getName() + Format.RESET + " = " + Color.WHITE + field.get(modPropertiesManager.modPropertiesInstance).toString()));
			}
		}

		catch (final Throwable e)
		{
			sender.addChatMessage(new ChatComponentText(Color.RED + "An unexpected error has occurred."));
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(Object arg0)
	{
		return 0;
	}
}
