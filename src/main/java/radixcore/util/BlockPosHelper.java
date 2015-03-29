package radixcore.util;

import radixcore.math.Point3D;
import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public final class BlockPosHelper 
{
	public static void setBlock(World world, BlockPos pos, Block block)
	{
		setBlock(world, pos.getX(), pos.getY(), pos.getZ(), block, 1);
	}

	public static void setBlock(World world, Point3D point, Block block)
	{
		setBlock(world, point.iPosX, point.iPosY, point.iPosZ, block, 1);
	}
	
	public static void setBlock(World world, int posX, int posY, int posZ, Block block)
	{
		setBlock(world, posX, posY, posZ, block, 1);
	}
	
	public static void setBlock(World world, int posX, int posY, int posZ, Block block, int meta)
	{
		world.setBlockState(new BlockPos(posX, posY, posZ), block.getStateFromMeta(meta));
	}
	
	public static Block getBlock(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock();
	}
	
	public static Block getBlock(World world, Point3D point)
	{
		return getBlock(world, point.toBlockPos());
	}
	
	public static Block getBlock(World world, int posX, int posY, int posZ)
	{
		return getBlock(world, new BlockPos(posX, posY, posZ));
	}
	
	public static int getBlockMetadata(World world, BlockPos pos)
	{
		return Block.getStateId(world.getBlockState(pos));
	}
	
	public static int getBlockMetadata(World world, Point3D point)
	{
		return getBlockMetadata(world, point.toBlockPos());
	}
	
	public static int getBlockMetadata(World world, int posX, int posY, int posZ)
	{
		return getBlockMetadata(world, new BlockPos(posX, posY, posZ));
	}
	
	private BlockPosHelper()
	{
	}
}
