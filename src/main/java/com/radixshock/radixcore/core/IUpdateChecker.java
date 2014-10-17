/*******************************************************************************
 * IUpdateChecker.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.core;

import net.minecraft.command.ICommandSender;

public interface IUpdateChecker
{
	void setCommandSender(ICommandSender sender);

	void run();
}
