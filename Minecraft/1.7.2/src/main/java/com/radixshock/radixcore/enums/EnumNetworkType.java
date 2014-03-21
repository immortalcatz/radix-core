/*******************************************************************************
 * EnumNetworkType.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.enums;

public enum EnumNetworkType 
{
	/**
	 * Use if your mod doesn't use the networking system.
	 */
	None,
	
	/**
	 * Use if you will define separate packets wherein all encoding, 
	 * decoding, and processing will be handled.
	 */
	SelfContained,
	
	/**
	 * Use to enable the 1.6 style of networking by defining your own
	 * PacketHandler.
	 */
	Legacy,
}