/*******************************************************************************
 * RadixEvents.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

import com.radixshock.radixcore.client.gui.GuiBadRadixVersion;
import com.radixshock.radixcore.util.object.Version;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Defines events handles by RadixCore.
 */
public class RadixEvents
{
	@SubscribeEvent
	public void playerLoggedInEventHandler(PlayerLoggedInEvent event)
	{
		final IUpdateChecker radixUpdater = RadixCore.getInstance().getCustomUpdateChecker();
		radixUpdater.setCommandSender(event.player);
		new Thread(new RunnableUpdateChecker(radixUpdater)).start();
		
		for (final IEnforcedCore mod : RadixCore.registeredMods)
		{
			if (mod.getChecksForUpdates())
			{
				if (mod.getUsesCustomUpdateChecker())
				{
					final IUpdateChecker updateChecker = mod.getCustomUpdateChecker();
					updateChecker.setCommandSender(event.player);
					new Thread(new RunnableUpdateChecker(updateChecker)).start();
					;
				}

				else
				{
					new Thread(new UpdateChecker(mod, event.player, mod.getUpdateURL(), mod.getRedirectURL())).start();
				}
			}
		}
	}

	/**
	 * Ticks the client tick handler.
	 * 
	 * @param event The event.
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event)
	{
		if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu)
		{
			if (RadixCore.getInstance().isBadVersion)
			{
				Minecraft.getMinecraft().displayGuiScreen(new GuiBadRadixVersion(RadixCore.getInstance().throwingMod, new Version(RadixCore.getInstance().getVersion())));
			}
		}
	}
}
