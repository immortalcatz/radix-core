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
	 * @param 	input	String of data that MD5 hash will be generated for.
	 * 
	 * @return	MD5 hash of the provided input in string format.
	 */
	public static String getMD5Hash(String input)
	{
		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(input.getBytes());

			byte[] hash = md5.digest();
			StringBuffer buffer = new StringBuffer();

			for (byte b : hash) 
			{
				buffer.append(Integer.toHexString(b & 0xff));
			}

			return buffer.toString();
		}

		catch (Exception e)
		{
			return "UNABLE TO PROCESS";
		}
	}
}
