/*******************************************************************************
 * TickMarker.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.logic;

import java.io.Serializable;

import net.minecraft.nbt.NBTTagCompound;

import com.radixshock.entity.ITickableEntity;

/**
 * Helps keep track of events that can happen during an entity's lifetime.
 */
public abstract class TickMarker implements Serializable
{
	protected transient ITickableEntity owner;
	private int endTicks;
	private boolean isComplete;

	public TickMarker(ITickableEntity owner, int durationInTicks)
	{
		this.owner = owner;
		this.isComplete = false;
		this.endTicks = owner.getTimeAlive() + durationInTicks;
	}

	public void update()
	{
		if (owner != null && endTicks != -1 && !isComplete && owner.getTimeAlive() >= endTicks)
		{
			isComplete = true;
			onComplete();
		}
	}

	public boolean isComplete()
	{
		return endTicks == -1 ? false : isComplete;
	}

	public void reset()
	{
		endTicks = -1;
		isComplete = true;
	}

	public void writeMarkerToNBT(ITickableEntity owner, NBTTagCompound nbt)
	{
		this.owner = owner;
		nbt.setInteger("endTicks", endTicks);
		nbt.setBoolean("isComplete", isComplete);
	}

	public void readMarkerFromNBT(ITickableEntity owner, NBTTagCompound nbt)
	{
		this.owner = owner;
		endTicks = nbt.getInteger("endTicks");
		isComplete = nbt.getBoolean("isComplete");
	}
	
	public abstract void onComplete();
}
