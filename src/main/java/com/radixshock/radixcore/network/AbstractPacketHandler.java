package com.radixshock.radixcore.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

import com.radixshock.radixcore.core.IEnforcedCore;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public abstract class AbstractPacketHandler
{
	protected SimpleNetworkWrapper wrapper;

	public AbstractPacketHandler(IEnforcedCore ownerMod)
	{
		wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ownerMod.getShortModName());
		registerPackets();

		//		registerDoubleSidedPacket(PacketSpawnParticles.class, 5000);
	}

	public abstract void registerPackets();

	protected void registerPacket(Class packetClass, int discriminator, Side processorSide)
	{
		wrapper.registerMessage(packetClass, packetClass, discriminator, processorSide);
	}

	protected void registerDoubleSidedPacket(Class packetClass, int discriminator)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			registerPacket(packetClass, discriminator, Side.CLIENT);
		}

		else
		{
			registerPacket(packetClass, discriminator, Side.SERVER);
		}
	}

	/**
	 * Sends the provided packet to all players.
	 * 
	 * @param packet The packet to be sent.
	 */
	public void sendPacketToAllPlayers(IMessage packet)
	{
		wrapper.sendToAll(packet);
	}

	/**
	 * Sends the provided packet to all players except the provided player.
	 * 
	 * @param packet The packet to be sent.
	 * @param player The player that will not receive the packet.
	 */
	public void sendPacketToAllPlayersExcept(IMessage packet, EntityPlayerMP player)
	{
		final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		final ServerConfigurationManager serverConfiguration = server.getConfigurationManager();

		for (int index = 0; index < serverConfiguration.playerEntityList.size(); ++index)
		{
			final EntityPlayerMP playerInList = (EntityPlayerMP) serverConfiguration.playerEntityList.get(index);

			if (!playerInList.getCommandSenderName().equals(player.getCommandSenderName()))
			{
				wrapper.sendTo(packet, playerInList);
			}
		}
	}

	/**
	 * Sends the provided packet to the provided player.
	 * 
	 * @param packet The packet to be sent.
	 * @param player The player that will receive the packet.
	 */
	public void sendPacketToPlayer(IMessage packet, EntityPlayerMP player)
	{
		if (player != null)
		{
			wrapper.sendTo(packet, player);
		}
	}

	/**
	 * Sends the provided packet to everyone within a certain range of the provided point.
	 * 
	 * @param packet The packet to be sent.
	 * @param point The point around which to send the packet.
	 */
	public void sendPacketToAllAround(IMessage packet, NetworkRegistry.TargetPoint point)
	{
		wrapper.sendToAllAround(packet, point);
	}

	/**
	 * Sends the provided packet to everyone within the supplied dimension.
	 * 
	 * @param packet The packet to be sent.
	 * @param dimensionId The dimension id.
	 */
	public void sendPacketToDimension(IMessage packet, int dimensionId)
	{
		wrapper.sendToDimension(packet, dimensionId);
	}

	/**
	 * Sends this message to the server.
	 * 
	 * @param packet The packet to be sent.
	 */
	public void sendPacketToServer(IMessage packet)
	{
		wrapper.sendToServer(packet);
	}
}
