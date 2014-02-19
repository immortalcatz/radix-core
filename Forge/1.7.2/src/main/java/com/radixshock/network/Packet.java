package com.radixshock.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import com.radixshock.core.IMod;

/**
 * Base class for all packets that will be sent through the packet pipeline.
 */
public class Packet 
{
	protected IMod mod;
	protected Enum packetType;
	protected Object[] arguments;
	
	public Packet()
	{
		super();
	}
	
	public Packet(Enum packetType, Object... arguments) 
	{
		super();
		
		this.packetType = packetType;
		this.arguments = arguments;
	}

	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer) 
	{
		//Add header containing the packet's type ordinal and the number of arguments.
		buffer.writeInt(packetType.ordinal());
		buffer.writeInt(arguments.length);
		
		//Encode the packet's payload.
		mod.getPacketCodec().encode(this, context, buffer);
	}

	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer) 
	{
		//Read packet type and arguments length from the header.
		//TODO
//		packetType = mod.getPacketTypes().getDeclaringClass().[buffer.readInt()];
		arguments = new Object[buffer.readInt()];

		//Decode the packet's payload.
		mod.getPacketCodec().decode(this, context, buffer);
	}

	public void handleClientSide(EntityPlayer player) 
	{
		mod.getPacketHandler().onHandlePacket(this, player);
	}

	public void handleServerSide(EntityPlayer player) 
	{
		mod.getPacketHandler().onHandlePacket(this, player);
	}
}