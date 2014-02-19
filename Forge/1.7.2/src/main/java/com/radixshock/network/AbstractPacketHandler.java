package com.radixshock.network;

import net.minecraft.entity.player.EntityPlayer;

import com.radixshock.core.IMod;

public abstract class AbstractPacketHandler 
{
	private IMod mod;
	
	protected AbstractPacketHandler(IMod mod)
	{
		this.mod = mod;
	}
	
	public abstract void onHandlePacket(Packet packet, EntityPlayer player);
}
