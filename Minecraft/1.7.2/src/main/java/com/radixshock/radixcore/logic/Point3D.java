/*******************************************************************************
 * Point3D.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
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
        this.sPosX = posX;
        this.sPosY = posY;
        this.sPosZ = posZ;
        
        this.iPosX = posX;
        this.iPosY = posY;
        this.iPosZ = posZ;
        
        this.fPosX = posX;
        this.fPosY = posY;
        this.fPosZ = posZ;
        
        this.dPosX = posX;
        this.dPosY = posY;
        this.dPosZ = posZ;
    }
    
    public Point3D(int posX, int posY, int posZ)
    {
        this.sPosX = (short) posX;
        this.sPosY = (short) posY;
        this.sPosZ = (short) posZ;
        
        this.iPosX = posX;
        this.iPosY = posY;
        this.iPosZ = posZ;
        
        this.fPosX = (float) posX;
        this.fPosX = (float) posY;
        this.fPosX = (float) posZ;
        
        this.dPosX = (double) posX;
        this.dPosY = (double) posY;
        this.dPosZ = (double) posZ;
    }
    
    public Point3D(float posX, float posY, float posZ)
    {
        this.sPosX = (short) posX;
        this.sPosY = (short) posY;
        this.sPosZ = (short) posZ;
        
        this.iPosX = (int) posX;
        this.iPosY = (int) posY;
        this.iPosZ = (int) posZ;
        
        this.fPosX = posX;
        this.fPosY = posY;
        this.fPosZ = posZ;
        
        this.dPosX = (double) posX;
        this.dPosY = (double) posY;
        this.dPosZ = (double) posZ;
    }
    
    public Point3D(double posX, double posY, double posZ)
    {
        this.sPosX = (short) posX;
        this.sPosY = (short) posY;
        this.sPosZ = (short) posZ;
        
        this.iPosX = (int) posX;
        this.iPosY = (int) posY;
        this.iPosZ = (int) posZ;
        
        this.fPosX = (float) posX;
        this.fPosY = (float) posY;
        this.fPosZ = (float) posZ;
        
        this.dPosX = posX;
        this.dPosY = posY;
        this.dPosZ = posZ;
    }

    /**
     * Gets string representation of the Coordinates object.
     * 
     * @return	"x, y, z" as string representation of the coordinates stored in this object.
     */
    public String toString()
    {
    	return dPosX + ", " + dPosY + ", " + dPosZ;
    }
}
