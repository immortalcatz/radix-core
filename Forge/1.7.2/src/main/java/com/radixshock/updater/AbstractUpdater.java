/*******************************************************************************
 * UpdateHandler.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.updater;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import net.minecraft.command.ICommandSender;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ChatComponentText;

import com.radixshock.constant.Font;
import com.radixshock.core.IMod;

/**
 * Checks for outdated versions and updates.
 */
public abstract class AbstractUpdater implements Runnable
{	
	private IMod mod;
	private NetHandlerPlayServer netHandler;
	private ICommandSender commandSender;
	private boolean hasCheckedForUpdates;
	
	/**
	 * Constructor used when a player logs in.
	 * 
	 * @param 	netHandler	The NetHandler of the player that just logged in.
	 */
	public AbstractUpdater(IMod mod, INetHandler netHandler)
	{
		this.mod = mod;
		this.netHandler = (NetHandlerPlayServer) netHandler;
	}

	/**
	 * Constructor used when a player issues the /mca.checkupdates on command.
	 * 
	 * @param 	commandSender	The player that sent the command.
	 */
	public AbstractUpdater(IMod mod, ICommandSender commandSender)
	{
		this.mod = mod;
		this.commandSender = commandSender;
	}

	@Override
	public void run()
	{
		try
		{
			if (!hasCheckedForUpdates)
			{
				final URL url = new URL(getRawUpdateInformationURL());
				final Scanner scanner = new Scanner(url.openStream());

				String validGameVersions = scanner.nextLine();
				String mostRecentVersion = scanner.nextLine();

				hasCheckedForUpdates = true;

				if (!mostRecentVersion.equals(mod.getVersion()))
				{
					final String messageUpdateVersion = Font.Color.DARKGREEN + mod.getShortModName() + mostRecentVersion + 
							Font.Color.YELLOW + " for " + 
							Font.Color.DARKGREEN + "Minecraft " + validGameVersions + 
							Font.Color.YELLOW + " is available.";
					
					final String messageUpdateURL = 
							Font.Color.YELLOW + "Click " + 
							Font.Color.BLUE   + Font.Format.ITALIC + getUpdateRedirectionURL() + " " + Font.Format.RESET + 
							Font.Color.YELLOW + "to download.";
					
					if (netHandler == null)
					{
						commandSender.addChatMessage(new ChatComponentText(messageUpdateVersion));
						commandSender.addChatMessage(new ChatComponentText(messageUpdateURL));
					}

					else if (commandSender == null)
					{
						netHandler.playerEntity.addChatMessage(new ChatComponentText(messageUpdateVersion));
						netHandler.playerEntity.addChatMessage(new ChatComponentText(messageUpdateURL));
					}
				}

				scanner.close();
			}
		}

		catch (MalformedURLException e)
		{
			mod.getLogger().log("Error checking for update.");
			mod.getLogger().log(e);
		}
		
		catch (IOException e)
		{
			mod.getLogger().log("Error checking for update.");
			mod.getLogger().log(e);
		}
	}
	
	public abstract String getRawUpdateInformationURL();
	
	public abstract String getUpdateRedirectionURL();
}
