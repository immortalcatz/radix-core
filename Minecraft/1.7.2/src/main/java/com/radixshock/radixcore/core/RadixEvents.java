package com.radixshock.radixcore.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/**
 * Defines events handles by RadixCore.
 */
public class RadixEvents 
{
	/**
	 * Checks for updates i
	 * @param event
	 */
	@SubscribeEvent
	public void playerLoggedInEventHandler(PlayerLoggedInEvent event)
	{
		new Thread(new UpdateChecker(RadixCore.getInstance(), event.player, RadixCore.getInstance().getUpdateURL(), RadixCore.getInstance().getRedirectURL())).start();
		
		for (IMod mod : RadixCore.registeredMods)
		{
			if (mod.getChecksForUpdates())
			{
				new Thread(new UpdateChecker(mod, event.player, mod.getUpdateURL(), mod.getRedirectURL())).start();
			}
		}
	}
}
