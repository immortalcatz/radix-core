/*******************************************************************************
 * FileSystemOperations.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
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
