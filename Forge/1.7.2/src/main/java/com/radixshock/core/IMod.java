package com.radixshock.core;

import com.radixshock.network.AbstractPacketCodec;
import com.radixshock.network.AbstractPacketHandler;
import com.radixshock.network.PacketPipeline;

public interface IMod 
{
	void initializeNetwork();

	String getShortModName();
	
	String getLongModName();
	
	String getVersion();
	
	ModLogger getLogger();
	
	AbstractPacketCodec getPacketCodec();
	
	AbstractPacketHandler getPacketHandler();
	
	PacketPipeline getPacketPipeline();
	
	Enum getPacketTypes();
}