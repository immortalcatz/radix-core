/*******************************************************************************
 * GuiBadRadixVersion.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.client.gui;

import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import com.radixshock.radixcore.constant.Font.Color;
import com.radixshock.radixcore.constant.Font.Format;
import com.radixshock.radixcore.core.IEnforcedCore;
import com.radixshock.radixcore.core.RadixCore;
import com.radixshock.radixcore.util.object.Version;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Defines the GUI shown when there was an error loading a language file.
 */
@SideOnly(Side.CLIENT)
public class GuiBadRadixVersion extends GuiScreen
{
	private final String message1;
	private final String message2;
	private final String message3;

	/**
	 * Constructor
	 */
	public GuiBadRadixVersion(IEnforcedCore throwingMod, Version currentRadixVersion)
	{
		super();

		message1 = "You are using RadixCore version " + Format.BOLD + Color.RED + currentRadixVersion.toString() + Format.RESET + ".";
		message2 = "The mod '" + Color.GOLD + Format.ITALIC + throwingMod.getLongModName() + Format.RESET + "' requires version " + Format.BOLD + Color.GREEN + throwingMod.getMinimumRadixCoreVersion() + Format.RESET + " or higher.";
		message3 = "Please update RadixCore.";
	}

	@Override
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 95, height / 2 + 35, "Close Minecraft"));
		buttonList.add(new GuiButton(1, width / 2 - 95, height / 2 + 10, "Go to Update Website"));
	}

	@Override
	protected void actionPerformed(GuiButton guiButton)
	{
		if (guiButton.id == 0)
		{
			System.exit(-1);
		}

		else if (guiButton.id == 1)
		{
			try
			{
				final URL url = new URL(RadixCore.getInstance().getUpdateURL());
				final Scanner scanner = new Scanner(url.openStream());

				String redirectURL = RadixCore.getInstance().getRedirectURL();
				final String validGameVersions = scanner.nextLine();
				final String mostRecentVersion = scanner.nextLine();

				redirectURL = redirectURL.replace("currentMC=%", "currentMC=" + validGameVersions);
				redirectURL = redirectURL.replace("currentRadixCore=%", "currentRadixCore=" + mostRecentVersion);

				scanner.close();

				final URI uri = new URI(redirectURL);

				final Class clazz = Class.forName("java.awt.Desktop");
				final Object object = clazz.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
				clazz.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { uri });
			}

			catch (final Throwable e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void drawScreen(int x, int y, float partialTickTime)
	{
		initGui();

		drawDefaultBackground();

		drawCenteredString(fontRendererObj, message1, width / 2, 70, 0xffffff);
		drawCenteredString(fontRendererObj, message2, width / 2, 90, 0xffffff);
		drawCenteredString(fontRendererObj, message3, width / 2, 110, 0xffffff);

		super.drawScreen(x, y, partialTickTime);
	}
}
