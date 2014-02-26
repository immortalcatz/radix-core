package com.radixshock.radixcore.util;

import java.io.File;

public final class FileSystemOperations 
{
	/**
	 * Deletes a path and all files and folders within.
	 * 
	 * @param 	file	The path to delete.
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
				String files[] = file.list();

				for (String temp : files)
				{
					File fileDelete = new File(file, temp);
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
