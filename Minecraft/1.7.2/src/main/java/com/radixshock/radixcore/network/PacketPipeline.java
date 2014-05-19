/*******************************************************************************
 * PacketPipeline.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

import com.radixshock.radixcore.core.IEnforcedCore;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A mod's packet pipeline. Directs packets to their targets, codec, and
 * handler. Adapted from code shared by Sirgingalot on the MinecraftForge wiki.
 */
@ChannelHandler.Sharable
public final class PacketPipeline extends MessageToMessageCodec<FMLProxyPacket, Packet>
{
	private final IEnforcedCore							mod;
	private EnumMap<Side, FMLEmbeddedChannel>			channels;
	private final LinkedList<Class<? extends Packet>>	packets	= new LinkedList<Class<? extends Packet>>();

	/**
	 * Constructor
	 * 
	 * @param mod
	 *            The pipeline's owner IMod.
	 */
	public PacketPipeline(IEnforcedCore mod)
	{
		this.mod = mod;
	}

	/**
	 * Registers the provided packet class with the pipeline.
	 * 
	 * @param packetClass
	 *            The packet class to be registered.
	 * 
	 * @return True if adding the packet was successful.
	 */
	public boolean registerPacket(Class<? extends Packet> packetClass)
	{
		return packets.add(packetClass);
	}

	@Override
	protected void encode(ChannelHandlerContext context, Packet packet, List<Object> out) throws Exception
	{
		packet.mod = mod;

		final ByteBuf buffer = Unpooled.buffer();
		final Class<? extends Packet> packetClass = packet.getClass();

		if (!packets.contains(packet.getClass())) { throw new NullPointerException("No packet registered for: " + packet.getClass().getCanonicalName()); }

		final byte discriminator = (byte) packets.indexOf(packetClass);
		buffer.writeByte(discriminator);

		packet.encodeInto(context, buffer);

		final FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer.copy(), context.channel().attr(NetworkRegistry.FML_CHANNEL).get());
		out.add(proxyPacket);
	}

	@Override
	protected void decode(ChannelHandlerContext context, FMLProxyPacket proxyPacket, List<Object> out) throws Exception
	{
		final ByteBuf payload = proxyPacket.payload();
		final byte discriminator = payload.readByte();
		final Class<? extends Packet> packetClass = packets.get(discriminator);

		if (packetClass == null) { throw new NullPointerException("No packet registered for discriminator: " + discriminator); }

		final Packet packet = packetClass.newInstance();
		packet.mod = mod;
		packet.decodeInto(context, payload.slice());

		EntityPlayer player;

		switch (FMLCommonHandler.instance().getEffectiveSide())
		{
			case CLIENT:
				player = getClientPlayer();
				packet.handleClientSide(player);
				break;

			case SERVER:
				final INetHandler netHandler = context.channel().attr(NetworkRegistry.NET_HANDLER).get();
				player = ((NetHandlerPlayServer) netHandler).playerEntity;
				packet.handleServerSide(player);
				break;

			default:
		}

		out.add(packet);
	}

	/**
	 * Add a name for your packet's channel.
	 * 
	 * @param channelName
	 *            The channel's name.
	 */
	public void addChannel(String channelName)
	{
		channels = NetworkRegistry.INSTANCE.newChannel(channelName, this);
	}

	@SideOnly(Side.CLIENT)
	private EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}

	/**
	 * Sends the provided packet to all players.
	 * 
	 * @param packet
	 *            The packet to be sent.
	 */
	public void sendPacketToAllPlayers(Packet packet)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeAndFlush(packet);
	}

	/**
	 * Sends the provided packet to all players except the provided player.
	 * 
	 * @param packet
	 *            The packet to be sent.
	 * @param player
	 *            The player that will not receive the packet.
	 */
	public void sendPacketToAllPlayersExcept(Packet packet, EntityPlayerMP player)
	{
		final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		final ServerConfigurationManager serverConfiguration = server.getConfigurationManager();

		for (int index = 0; index < serverConfiguration.playerEntityList.size(); ++index)
		{
			final EntityPlayerMP playerInList = (EntityPlayerMP) serverConfiguration.playerEntityList.get(index);

			if (!playerInList.getCommandSenderName().equals(player.getCommandSenderName()))
			{
				sendPacketToPlayer(packet, playerInList);
			}
		}
	}

	/**
	 * Sends the provided packet to the provided player.
	 * 
	 * @param packet
	 *            The packet to be sent.
	 * @param player
	 *            The player that will receive the packet.
	 */
	public void sendPacketToPlayer(Packet packet, EntityPlayerMP player)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channels.get(Side.SERVER).writeAndFlush(packet);
	}

	/**
	 * Sends the provided packet to everyone within a certain range of the
	 * provided point.
	 * 
	 * @param packet
	 *            The packet to be sent.
	 * @param point
	 *            The point around which to send the packet.
	 */
	public void sendPacketToAllAround(Packet packet, NetworkRegistry.TargetPoint point)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		channels.get(Side.SERVER).writeAndFlush(packet);
	}

	/**
	 * Sends the provided packet to everyone within the supplied dimension.
	 * 
	 * @param packet
	 *            The packet to be sent.
	 * @param dimensionId
	 *            The dimension id.
	 */
	public void sendPacketToDimension(Packet packet, int dimensionId)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
		channels.get(Side.SERVER).writeAndFlush(packet);
	}

	/**
	 * Sends this message to the server.
	 * 
	 * @param packet
	 *            The packet to be sent.
	 */
	public void sendPacketToServer(Packet packet)
	{
		channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channels.get(Side.CLIENT).writeAndFlush(packet);
	}
}
