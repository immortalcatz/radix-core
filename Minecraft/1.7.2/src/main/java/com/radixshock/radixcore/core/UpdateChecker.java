/*******************************************************************************
 * UpdateHandler.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import com.radixshock.radixcore.constant.Font;

/**
 * Checks for outdated versions and updates.
 */
public final class UpdateChecker implements Runnable
{	
	private IMod mod;
	private ICommandSender commandSender;
	private boolean hasCheckedForUpdates;
	private String rawUpdateInfoURL;
	private String updateRedirectionURL;

	/**
	 * Constructor
	 * 
	 * @param mod					The update checker's owner mod.
	 * @param commandSender			The player checking for updates.
	 * @param rawUpdateInfoURL		The URL containing raw update information.
	 * @param updateRedirectionURL	The URL the player should be redirected to.
	 */
	public UpdateChecker(IMod mod, ICommandSender commandSender, String rawUpdateInfoURL, String updateRedirectionURL)
	{
		this.mod = mod;
		this.commandSender = commandSender;
		this.rawUpdateInfoURL = rawUpdateInfoURL;
		this.updateRedirectionURL = updateRedirectionURL;
	}

	@Override
	public void run()
	{
		mod.getLogger().log("Checking for updates...");

		try
		{
			if (!hasCheckedForUpdates)
			{
				final URL url = new URL(rawUpdateInfoURL);
				final Scanner scanner = new Scanner(url.openStream());

				String validGameVersions = scanner.nextLine();
				String mostRecentVersion = scanner.nextLine();

				hasCheckedForUpdates = true;

				if (!mostRecentVersion.equals(mod.getVersion()))
				{
					final String messageUpdateVersion = Font.Color.DARKGREEN + mod.getShortModName() + " " + mostRecentVersion + 
							Font.Color.YELLOW + " for " + 
							Font.Color.DARKGREEN + "Minecraft " + validGameVersions + 
							Font.Color.YELLOW + " is available.";

					final String messageUpdateURL = 
							Font.Color.YELLOW + "See " + 
									Font.Color.BLUE   + Font.Format.ITALIC + updateRedirectionURL + " " + Font.Format.RESET + 
									Font.Color.YELLOW + "to download.";

					commandSender.addChatMessage(new ChatComponentText(messageUpdateVersion));
					commandSender.addChatMessage(new ChatComponentText(messageUpdateURL));
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
}
