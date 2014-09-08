package com.radixshock.radixcore.core;

import net.minecraft.command.ICommandSender;

public interface IUpdateChecker
{
	void setCommandSender(ICommandSender sender);

	void run();
}
