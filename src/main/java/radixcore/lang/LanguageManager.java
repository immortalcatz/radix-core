package radixcore.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringTranslate;
import radixcore.core.RadixCore;
import radixcore.util.RadixExcept;
import radixcore.util.RadixMath;

public class LanguageManager 
{
	private String modId;
	private AbstractLanguageParser parser;
	private Map<String, String> translationsMap;

	public LanguageManager(String providedModId)
	{
		this.modId = providedModId.toLowerCase();
		this.translationsMap = new HashMap<String, String>();
		loadLanguage(getGameLanguageID());
	}

	public LanguageManager(String modId, AbstractLanguageParser parser)
	{
		this(modId);
		this.parser = parser;
	}

	@SideOnly(Side.CLIENT)
	public String getGameLanguageID()
	{
		String languageID = "en_US";

		try
		{
			languageID = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
		}

		catch (final Exception e)
		{
			RadixCore.getLogger().error("Unable to get current language code. Defaulting to English.");
		}

		return languageID;
	}

	public String getString(String id)
	{
		return getString(id, (Object) null);
	}

	public String getString(String id, Object... arguments)
	{
		//Check if the exact provided key exists in the translations map.
		if (translationsMap.containsKey(id))
		{
			//Parse it if a parser was provided.
			if (parser != null)
			{
				return parser.parsePhrase(translationsMap.get(id), arguments);
			}

			else
			{
				return translationsMap.get(id);
			}
		}

		else
		{
			//Build a list of keys that at least contain part of the provided key name.
			List<String> containingKeys = new ArrayList<String>();

			for (String key : translationsMap.keySet())
			{
				if (key.contains(id))
				{
					containingKeys.add(key);
				}
			}

			//Return a random potentially valid key if some were found.
			if (containingKeys.size() > 0)
			{
				String key = containingKeys.get(RadixMath.getNumberInRange(0, containingKeys.size() - 1));

				if (parser != null)
				{
					return parser.parsePhrase(translationsMap.get(key), arguments);
				}

				else
				{
					return translationsMap.get(key);
				}
			}

			else
			{
				RadixCore.getLogger().error("[" + modId + "] No mapping found for requested phrase ID: " + id);
				Throwable trace = new Throwable();
				RadixExcept.logErrorCatch(trace, "Stacktrace for non-fatal error.");
				return id;
			}
		}
	}

	/**
	 * @return	The number of phrases containing the provided string in their ID.
	 */
	public int getNumberOfPhrasesMatchingID(String id)
	{
		List<String> containingKeys = new ArrayList<String>();

		for (String key : translationsMap.keySet())
		{
			if (key.contains(id))
			{
				containingKeys.add(key);
			}
		}

		return containingKeys.size();
	}

	public void loadLanguage(String languageId)
	{
		//Remove old translations.
		translationsMap.clear();
		
		//Set up properties instance.
		Properties properties = new Properties();
		
		//Can only load languages client-side.
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			try
			{
				properties.load(StringTranslate.class.getResourceAsStream("/assets/" + modId + "/lang/" + languageId + ".lang"));
			}

			catch (Exception e)
			{
				RadixCore.getLogger().error("Error loading language " + languageId + " for " + modId + ". Attempting to default to English.");

				try
				{
					properties.load(StringTranslate.class.getResourceAsStream("/assets/" + modId + "/lang/" + "en_US.lang"));
				}

				catch (Exception e2)
				{
					RadixExcept.logFatalCatch(e2, "Error loading language " + languageId + " for mod " + modId + ".");
				}
			}
		}

		else
		{
			try
			{
				properties.load(StringTranslate.class.getResourceAsStream("/assets/" + modId + "/lang/" + "en_US.lang"));
			}

			catch (Exception e)
			{
				RadixExcept.logFatalCatch(e, "Error loading language server-side. Loading cannot continue.");
			}
		}

		for (final Map.Entry<Object, Object> entrySet : properties.entrySet())
		{
			translationsMap.put(entrySet.getKey().toString(), entrySet.getValue().toString());
		}
	}
}
