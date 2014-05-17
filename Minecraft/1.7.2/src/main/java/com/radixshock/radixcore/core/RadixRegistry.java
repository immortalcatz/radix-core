package com.radixshock.radixcore.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Use to register mod items, entities, etc. with the game.
 */
public final class RadixRegistry
{
	public static final class Items
	{
		/**
		 * Creates a new creative tab using the provided item as an icon.
		 * 
		 * @param mod		The mod that this creative tab belongs to. The creative tab's ID will be "tab" plus the mod's short name with all spaces removed.
		 * @param iconItem	The item that will be used as the creative tab's icon. [b]The item will [u]NOT[/u] be registered, [u]or[/u] added to the creative tab.[/b]	
		 * 
		 * @return	The newly created creative tab for the provided mod.
		 */
		public static CreativeTabs registerCreativeTab(IEnforcedCore mod, final Item iconItem)
		{
			CreativeTabs returnTab = null;

			if (iconItem != null)
			{
				returnTab = new CreativeTabs("tab" + mod.getShortModName().replace(" ", ""))
				{
					@Override
					public Item getTabIconItem()
					{
						return iconItem;
					}
				};

				return returnTab;
			}

			else
			{
				throw new NullPointerException("The item intended to be used as an icon has not been initialized.");
			}
		}

		/**
		 * Registers an item with the game registry. The item's unlocalized name will be used at the registered name.
		 * 
		 * @param item	The item to register.
		 */
		public static void registerItem(Item item)
		{
			if (item != null)
			{
				GameRegistry.registerItem(item, item.getUnlocalizedName());
			}

			else
			{
				throw new NullPointerException("An item passed to the registry was null. Ensure all items intended to be registered have been initialized.");
			}
		}
	}

	public static final class Entities
	{
		private static final Map<IEnforcedCore, Integer> registeredEntityIds = new HashMap<IEnforcedCore, Integer>();

		/**
		 * Registers an entity with the entity registry.
		 * Automatically assigns an ID, a simple name, view distance of 50, sets update frequency to 2, and updates velocity.
		 * 
		 * @param 	mod			The mod the entity belongs to.
		 * @param 	entityClass	The class of the entity to register.
		 */
		public static void registerModEntity(IEnforcedCore mod, Class entityClass)
		{
			int id = 0;

			if (registeredEntityIds.containsKey(mod))
			{
				//Increment stored ID by one.
				id = registeredEntityIds.get(mod) + 1;
			}

			//Always update the ID
			registeredEntityIds.put(mod, id);
			EntityRegistry.registerModEntity(entityClass, entityClass.getSimpleName(), id, mod, 50, 2, true);
		}
	}

	public static final class Achievements
	{
		private static final Map<IEnforcedCore, List<Achievement>> registeredAchievements = new HashMap<IEnforcedCore, List<Achievement>>();

		public static Achievement createAchievement(IEnforcedCore mod, String id, int posX, int posY, Item itemIcon, Achievement prerequisiteAchievement)
		{
			final Achievement returnAchievement = new Achievement(id, id, posX, posY * -1, itemIcon, prerequisiteAchievement).registerStat();
			addAchievementToMap(mod, returnAchievement);
			return returnAchievement;
		}

		public static Achievement createAchievement(IEnforcedCore mod, String id, int posX, int posY, Block blockIcon, Achievement prerequisiteAchievement)
		{
			final Achievement returnAchievement = new Achievement(id, id, posX, posY * -1, blockIcon, prerequisiteAchievement).registerStat();
			addAchievementToMap(mod, returnAchievement);
			return returnAchievement;
		}

		public static Achievement createAchievement(IEnforcedCore mod, String id, int posX, int posY, Item itemIcon)
		{
			final Achievement returnAchievement = new Achievement(id, id, posX, posY * -1, itemIcon, null).registerStat();
			addAchievementToMap(mod, returnAchievement);
			return returnAchievement;
		}

		public static Achievement createAchievement(IEnforcedCore mod, String id, int posX, int posY, Block blockIcon)
		{
			final Achievement returnAchievement = new Achievement(id, id, posX, posY * -1, blockIcon, null).registerStat();
			addAchievementToMap(mod, returnAchievement);
			return returnAchievement;
		}

		public static AchievementPage createAchievementPage(IEnforcedCore mod, String buttonName)
		{
			final List<Achievement> achievementList = registeredAchievements.get(mod);
			final AchievementPage returnPage = new AchievementPage(buttonName, achievementList.toArray(new Achievement[achievementList.size()]));
			AchievementPage.registerAchievementPage(returnPage);
			return returnPage;
		}

		public static void exportAchievementsToFile(IEnforcedCore mod, String destination)
		{
			try
			{
				final List<Achievement> achievementList = registeredAchievements.get(mod);
				final FileOutputStream outputStream = new FileOutputStream(destination);
				final Properties properties = new Properties();

				for (Achievement achievement : achievementList)
				{
					properties.put("achievement." + achievement.statId, "");
					properties.put("achievement." + achievement.statId + ".desc", "");
				}

				properties.store(outputStream, "");
				outputStream.close();
			}

			catch (FileNotFoundException e)
			{
				RadixCore.getInstance().quitWithException("Destination directory is malformed or doesn't exist.", e);
			}

			catch (IOException e)
			{
				RadixCore.getInstance().quitWithException("Unexpected IOException when exporting achievements.", e);
			}
		}

		private static void addAchievementToMap(IEnforcedCore mod, Achievement achievement)
		{
			List<Achievement> modAchievements = registeredAchievements.get(mod);

			if (modAchievements == null)
			{
				modAchievements = new ArrayList<Achievement>();
			}

			modAchievements.add(achievement);
			registeredAchievements.put(mod, modAchievements);
		}
	}
}