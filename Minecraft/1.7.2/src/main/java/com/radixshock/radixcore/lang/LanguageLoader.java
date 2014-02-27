/*******************************************************************************
 * LanguageHelper.java
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
import com.radixshock.radixcore.core.IMod;
import com.radixshock.radixcore.core.RadixCore;

/**
 * Handles loading the language files into the mod and retrieving strings from them.
 */
public final class LanguageLoader 
{
	private IMod mod = null;
	private ConcurrentHashMap<String, String> translationsMap = new ConcurrentHashMap();
	private String languageName = "";

	/** The properties instance used to load languages. */
	private Properties properties = new Properties();

	/**
	 * Constructor
	 * 
	 * @param 	mod	The language loader's owner IMod.
	 */
	public LanguageLoader(IMod mod)
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
	 * @param 	languageID	The ID of the language to load.
	 */
	public void loadLanguage(String languageID)
	{
		//Clear old data.
		translationsMap.clear();

		//Get the name and location of the appropriate language file.
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
			ILanguageLoaderHook modHook = mod.getLanguageLoaderHook();

			properties.load(StringTranslate.class.getResourceAsStream("/assets/" + mod.getShortModName().toLowerCase() + "/lang/" + languageID + ".lang"));

			//Loop through each item in the properties instance.
			for (final Map.Entry<Object, Object> entrySet : properties.entrySet())
			{
				//OMIT will make the language loader skip that phrase.
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

			//Clear the properties instance.
			properties.clear();

			//addLocalizedAchievementNames(languageID);

			mod.getLogger().log("Loaded " + translationsMap.size() + " phrases in " + languageName + ".");
		}

		catch (IOException e)
		{
			RadixCore.getInstance().quitWithException("IOException while loading language.", e);
		}
	}

	//TODO
	//	private void addLocalizedAchievementNames(String languageID)
	//	{
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_Charmer", languageID, LanguageHelper.getString("achievement.title.charmer"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_Charmer.desc", languageID, LanguageHelper.getString("achievement.descr.charmer")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_GetMarried", languageID, LanguageHelper.getString("achievement.title.getmarried"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_GetMarried.desc", languageID, LanguageHelper.getString("achievement.descr.getmarried")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveBabyBoy", languageID, LanguageHelper.getString("achievement.title.havebabyboy"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveBabyBoy.desc", languageID, LanguageHelper.getString("achievement.descr.havebabyboy")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveBabyGirl", languageID, LanguageHelper.getString("achievement.title.havebabygirl"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveBabyGirl.desc", languageID, LanguageHelper.getString("achievement.descr.havebabygirl")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_CookBaby", languageID, LanguageHelper.getString("achievement.title.cookbaby"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_CookBaby.desc", languageID, LanguageHelper.getString("achievement.descr.cookbaby")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_BabyGrowUp", languageID, LanguageHelper.getString("achievement.title.growbaby"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_BabyGrowUp.desc", languageID, LanguageHelper.getString("achievement.descr.growbaby")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildFarm", languageID, LanguageHelper.getString("achievement.title.farming"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildFarm.desc", languageID, LanguageHelper.getString("achievement.descr.farming")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildFish", languageID, LanguageHelper.getString("achievement.title.fishing"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildFish.desc", languageID, LanguageHelper.getString("achievement.descr.fishing")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildWoodcut", languageID, LanguageHelper.getString("achievement.title.woodcutting"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildWoodcut.desc", languageID, LanguageHelper.getString("achievement.descr.woodcutting")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildMine", languageID, LanguageHelper.getString("achievement.title.mining"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildMine.desc", languageID, LanguageHelper.getString("achievement.descr.mining")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildHuntKill", languageID, LanguageHelper.getString("achievement.title.huntkill"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildHuntKill.desc", languageID, LanguageHelper.getString("achievement.descr.huntkill"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildHuntTame", languageID, LanguageHelper.getString("achievement.title.hunttame"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildHuntTame.desc", languageID, LanguageHelper.getString("achievement.descr.hunttame"));		
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildGrowUp", languageID, LanguageHelper.getString("achievement.title.growkid"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ChildGrowUp.desc", languageID, LanguageHelper.getString("achievement.descr.growkid")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_AdultFullyEquipped", languageID, LanguageHelper.getString("achievement.title.equipadult"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_AdultFullyEquipped.desc", languageID, LanguageHelper.getString("achievement.descr.equipadult")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_AdultKills", languageID, LanguageHelper.getString("achievement.title.mobkills"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_AdultKills.desc", languageID, LanguageHelper.getString("achievement.descr.mobkills")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_AdultMarried", languageID, LanguageHelper.getString("achievement.title.marrychild"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_AdultMarried.desc", languageID, LanguageHelper.getString("achievement.descr.marrychild")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveGrandchild", languageID, LanguageHelper.getString("achievement.title.havegrandchild"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveGrandchild.desc", languageID, LanguageHelper.getString("achievement.descr.havegrandchild"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveGreatGrandchild", languageID, LanguageHelper.getString("achievement.title.havegreatgrandchild"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveGreatGrandchild.desc", languageID, LanguageHelper.getString("achievement.descr.havegreatgrandchild")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveGreatx2Grandchild", languageID, LanguageHelper.getString("achievement.title.havegreatx2grandchild"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveGreatx2Grandchild.desc", languageID, LanguageHelper.getString("achievement.descr.havegreatx2grandchild")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveGreatx10Grandchild", languageID, LanguageHelper.getString("achievement.title.havegreatx10grandchild"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HaveGreatx10Grandchild.desc", languageID, LanguageHelper.getString("achievement.descr.havegreatx10grandchild")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HardcoreSecret", languageID, LanguageHelper.getString("achievement.title.hardcoresecret"));
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_HardcoreSecret.desc", languageID, LanguageHelper.getString("achievement.descr.hardcoresecret")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_CraftCrown", languageID, LanguageHelper.getString("achievement.title.craftcrown")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_CraftCrown.desc", languageID, LanguageHelper.getString("achievement.descr.craftcrown")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ExecuteVillager", languageID, LanguageHelper.getString("achievement.title.executevillager")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_ExecuteVillager.desc", languageID, LanguageHelper.getString("achievement.descr.executevillager")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_MakeKnight", languageID, LanguageHelper.getString("achievement.title.makeknight")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_MakeKnight.desc", languageID, LanguageHelper.getString("achievement.descr.makeknight")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_KnightArmy", languageID, LanguageHelper.getString("achievement.title.knightarmy")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_KnightArmy.desc", languageID, LanguageHelper.getString("achievement.descr.knightarmy")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_MakePeasant", languageID, LanguageHelper.getString("achievement.title.makepeasant")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_MakePeasant.desc", languageID, LanguageHelper.getString("achievement.descr.makepeasant")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_PeasantArmy", languageID, LanguageHelper.getString("achievement.title.peasantarmy")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_PeasantArmy.desc", languageID, LanguageHelper.getString("achievement.descr.peasantarmy")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_NameHeir", languageID, LanguageHelper.getString("achievement.title.nameheir")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_NameHeir.desc", languageID, LanguageHelper.getString("achievement.descr.nameheir")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_MonarchSecret", languageID, LanguageHelper.getString("achievement.title.monarchsecret")); 
	//		LanguageRegistry.instance().addStringLocalization("achievement." + "MCA_MonarchSecret.desc", languageID, LanguageHelper.getString("achievement.descr.monarchsecret")); 
	//	}

	/**
	 * Retrieves the specified string from the string translations map. Used when the string being retrieved
	 * is not being spoken by an entity, such as a GUI button or item name.
	 * 
	 * @param	elementId	The ID of the string to retrieve.
	 * @param	arguments	Arguments to use when parsing the string.
	 * 
	 * @return	Returns localized string matching the ID provided.
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
		
		//Loop through each item in the string translations map.
		for (final Map.Entry<String, String> entrySet : translationsMap.entrySet())
		{
			//Check if the entry's key contains the ID.
			if (entrySet.getKey().contains(elementId))
			{
				//Then check if it completely equals the ID.
				if (entrySet.getKey().equals(elementId))
				{
					//In this case, clear the values list and add only the value that equals the ID.
					matchingValues.clear();
					matchingValues.add(entrySet.getValue());
					break;
				}

				else //Otherwise just add the matching ID's value to the matching values list.
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
	 * @return	Returns the language ID last loaded by Minecraft.
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

		catch (FileNotFoundException e) 
		{
			mod.getLogger().log("Could not find options.txt file. Defaulting to English.");
			languageID = "en_US";
		} 

		catch (IOException e)
		{
			RadixCore.getInstance().quitWithException("Error reading from Minecraft options.txt file.", e);
			languageID = null;
		}

		catch (NullPointerException e)
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
}
