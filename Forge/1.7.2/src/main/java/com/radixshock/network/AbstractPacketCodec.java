package com.radixshock.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.radixshock.core.IMod;
import com.radixshock.core.RadixCore;

public abstract class AbstractPacketCodec 
{
	private IMod mod;
	
	public AbstractPacketCodec(IMod mod)
	{
		this.mod = mod;
	}
	
	public abstract void encode(Packet packet, ChannelHandlerContext context, ByteBuf buffer);

	public abstract void decode(Packet packet, ChannelHandlerContext context, ByteBuf buffer);

	protected byte[] convertToByteArray(Object obj)
	{
		final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

		try
		{
			final ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			objectOutput.writeObject(obj);
			objectOutput.close();
		}

		catch (IOException e)
		{
			mod.getLogger().log(e);
		}

		return byteOutput.toByteArray();
	}

	protected Object convertBytesToObject(byte[] byteArray)
	{
		final ByteArrayInputStream byteInput = new ByteArrayInputStream(byteArray);
		Object returnObject = null;

		try
		{
			final ObjectInputStream objectInput = new ObjectInputStream(byteInput);

			returnObject = objectInput.readObject();
			objectInput.close();
		}

		catch (IOException e)
		{
			mod.getLogger().log(e);
		}

		catch (ClassNotFoundException e)
		{
			mod.getLogger().log(e);
		}

		return returnObject;
	}

	/**
	 * Deflates a byte array.
	 * 
	 * @param 	input	The byte array to be deflated.
	 * 
	 * @return	Deflated byte array.
	 */
	protected byte[] compress(byte[] input)
	{
		try
		{
			final Deflater deflater = new Deflater();
			deflater.setLevel(Deflater.BEST_COMPRESSION);
			deflater.setInput(input);

			final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(input.length);
			deflater.finish();

			final byte[] buffer = new byte[1024];

			while(!deflater.finished())
			{
				final int count = deflater.deflate(buffer);
				byteOutput.write(buffer, 0, count);
			}

			byteOutput.close();
			return byteOutput.toByteArray();
		}

		catch (IOException e)
		{
			RadixCore.getInstance().quitWithException("Error compressing byte array.", e);
			return null;
		}
	}

	/**
	 * Inflates a deflated byte array.
	 * 
	 * @param 	input	The byte array to be deflated.
	 * 
	 * @return	Decompressed byte array.
	 */
	protected byte[] decompress(byte[] input)
	{
		try
		{
			final Inflater inflater = new Inflater();
			final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(input.length);
			final byte[] buffer = new byte[1024];
			inflater.setInput(input);

			while(!inflater.finished())
			{
				final int count = inflater.inflate(buffer);
				byteOutput.write(buffer, 0, count);
			}

			byteOutput.close();
			return byteOutput.toByteArray();
		}

		catch (DataFormatException e)
		{
			RadixCore.getInstance().quitWithException("Error decompressing byte array.", e);
			return null;
		}

		catch (IOException e)
		{
			RadixCore.getInstance().quitWithException("Error decompressing byte array.", e);
			return null;
		}
	}
	
	protected void writeByteArray(ByteBuf buffer, byte[] byteArray)
	{
		final byte[] compressedArray = compress(byteArray);
		buffer.writeInt(compressedArray.length);
		buffer.writeBytes(compressedArray);
	}
	
	protected byte[] readByteArray(ByteBuf buffer)
	{
		final int arraySize = buffer.readInt();
		return decompress(buffer.readBytes(arraySize).array());
	}
	
	protected void writeObject(ByteBuf buffer, Object object)
	{
		writeByteArray(buffer, convertToByteArray(object));
	}
	
	protected Object readObject(ByteBuf buffer)
	{
		 return convertBytesToObject(readByteArray(buffer));
	}
}
