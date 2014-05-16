/*******************************************************************************
 * UpdateChecker.java
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
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import com.radixshock.radixcore.constant.Font;

/**
 * Checks for outdated versions and updates.
 */
public final class UpdateChecker implements Runnable
{	
	private IEnforcedCore mod;
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
	public UpdateChecker(IEnforcedCore mod, ICommandSender commandSender, String rawUpdateInfoURL, String updateRedirectionURL)
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
							Font.Color.YELLOW + "Click " + 
									Font.Color.BLUE   + Font.Format.ITALIC + Font.Format.UNDERLINE + "here" + Font.Format.RESET +
									Font.Color.YELLOW + " to download the update for " + mod.getShortModName() + ".";

					commandSender.addChatMessage(new ChatComponentText(messageUpdateVersion));
					
					if (updateRedirectionURL.contains("current" + mod.getShortModName() + "=%"))
					{
						updateRedirectionURL = updateRedirectionURL.replace("current" + mod.getShortModName() + "=%", "current" + mod.getShortModName() + "=" + mostRecentVersion);
					}
					
					if (updateRedirectionURL.contains("currentMC=%"))
					{
						updateRedirectionURL = updateRedirectionURL.replace("currentMC=%", "currentMC=" + validGameVersions);
					}
					
					if (!updateRedirectionURL.contains("currentRadixCore=") && !updateRedirectionURL.contains("userRadixCore="))
					{
						updateRedirectionURL += "&userRadixCore=" + RadixCore.getInstance().getVersion();
						
						final URL radixUrl = new URL(RadixCore.getInstance().getUpdateURL());
						final Scanner radixScanner = new Scanner(radixUrl.openStream());

						String radixGameVersions = radixScanner.nextLine();
						String radixRecentVersion = radixScanner.nextLine();
						
						radixScanner.close();
						
						updateRedirectionURL += "&currentRadixCore=" + radixRecentVersion;
					}
					
					IChatComponent chatComponentUpdate = new ChatComponentText(messageUpdateURL);
					chatComponentUpdate.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, updateRedirectionURL));
					chatComponentUpdate.getChatStyle().setUnderlined(true);
					commandSender.addChatMessage(chatComponentUpdate);
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
