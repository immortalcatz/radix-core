/*******************************************************************************
 * AbstractPacketHandler.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.network;

import net.minecraft.entity.player.EntityPlayer;

import com.radixshock.radixcore.core.IEnforcedCore;

import cpw.mods.fml.relauncher.Side;

/**
 * Base class for a mod's packet handler.
 */
public abstract class AbstractPacketHandler
{
	/** The mod that this packet handler belongs to. */
	protected IEnforcedCore	mod;

	/**
	 * Constructor
	 * 
	 * @param mod
	 *            The packet handler's owner IMod.
	 */
	protected AbstractPacketHandler(IEnforcedCore mod)
	{
		this.mod = mod;
	}

	/**
	 * Handles a packet that has been received. At this point, the packet will
	 * have already been decoded and will contain the original data included by
	 * the packet's sender.
	 * <p>
	 * Recommened method for handling packets is to:
	 * <p>
	 * <ol>
	 * <li>Cast the packet's type (packet.packetType) into your mod's packet
	 * type enum.</li>
	 * <li>Switch on the newly cast enum type inside a try-catch block so that
	 * you can handle exceptions.</li>
	 * <li>Have a Case that handles each possible enum type, calling a private
	 * handleXXXX method.</li>
	 * <li>Actually perform what needs to be done with the packet inside the
	 * handleXXXX method.</li>
	 * </ol>
	 * 
	 * @param packet
	 *            The decoded packet.
	 * @param player
	 *            The player that received the packet.
	 * @param side
	 *            The current side you're working with, either CLIENT or SERVER.
	 */
	public abstract void onHandlePacket(Packet packet, EntityPlayer player, Side side);
}
