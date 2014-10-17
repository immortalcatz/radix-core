/*******************************************************************************
 * RunnableUpdateChecker.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.core;

public class RunnableUpdateChecker implements Runnable
{
	private final IUpdateChecker updateChecker;

	public RunnableUpdateChecker(IUpdateChecker updateChecker)
	{
		this.updateChecker = updateChecker;
	}

	@Override
	public void run()
	{
		updateChecker.run();
	}
}
