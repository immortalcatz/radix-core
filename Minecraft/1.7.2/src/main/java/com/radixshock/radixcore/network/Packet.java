/*******************************************************************************
 * Packet.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import com.radixshock.radixcore.core.IMod;

import cpw.mods.fml.relauncher.Side;

/**
 * Defines a packet that will be sent through a mod's packet pipeline.
 */
public final class Packet 
{
	/** The packet's owner IMod. Assigned by the pipeline. */
	protected IMod mod;
	
	/** The packet's packet type as an enum. */
	public Enum packetType;
	
	/** The packet's data/payload. */
	public Object[] arguments;
	
	/**
	 * Construcor required by pipeline.
	 */
	public Packet()
	{
		super();
	}
	
	/**
	 * Creates a new packet.
	 * 
	 * @param packetType	The packet's packet type, defined by the mod.
	 * @param arguments		The packet's data. This can accept any number of arguments, 
	 * 						and should be encoded, decoded, and cast in the PacketHandler
	 * 						in the <b>exact same order listed when the packet was created.</b> Be consistent.
	 */
	public Packet(Enum packetType, Object... arguments) 
	{
		super();
		
		this.packetType = packetType;
		this.arguments = arguments;
	}

	/**
	 * Encodes a packet's data into the provided ByteBuf.
	 * 
	 * @param 	context	The packet's ChannelHandlerContext.
	 * @param 	buffer	The buffer that data will be written to.
	 */
	protected void encodeInto(ChannelHandlerContext context, ByteBuf buffer) 
	{
		//Add header containing the packet's type ordinal and the number of arguments.
		buffer.writeInt(packetType.ordinal());
		buffer.writeInt(arguments.length);
		
		//Encode the packet's payload.
		mod.getPacketCodec().encode(this, context, buffer);
	}

	/**
	 * Decodes a packet's data from the provided ByteBuf.
	 * 
	 * @param 	context	The packet's ChannelHandlerContext.
	 * @param 	buffer	The buffer that data will be read from.
	 */
	protected void decodeInto(ChannelHandlerContext context, ByteBuf buffer) 
	{
		//Read packet type and arguments length from the header.
		try 
		{
			packetType = (Enum)mod.getPacketTypeClass().getFields()[buffer.readInt()].get(mod.getPacketTypeClass());
		} 
		
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		
		arguments = new Object[buffer.readInt()];

		//Decode the packet's payload.
		mod.getPacketCodec().decode(this, context, buffer);
	}

	/**
	 * Sends a packet to the packet handler, providing Side.CLIENT as the packet's side.
	 * 
	 * @param 	player	The client player.
	 */
	protected void handleClientSide(EntityPlayer player) 
	{
		mod.getPacketHandler().onHandlePacket(this, player, Side.CLIENT);
	}

	/**
	 * Sends a packet to the packet handler, providing Side.SERVER as the packet's side.
	 * 
	 * @param 	player	The player that sent the packet.
	 */
	protected void handleServerSide(EntityPlayer player) 
	{
		mod.getPacketHandler().onHandlePacket(this, player, Side.SERVER);
	}
}