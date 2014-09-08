/*******************************************************************************
 * CommandSetModProperty.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.command;

import java.lang.reflect.Field;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import com.radixshock.radixcore.constant.Font.Color;
import com.radixshock.radixcore.core.IEnforcedCore;
import com.radixshock.radixcore.file.ModPropertiesManager;

import cpw.mods.fml.common.FMLCommonHandler;

/**
 * Defines the SetModProperty command.
 */
public class CommandSetModProperty extends CommandBase
{
	private final IEnforcedCore mod;

	public CommandSetModProperty(IEnforcedCore mod)
	{
		this.mod = mod;
	}

	@Override
	public String getCommandName()
	{
		return mod.getPropertyCommandPrefix() + "setmodproperty";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return mod.getPropertyCommandPrefix() + "setmodproperty <property name> <property value>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] arguments)
	{
		if (FMLCommonHandler.instance().getSide().isServer())
		{
			if (MinecraftServer.getServer().isDedicatedServer())
			{
				sender.addChatMessage(new ChatComponentText(Color.RED + "You must use this command from the server console."));
				return;
			}
		}

		if (arguments.length == 2)
		{
			try
			{
				final String propertyName = arguments[0];
				final String propertyValue = arguments[1];
				final ModPropertiesManager modPropertiesManager = mod.getModPropertiesManager();
				final Class modPropertiesListClass = mod.getModPropertiesManager().modPropertiesInstance.getClass();

				for (final Field field : modPropertiesListClass.getDeclaredFields())
				{
					if (field.getName().toString().equalsIgnoreCase(propertyName))
					{
						if (field.getType().toString().equals("int"))
						{
							field.set(modPropertiesManager.modPropertiesInstance, Integer.parseInt(propertyValue));
						}

						else if (field.getType().toString().equals("boolean"))
						{
							field.set(modPropertiesManager.modPropertiesInstance, Boolean.parseBoolean(propertyValue));
						}

						else
						{
							field.set(modPropertiesManager.modPropertiesInstance, field.getType().cast(propertyValue));
						}

						sender.addChatMessage(new ChatComponentText(Color.GREEN + field.getName() + " set to " + propertyValue + "."));
						modPropertiesManager.saveModProperties();
						return;
					}
				}

				sender.addChatMessage(new ChatComponentText(Color.RED + "Property not found for " + mod.getLongModName() + ": " + propertyName));
			}

			catch (final Throwable e)
			{
				sender.addChatMessage(new ChatComponentText(Color.RED + "Error setting mod property: " + e.getMessage()));
			}
		}

		else
		{
			throw new WrongUsageException(getCommandUsage(sender));
		}
	}

	@Override
	public int compareTo(Object arg0)
	{
		return 0;
	}
}
