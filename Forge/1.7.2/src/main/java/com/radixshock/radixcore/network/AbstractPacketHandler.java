package com.radixshock.radixcore.network;

import net.minecraft.entity.player.EntityPlayer;

import com.radixshock.radixcore.core.IMod;

public abstract class AbstractPacketHandler 
{
	protected IMod mod;
	
	protected AbstractPacketHandler(IMod mod)
	{
		this.mod = mod;
	}
	
	public abstract void onHandlePacket(Packet packet, EntityPlayer player);
}
