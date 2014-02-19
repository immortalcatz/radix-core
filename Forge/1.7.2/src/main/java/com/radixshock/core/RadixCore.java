package com.radixshock.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;

import com.radixshock.network.AbstractPacketCodec;
import com.radixshock.network.AbstractPacketHandler;
import com.radixshock.network.PacketPipeline;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid="radixcore", name="RadixCore", version="1.0.0")
public class RadixCore implements IMod
{
	@Instance("radixcore")
	private static RadixCore instance;
	private ModLogger logger;
	
	public String runningDirectory;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		instance = this;
		logger = new ModLogger(this);
		runningDirectory = System.getProperty("user.dir");
		
		logger.log("RadixCore version " + getVersion() + " started successfully.");
	}

	public static RadixCore getInstance()
	{
		return instance;
	}
	
	/**
	 * Stops the game and writes the error to the Forge crash log.
	 * 
	 * @param 	description	A string providing a short description of the problem.
	 * @param 	e			The exception that caused this method to be called.
	 */
	@SideOnly(Side.CLIENT)
	public void quitWithDescription(String description)
	{
		final Writer stackTrace = new StringWriter();
		final Exception exception = new Exception();

		PrintWriter stackTraceWriter = new PrintWriter(stackTrace);
		exception.printStackTrace(stackTraceWriter);

		logger.log(Level.FINER, "Minecraft Comes Alive: An exception occurred.\n>>>>>" + description + "<<<<<\n" + stackTrace.toString());
		System.out.println("Minecraft Comes Alive: An exception occurred.\n>>>>>" + description + "<<<<<\n" + stackTrace.toString());

		final CrashReport crashReport = new CrashReport("MCA: " + description, exception);
		Minecraft.getMinecraft().crashed(crashReport);
		Minecraft.getMinecraft().displayCrashReport(crashReport);
	}

	/**
	 * Stops the game and writes the error to the Forge crash log.
	 * 
	 * @param 	description	A string providing a short description of the problem.
	 * @param 	exception	The exception that caused this method to be called.
	 */
	@SideOnly(Side.CLIENT)
	public void quitWithException(String description, Exception exception)
	{
		final Writer stackTrace = new StringWriter();
		final PrintWriter stackTraceWriter = new PrintWriter(stackTrace);
		exception.printStackTrace(stackTraceWriter);

		logger.log(Level.FINER, "Minecraft Comes Alive: An exception occurred.\n>>>>>" + description + "<<<<<\n" + stackTrace.toString());
		System.out.println("Minecraft Comes Alive: An exception occurred.\n>>>>>" + description + "<<<<<\n" + stackTrace.toString());

		final CrashReport crashReport = new CrashReport("MCA: " + description, exception);
		Minecraft.getMinecraft().crashed(crashReport);
		Minecraft.getMinecraft().displayCrashReport(crashReport);
	}
	
	@Override
	public String getShortModName() 
	{
		return getLongModName();
	}

	@Override
	public String getLongModName() 
	{
		return "RadixCore";
	}

	@Override
	public String getVersion() 
	{
		return "1.0.0";
	}

	@Override
	public ModLogger getLogger() 
	{
		return logger;
	}

	@Override
	public void initializeNetwork() 
	{
	}

	@Override
	public AbstractPacketCodec getPacketCodec() 
	{
		return null;
	}

	@Override
	public AbstractPacketHandler getPacketHandler() 
	{
		return null;
	}

	@Override
	public PacketPipeline getPacketPipeline() 
	{
		return null;
	}

	@Override
	public Enum getPacketTypes() 
	{
		return null;
	}
}
