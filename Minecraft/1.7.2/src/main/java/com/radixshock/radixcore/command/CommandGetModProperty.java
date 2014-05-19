/*******************************************************************************
 * CommandGetModProperty.java
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
import net.minecraft.util.ChatComponentText;

import com.radixshock.radixcore.constant.Font.Color;
import com.radixshock.radixcore.constant.Font.Format;
import com.radixshock.radixcore.core.IEnforcedCore;
import com.radixshock.radixcore.file.ModPropertiesManager;

/**
 * Defines the GetModProperty command.
 */
public class CommandGetModProperty extends CommandBase
{
	private final IEnforcedCore	mod;

	public CommandGetModProperty(IEnforcedCore mod)
	{
		this.mod = mod;
	}

	@Override
	public String getCommandName()
	{
		return mod.getPropertyCommandPrefix() + "getmodproperty";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return mod.getPropertyCommandPrefix() + "getmodproperty <property name>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] arguments)
	{
		if (arguments.length == 1)
		{
			try
			{
				final String propertyName = arguments[0];
				final ModPropertiesManager modPropertiesManager = mod.getModPropertiesManager();
				final Class modPropertiesListClass = mod.getModPropertiesManager().modPropertiesInstance.getClass();

				for (final Field field : modPropertiesListClass.getDeclaredFields())
				{
					if (field.getName().toString().equalsIgnoreCase(propertyName))
					{
						sender.addChatMessage(new ChatComponentText(Color.YELLOW + "Value of property " + field.getName() + " is: " + Format.RESET + field.get(modPropertiesManager.modPropertiesInstance)));
						return;
					}
				}

				sender.addChatMessage(new ChatComponentText(Color.RED + "Property not found for " + mod.getLongModName() + ": " + propertyName));
			}

			catch (final Throwable e)
			{
				sender.addChatMessage(new ChatComponentText(Color.RED + "Error getting mod property: " + e.getMessage()));
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
