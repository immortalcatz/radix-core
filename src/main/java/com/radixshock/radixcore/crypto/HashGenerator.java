/*******************************************************************************
 * HashGenerator.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.crypto;

import java.security.MessageDigest;

/**
 * Only generates MD5 hashes of string inputs at this time.
 */
public final class HashGenerator
{
	/**
	 * Provides an MD5 hash based on input
	 * 
	 * @param input String of data that MD5 hash will be generated for.
	 * @return MD5 hash of the provided input in string format.
	 */
	public static String getMD5Hash(String input)
	{
		try
		{
			final MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(input.getBytes());

			final byte[] hash = md5.digest();
			final StringBuffer buffer = new StringBuffer();

			for (final byte b : hash)
			{
				buffer.append(Integer.toHexString(b & 0xff));
			}

			return buffer.toString();
		}

		catch (final Exception e)
		{
			return "UNABLE TO PROCESS";
		}
	}
}
