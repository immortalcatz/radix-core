/*******************************************************************************
 * WorldPropertiesManager.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import net.minecraft.server.MinecraftServer;

import com.radixshock.radixcore.core.IEnforcedCore;
import com.radixshock.radixcore.core.RadixCore;
import com.radixshock.radixcore.core.UnenforcedCore;
import com.radixshock.radixcore.util.FileSystem;

import cpw.mods.fml.common.FMLCommonHandler;

/**
 * Handles operations that use the WorldProps.properties file.
 */
public class WorldPropertiesManager implements Serializable
{
	public transient IEnforcedCore mod;
	
	private String currentPlayerName = "";
	private String currentWorldName = "";
	private File worldPropertiesFolderPath = null;
	private File worldPropertiesFolder = null;
	private File worldPropertiesFile = null;
	private transient Properties properties = new Properties();
	private transient FileInputStream inputStream   = null;
	private transient FileOutputStream outputStream = null;

	/** The properties and values stored within the world properties file. */
	//public WorldPropertiesList worldProperties = new WorldPropertiesList();
	private transient Class				worldPropertiesClass;

	/** An instance of the class containing mod properties. */
	public Object				worldPropertiesInstance;
	
	/**
	 * Constructor
	 * 
	 * @param 	worldName	The name of the world this manager will be used in.
	 * @param 	playerName	The name of the player this manager belongs to.
	 */
	public WorldPropertiesManager(UnenforcedCore mod, String worldName, String playerName, Class worldPropertiesClass)
	{
		this.mod = mod;
		this.worldPropertiesClass = worldPropertiesClass;
		
		try
		{
			worldPropertiesInstance = worldPropertiesClass.newInstance();
		}

		catch (final Exception e)
		{
			e.printStackTrace();
		}
		
		MinecraftServer server = MinecraftServer.getServer();

		//Assign relevant data.
		currentPlayerName = playerName;
		currentWorldName = worldName;

		if (server.isDedicatedServer())
		{
			worldPropertiesFolderPath   = new File(RadixCore.getInstance().runningDirectory + "/config/" + mod.getShortModName() + "/ServerWorlds/");
			worldPropertiesFolder   	= new File(worldPropertiesFolderPath.getPath() + "/" + worldName + "/" + playerName + "/");
			worldPropertiesFile     	= new File(worldPropertiesFolder.getPath() + "/" + "ServerWorldProps.properties");
		}

		else
		{
			worldPropertiesFolderPath   = new File(RadixCore.getInstance().runningDirectory + "/config/" + mod.getShortModName() + "/Worlds/");
			worldPropertiesFolder   	= new File(worldPropertiesFolderPath.getPath() + "/" + worldName + "/" + playerName + "/");
			worldPropertiesFile     	= new File(worldPropertiesFolder.getPath() + "/" + "WorldProps.properties");
		}

		//Account for issues with Mac's god-awful OS.
		if (worldPropertiesFile.getPath().contains("/.DS_STORE"))
		{
			worldPropertiesFile = new File(worldPropertiesFile.getPath().replace("/.DS_STORE", ""));
		}
		
		//Check and be sure the config/<modshortname>/Worlds folder exists.
		if (!worldPropertiesFolderPath.exists())
		{
			worldPropertiesFolderPath.mkdir();
			mod.getLogger().log("Created Worlds folder.");
		}

		//Now check if the current world has its own properties folder.
		if (!worldPropertiesFolder.exists())
		{
			worldPropertiesFolder.mkdirs();
			mod.getLogger().log("Created new properties folder for the world '" + worldName + "'.");
		}

		//Then check if the world has a properties file within that folder. If it doesn't, a new one is created.
		if (!worldPropertiesFile.exists())
		{
			mod.onCreateNewWorldProperties(this);
			
			saveWorldProperties();
			mod.getLogger().log("Saved new world properties for world '" + worldName + "' and player '" + playerName + "'.");
		}

		//If the world does have its own world properties file, then load it.
		else
		{
			loadWorldProperties();
			mod.getLogger().log("Loaded existing world properties for world '" + worldName + "' and player '" + playerName + "'.");
		}
	}

	/**
	 * Saves all world properties to the WorldProps.properties file.
	 */
	public void saveWorldProperties()
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			try
			{
				properties = new Properties();

				//Put world specific data in the properties.
				for (Field f : worldPropertiesClass.getFields())
				{
					try
					{
						String fieldType = f.getType().toString();

						if (fieldType.contains("List"))
						{
							final List<String> list = (List<String>) f.get(worldPropertiesInstance);
							String stringToSave = "";

							for (String s : list)
							{
								stringToSave += s;

								if (list.indexOf(s) != list.size() - 1)
								{
									stringToSave += ",";
								}
							}

							properties.put(f.getName(), stringToSave);
						}

						else if (fieldType.contains("boolean") || fieldType.contains("int") || fieldType.contains("String"))
						{
							properties.put(f.getName(), f.get(worldPropertiesInstance).toString());
						}
					}

					catch (NullPointerException e)
					{
						mod.getLogger().log(e);
						continue;
					}
				}

				//Store the variables in the properties instance to file.
				outputStream = new FileOutputStream(worldPropertiesFile);
				properties.store(outputStream, mod.getShortModName() + " Properties for World: " + currentWorldName);
				outputStream.close();

				mod.getLogger().log("Saved world properties for player " + currentPlayerName + " in world " + currentWorldName);
				mod.onSaveWorldProperties(this);

				//Load properties again to update them across the client and integrated server.
				loadWorldProperties();

				//Send the properties to the server or client.
				mod.onUpdateWorldProperties(this);
			}

			catch (FileNotFoundException e)
			{
				//Check for the rare "The requested operation cannot be performed on a file with a user-mapped section open"
				//message. Skip saving if it's encountered.
				if (!e.getMessage().contains("user-mapped"))
				{
					RadixCore.getInstance().quitWithException("FileNotFoundException occurred while saving world properties to file.", e);
				}
			}

			catch (IllegalAccessException e)
			{
				RadixCore.getInstance().quitWithException("IllegalAccessException occurred while saving world properties to file.", e);
			}

			catch (IOException e)
			{
				return;
			}

			catch (NullPointerException e)
			{
				mod.getLogger().log(e);
				RadixCore.getInstance().quitWithException("NullPointerException while saving world properties.", e);
			}
		}

		else
		{
			mod.onUpdateWorldProperties(this);
			
			//FIXME
			//mod.getPacketHandler().sendPacketToServer(new PacketSetWorldProperties(this));
		}
	}

	/**
	 * Loads the WorldProps.properties file and assigns the values within to the appropriate fields in the world properties list.
	 */
	public void loadWorldProperties()
	{
		try
		{
			//Clear the properties instance and load the world's properties file.
			properties = new Properties();

			//Load the data from the world properties file into the properties instance.
			inputStream = new FileInputStream(worldPropertiesFile);
			properties.load(inputStream);
			inputStream.close();

			//Loop through all fields prefixed with world_ and assign their value that is in the properties.
			for (Field f : worldPropertiesClass.getFields())
			{
				String fieldType = f.getType().toString();

				//Determine the type of data contained in the field and parse it accordingly, since everything read from
				//the properties instance is a String.
				if (fieldType.contains("boolean"))
				{
					f.set(worldPropertiesInstance, Boolean.parseBoolean(properties.getProperty(f.getName())));
				}

				else if (fieldType.contains("List"))
				{
					String listData = properties.getProperty(f.getName()).toString();

					List<String> list = new ArrayList<String>();

					for (String s : listData.split(","))
					{
						list.add(s);
					}

					f.set(worldPropertiesInstance, list);
				}

				else if (fieldType.contains("int"))
				{
					f.set(worldPropertiesInstance, Integer.parseInt(properties.getProperty(f.getName())));
				}

				else if (fieldType.contains("String"))
				{
					f.set(worldPropertiesInstance, properties.getProperty(f.getName()));
				}
			}

			mod.onLoadWorldProperties(this);
			//.playerWorldManagerMap.put(currentPlayerName, this);
		}

		catch (FileNotFoundException e)
		{
			RadixCore.getInstance().quitWithException("FileNotFoundException occurred while loading world properties from file.", e);
		}

		catch (IllegalAccessException e)
		{
			RadixCore.getInstance().quitWithException("IllegalAccessException occurred while loading world properties from file.", e);
		}

		catch (IOException e)
		{
			RadixCore.getInstance().quitWithException("IOException occurred while loading world properties from file.", e);
		}

		catch (NullPointerException e)
		{
			resetWorldProperties();
		}

		catch (NumberFormatException e)
		{
			resetWorldProperties();
		}
	}

	/**
	 * Resets all world properties back to their default values.
	 */
	public void resetWorldProperties()
	{
		mod.getLogger().log(currentPlayerName + "'s world properties are errored. Resetting back to original settings.");

		try
		{
			worldPropertiesInstance = worldPropertiesClass.newInstance();
		}
		
		catch (Exception e)
		{
			RadixCore.getInstance().quitWithException("Error resetting world properties.", e);
			saveWorldProperties();	
		}
	}

	public String getCurrentPlayerName()
	{
		return currentPlayerName;
	}
	
	/**
	 * Deletes all world properties folders that do not have a folder with the same name in the /saves/ folder.
	 */
	public static void emptyOldWorldProperties(IEnforcedCore callingMod)
	{
		File minecraftSavesFolder = new File(RadixCore.getInstance().runningDirectory + "/saves");
		File configSavesFolder = new File(RadixCore.getInstance().runningDirectory + "/config/" + callingMod.getShortModName() + "/Worlds");

		if (!minecraftSavesFolder.exists())
		{
			minecraftSavesFolder.mkdirs();
		}

		if (!configSavesFolder.exists())
		{
			configSavesFolder.mkdirs();
		}

		List<String> minecraftSaves = Arrays.asList(minecraftSavesFolder.list());
		List<String> configSaves = Arrays.asList(configSavesFolder.list());
		List<String> invalidSaves = new ArrayList<String>();

		for (String configSaveName : configSaves)
		{
			if (!minecraftSaves.contains(configSaveName))
			{
				invalidSaves.add(configSaveName);
			}
		}

		for (String invalidSaveName : invalidSaves)
		{
			callingMod.getLogger().log("Deleted old properties folder: " + invalidSaveName);
			FileSystem.recursiveDeletePath(new File(RadixCore.getInstance().runningDirectory + "/config/" + callingMod.getShortModName() + "/Worlds/" + invalidSaveName));
		}
	}
}
