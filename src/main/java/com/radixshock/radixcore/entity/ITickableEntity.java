/*******************************************************************************
 * ITickableEntity.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.entity;

/**
 * Allows an entity to use tick markers.
 */
public interface ITickableEntity
{
	/**
	 * @return This should return your entity's entity ID.
	 */
	int getId();

	/**
	 * @return This should return an integer defined in the entity that increments once per tick and is <b>never</b> reset. MCA calls this field <code>lifeTicks</code> in <code>AbstractEntity.java</code>.
	 */
	int getTimeAlive();

	/**
	 * Should call update() on all tick markers used by this entity.
	 */
	void updateTickMarkers();
}
