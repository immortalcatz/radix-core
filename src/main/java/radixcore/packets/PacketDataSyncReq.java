package radixcore.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import radixcore.data.DataWatcherEx;
import radixcore.data.IWatchable;
import radixcore.util.RadixExcept;

public class PacketDataSyncReq extends AbstractPacket implements IMessage, IMessageHandler<PacketDataSyncReq, IMessage>
{
	private int entityId;
	
	public PacketDataSyncReq()
	{
	}

	public PacketDataSyncReq(int entityId)
	{
		this.entityId = entityId;
	}

	@Override
	public void fromBytes(ByteBuf byteBuf)
	{
		this.entityId = byteBuf.readInt();
	}

	@Override
	public void toBytes(ByteBuf byteBuf)
	{
		byteBuf.writeInt(this.entityId);
	}

	@Override
	public IMessage onMessage(PacketDataSyncReq packet, MessageContext context)
	{
		try
		{
			IWatchable watchable = (IWatchable) context.getServerHandler().playerEntity.worldObj.getEntityByID(packet.entityId);
			DataWatcherEx dataWatcherEx = watchable.getDataWatcherEx();
			
			return new PacketDataSync(packet.entityId, dataWatcherEx);
		}
		
		catch (Throwable e)
		{
			RadixExcept.logErrorCatch(e, "Error sending sync data to client.");
		}
		
		return null;
	}
}
