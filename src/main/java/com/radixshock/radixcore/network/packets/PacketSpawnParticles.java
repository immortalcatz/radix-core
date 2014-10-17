/*******************************************************************************
 * PacketSpawnParticles.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.network.packets;

import io.netty.buffer.ByteBuf;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.radixshock.radixcore.network.ByteBufIO;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSpawnParticles extends AbstractPacket implements IMessage, IMessageHandler<PacketSpawnParticles, IMessage>
{
	private double posX;
	private double posY;
	private double posZ;
	private float width;
	private float height;
	private String particleName;
	private int intensity;

	public PacketSpawnParticles()
	{
	}

	public PacketSpawnParticles(double posX, double posY, double posZ, float width, float height, String particleName, int intensity)
	{
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.width = width;
		this.height = height;
		this.particleName = particleName;
		this.intensity = intensity;
	}

	public PacketSpawnParticles(Entity entity, String particleName, int intensity)
	{
		posX = entity.posX;
		posY = entity.posY;
		posZ = entity.posZ;
		width = entity.width;
		height = entity.height;
		this.particleName = particleName;
		this.intensity = intensity;
	}

	@Override
	public void fromBytes(ByteBuf byteBuf)
	{
		posX = byteBuf.readDouble();
		posY = byteBuf.readDouble();
		posZ = byteBuf.readDouble();
		particleName = (String) ByteBufIO.readObject(byteBuf);
		intensity = byteBuf.readInt();
	}

	@Override
	public void toBytes(ByteBuf byteBuf)
	{
		byteBuf.writeDouble(posX);
		byteBuf.writeDouble(posY);
		byteBuf.writeDouble(posZ);
		ByteBufIO.writeObject(byteBuf, particleName);
		byteBuf.writeInt(intensity);
	}

	@Override
	public IMessage onMessage(PacketSpawnParticles packet, MessageContext context)
	{
		final Random rand = new Random();
		final World worldObj = Minecraft.getMinecraft().theWorld;

		for (int loops = 0; loops < intensity; loops++)
		{
			final double velX = rand.nextGaussian() * 0.02D;
			final double velY = rand.nextGaussian() * 0.02D;
			final double velZ = rand.nextGaussian() * 0.02D;

			worldObj.spawnParticle(particleName, posX + rand.nextFloat() * width * 2.0F - width, posY + 0.5D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, velX, velY, velZ);
		}

		return null;
	}
}
