/*******************************************************************************
 * TickMarker.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.logic;

import java.io.Serializable;

import net.minecraft.nbt.NBTTagCompound;

import com.radixshock.radixcore.entity.ITickableEntity;

/**
 * Helps keep track of events that can happen during an entity's lifetime.
 */
public abstract class TickMarker implements Serializable
{
	/** The TickMarker's owner entity. */
	protected transient ITickableEntity	owner;
	private int							endTicks;
	private boolean						isComplete;

	/**
	 * Constructor
	 * 
	 * @param owner
	 *            The owner of this TickMarker.
	 * @param durationInTicks
	 *            How long before the TickMarker's onComplete() method is run,
	 *            in ticks.
	 */
	public TickMarker(ITickableEntity owner, int durationInTicks)
	{
		this.owner = owner;
		isComplete = false;
		endTicks = owner.getTimeAlive() + durationInTicks;
	}

	/**
	 * Updates the TickMarker.
	 */
	public void update()
	{
		if (owner != null && endTicks != -1 && !isComplete && owner.getTimeAlive() >= endTicks)
		{
			isComplete = true;
			onComplete();
		}
	}

	/**
	 * @return True if this TickMarker has completed.
	 */
	public boolean isComplete()
	{
		return endTicks == -1 ? false : isComplete;
	}

	/**
	 * Resets this TickMarker.
	 */
	public void reset()
	{
		endTicks = -1;
		isComplete = true;
	}

	/**
	 * Writes the marker data to the game save.
	 * 
	 * @param owner
	 *            The marker's owner.
	 * @param nbt
	 *            The NBTTagCompound.
	 */
	public void writeMarkerToNBT(ITickableEntity owner, NBTTagCompound nbt)
	{
		this.owner = owner;
		nbt.setInteger("endTicks", endTicks);
		nbt.setBoolean("isComplete", isComplete);
	}

	/**
	 * Reads the marker data from the game save.
	 * 
	 * @param owner
	 *            The marker's owner.
	 * @param nbt
	 *            The NBTTagCompound.
	 */
	public void readMarkerFromNBT(ITickableEntity owner, NBTTagCompound nbt)
	{
		this.owner = owner;
		endTicks = nbt.getInteger("endTicks");
		isComplete = nbt.getBoolean("isComplete");
	}

	/**
	 * Called automatically on this TickMarker's completion.
	 */
	public abstract void onComplete();
}
