/*******************************************************************************
 * RadixEvents.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/**
 * Defines events handles by RadixCore.
 */
public class RadixEvents
{
	@SubscribeEvent
	public void playerLoggedInEventHandler(PlayerLoggedInEvent event)
	{
		new Thread(new UpdateChecker(RadixCore.getInstance(), event.player, RadixCore.getInstance().getUpdateURL(), RadixCore.getInstance().getRedirectURL())).start();

		for (final IEnforcedCore mod : RadixCore.registeredMods)
		{
			if (mod.getChecksForUpdates())
			{
				new Thread(new UpdateChecker(mod, event.player, mod.getUpdateURL(), mod.getRedirectURL())).start();
			}
		}
	}
}
