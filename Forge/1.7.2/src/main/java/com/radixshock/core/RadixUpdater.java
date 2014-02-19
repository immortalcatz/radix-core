package com.radixshock.core;

import net.minecraft.command.ICommandSender;

import com.radixshock.updater.AbstractUpdater;

public class RadixUpdater extends AbstractUpdater
{
	public RadixUpdater(IMod mod, ICommandSender commandSender) 
	{
		super(mod, commandSender);
	}

	@Override
	public String getRawUpdateInformationURL() 
	{
		return null; //TODO
	}

	@Override
	public String getUpdateRedirectionURL() 
	{
		return null; //TODO
	}

}
