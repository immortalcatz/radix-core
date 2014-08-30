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
