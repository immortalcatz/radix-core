package radixcore.util;

import radixcore.core.RadixCore;

/**
 * Utility class that will log error messages to the console.
 */
public final class RadixExcept 
{
	public static void logErrorCatch(Throwable t, String description)
	{
		RadixCore.getLogger().catching(t);
		RadixCore.getLogger().error("Unexpected exception/(" + description +"). " + t.getMessage());
	}
	
	/**
	 * Logs the provided information to the console and stops the game immediately.
	 */
	public static void logFatalCatch(Throwable t, String description)
	{
		RadixCore.getLogger().catching(t);
		RadixCore.getLogger().fatal("Unexpected exception/(" + description +"). " + t.getMessage());
		throw new RuntimeException("Caught fatal exception and stopped the game. Please review your logs for crash details.");
	}
	
	private RadixExcept()
	{
		
	}
}
