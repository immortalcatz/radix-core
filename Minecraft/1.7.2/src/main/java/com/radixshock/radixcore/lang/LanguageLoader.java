/*******************************************************************************
 * LanguageLoader.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.lang;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.util.StringTranslate;

import com.radixshock.radixcore.constant.Language;
import com.radixshock.radixcore.core.IEnforcedCore;
import com.radixshock.radixcore.core.RadixCore;

/**
 * Handles loading the language files into the mod and retrieving strings from them.
 */
public final class LanguageLoader
{
	private IEnforcedCore mod = null;
	private final ConcurrentHashMap<String, String> translationsMap = new ConcurrentHashMap();
	private String languageName = "";

	/** The properties instance used to load languages. */
	private final Properties properties = new Properties();

	/**
	 * Constructor
	 * 
	 * @param mod The language loader's owner IMod.
	 */
	public LanguageLoader(IEnforcedCore mod)
	{
		this.mod = mod;
	}

	/**
	 * Loads the language whose ID is in the options.txt file.
	 */
	public void loadLanguage()
	{
		if (!mod.getLanguageLoaded())
		{
			loadLanguage(getLanguageIDFromOptions());
			mod.setLanguageLoaded(true);
		}
	}

	/**
	 * Loads the language with the specified language ID.
	 * 
	 * @param languageID The ID of the language to load.
	 */
	public void loadLanguage(String languageID)
	{
		// Clear old data.
		translationsMap.clear();

		// Get the name and location of the appropriate language file.
		languageName = Language.valueOf(getLanguageIDFromOptions()).getEnglishName();
		mod.getLogger().log("Language ID: " + languageID + " - " + languageName);

		if (languageName == null)
		{
			mod.getLogger().log("WARNING: Unable to find mapping of langauge ID. Defaulting to English.");
			languageName = "English";
			languageID = "en_US";
		}

		mod.getLogger().log("Loading " + languageName + "...");

		try
		{
			final ILanguageLoaderHook modHook = mod.getLanguageLoaderHook();

			properties.load(StringTranslate.class.getResourceAsStream("/assets/" + mod.getShortModName().toLowerCase() + "/lang/" + languageID + ".lang"));

			// Loop through each item in the properties instance.
			for (final Map.Entry<Object, Object> entrySet : properties.entrySet())
			{
				// OMIT will make the language loader skip that phrase.
				if (!entrySet.getValue().toString().equalsIgnoreCase("OMIT"))
				{
					if (modHook != null && modHook.processEntrySet(entrySet))
					{
						continue;
					}

					else
					{
						translationsMap.put(entrySet.getKey().toString(), entrySet.getValue().toString());
					}
				}
			}

			// Clear the properties instance.
			properties.clear();

			mod.getLogger().log("Loaded " + translationsMap.size() + " phrases in " + languageName + ".");
		}

		catch (final IOException e)
		{
			RadixCore.getInstance().quitWithException("IOException while loading language.", e);
		}

		catch (final NullPointerException e)
		{
			mod.getLogger().log("WARNING: Recoverable error while loading language. Language may not be supported. Defaulting to English.");
			loadLanguage("en_US");
		}
	}

	/**
	 * Retrieves the specified string from the string translations map. Used when the string being retrieved is not being spoken by an entity, such as a GUI button or item name.
	 * 
	 * @param elementId The ID of the string to retrieve.
	 * @param arguments Arguments to use when parsing the string.
	 * @return Returns localized string matching the ID provided.
	 */
	public String getString(String elementId, Object... arguments)
	{
		final ILanguageLoaderHook modHook = mod.getLanguageLoaderHook();
		final List<String> matchingValues = new ArrayList();
		String outputString = "";
		elementId = elementId.toLowerCase();

		if (modHook != null && modHook.shouldReceiveGetStringCalls())
		{
			return modHook.onGetString(elementId, arguments);
		}

		// Loop through each item in the string translations map.
		for (final Map.Entry<String, String> entrySet : translationsMap.entrySet())
		{
			// Check if the entry's key contains the ID.
			if (entrySet.getKey().contains(elementId))
			{
				// Then check if it completely equals the ID.
				if (entrySet.getKey().equals(elementId))
				{
					// In this case, clear the values list and add only the
					// value that equals the ID.
					matchingValues.clear();
					matchingValues.add(entrySet.getValue());
					break;
				}

				else
				// Otherwise just add the matching ID's value to the matching
				// values list.
				{
					matchingValues.add(entrySet.getValue());
				}
			}
		}

		if (matchingValues.isEmpty())
		{
			outputString = "(" + elementId + " not found)";
		}

		return outputString;
	}

	/**
	 * Reads Minecraft's options file and retrieves the language ID from it.
	 * 
	 * @return Returns the language ID last loaded by Minecraft.
	 */
	public String getLanguageIDFromOptions()
	{
		BufferedReader reader = null;
		String languageID = "";

		try
		{
			reader = new BufferedReader(new FileReader(RadixCore.getInstance().runningDirectory + "/options.txt"));

			String line = "";

			while (line != null)
			{
				line = reader.readLine();

				if (line.contains("lang:"))
				{
					break;
				}
			}

			if (!line.isEmpty())
			{
				reader.close();
				languageID = line.substring(5);
			}
		}

		catch (final FileNotFoundException e)
		{
			mod.getLogger().log("Could not find options.txt file. Defaulting to English.");
			languageID = "en_US";
		}

		catch (final IOException e)
		{
			RadixCore.getInstance().quitWithException("Error reading from Minecraft options.txt file.", e);
			languageID = null;
		}

		catch (final NullPointerException e)
		{
			mod.getLogger().log("NullPointerException while trying to read options.txt. Defaulting to English.");
			languageID = "en_US";
		}

		return languageID;
	}

	/**
	 * @return This LanguageLoader's translations map.
	 */
	public Map<String, String> getTranslations()
	{
		return translationsMap;
	}

	public boolean isValidString(String elementId)
	{
		return !getString(elementId).contains("(" + elementId + " not found)");
	}
}
