package com.radixshock.radixcore.core;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Use to register mod items, entities, etc. with the game.
 */
public final class RadixRegistry
{
	private static final Map<IEnforcedCore, Integer> registeredEntityIds = new HashMap<IEnforcedCore, Integer>();

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