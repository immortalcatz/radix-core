/*******************************************************************************
 * Point3D.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.logic;

/**
 * Used to store a group of 3D coordinates and easily move them around.
 */
public class Point3D
{
	public short sPosX;
	public short sPosY;
	public short sPosZ;

	public int iPosX;
	public int iPosY;
	public int iPosZ;

	public float fPosX;
	public float fPosY;
	public float fPosZ;

	public double dPosX;
	public double dPosY;
	public double dPosZ;

	public Point3D(short posX, short posY, short posZ)
	{
		sPosX = posX;
		sPosY = posY;
		sPosZ = posZ;

		iPosX = posX;
		iPosY = posY;
		iPosZ = posZ;

		fPosX = posX;
		fPosY = posY;
		fPosZ = posZ;

		dPosX = posX;
		dPosY = posY;
		dPosZ = posZ;
	}

	public Point3D(int posX, int posY, int posZ)
	{
		sPosX = (short) posX;
		sPosY = (short) posY;
		sPosZ = (short) posZ;

		iPosX = posX;
		iPosY = posY;
		iPosZ = posZ;

		fPosX = posX;
		fPosX = posY;
		fPosX = posZ;

		dPosX = posX;
		dPosY = posY;
		dPosZ = posZ;
	}

	public Point3D(float posX, float posY, float posZ)
	{
		sPosX = (short) posX;
		sPosY = (short) posY;
		sPosZ = (short) posZ;

		iPosX = (int) posX;
		iPosY = (int) posY;
		iPosZ = (int) posZ;

		fPosX = posX;
		fPosY = posY;
		fPosZ = posZ;

		dPosX = posX;
		dPosY = posY;
		dPosZ = posZ;
	}

	public Point3D(double posX, double posY, double posZ)
	{
		sPosX = (short) posX;
		sPosY = (short) posY;
		sPosZ = (short) posZ;

		iPosX = (int) posX;
		iPosY = (int) posY;
		iPosZ = (int) posZ;

		fPosX = (float) posX;
		fPosY = (float) posY;
		fPosZ = (float) posZ;

		dPosX = posX;
		dPosY = posY;
		dPosZ = posZ;
	}

	/**
	 * Gets string representation of the Coordinates object.
	 * 
	 * @return "x, y, z" as string representation of the coordinates stored in this object.
	 */
	@Override
	public String toString()
	{
		return dPosX + ", " + dPosY + ", " + dPosZ;
	}
}
