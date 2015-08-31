package radixcore.update;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import cpw.mods.fml.common.FMLCommonHandler;
import radixcore.core.ModMetadataEx;
import radixcore.core.RadixCore;
import radixcore.util.RadixExcept;

public class RDXUpdateProtocol implements IUpdateProtocol
{
	private Socket connectSocket;
	
	@Override
	public UpdateData getUpdateData(ModMetadataEx modData) 
	{
		String minecraftVersion = "1.7.10";
		
		String url = "http://files.radix-shock.com/get-xml-property.php?modName=%modName%&mcVersion=%mcVersion%&xmlProperty=version";
		url = url.replace("%modName%", modData.modId).replace("%mcVersion%", minecraftVersion);
		
		try
		{
			UpdateData data = new UpdateData();
			String response = readStringFromURL(url);

			data.minecraftVersion = minecraftVersion;
			data.modVersion = response;

			return data;
		}
		
		catch (Exception e)
		{
			RadixExcept.logErrorCatch(e, "Error checking for updates for " + modData.modId);
			return null;
		}
	}

	@Override
	public void cleanUp() 
	{
		if (connectSocket != null && !connectSocket.isClosed())
		{
			try
			{
				connectSocket.close();
			}
			
			catch (IOException e)
			{
				RadixCore.getLogger().error("Unexpected exception while cleaning up update checker. Error was: " + e.getMessage());
			}
		}
	}
	
	private static String readStringFromURL(String urlString) throws IOException
	{
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		connection.connect();

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String output = in.readLine();
		in.close();
		
		return output;
	}
}
