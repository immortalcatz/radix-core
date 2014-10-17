/*******************************************************************************
 * FileSystem.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.util;

import java.io.File;

/**
 * Defines utility methods that interact with the file system.
 */
public final class FileSystem
{
	/**
	 * Deletes a path and all files and folders within.
	 * 
	 * @param file The path to delete.
	 */
	public static void recursiveDeletePath(File file)
	{
		if (file.isDirectory())
		{
			if (file.list().length == 0)
			{
				file.delete();
			}

			else
			{
				final String files[] = file.list();

				for (final String temp : files)
				{
					final File fileDelete = new File(file, temp);
					recursiveDeletePath(fileDelete);
				}

				if (file.list().length == 0)
				{
					file.delete();
				}
			}
		}

		else
		{
			file.delete();
		}
	}

}
