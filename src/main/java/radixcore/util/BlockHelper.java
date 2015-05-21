package radixcore.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import radixcore.math.Point3D;

public final class BlockHelper 
{
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
		world.setBlock(posX, posY, posZ, block, meta, 2);
	}

	public static Block getBlock(World world, Point3D point)
	{
		return getBlock(world, point.iPosX, point.iPosY, point.iPosZ);
	}
	
	public static Block getBlock(World world, int posX, int posY, int posZ)
	{
		return world.getBlock(posX, posY, posZ);
	}

	public static void setBlockMetadataWithNotify(World world, int posX, int posY, int posZ, int meta, int flags)
	{
		world.setBlockMetadataWithNotify(posX, posY, posZ, meta, flags);
	}
	
	public static int getBlockMetadata(World world, Point3D point)
	{
		return getBlockMetadata(world, point.iPosX, point.iPosY, point.iPosZ);
	}
	
	public static int getBlockMetadata(World world, int posX, int posY, int posZ)
	{
		return world.getBlockMetadata(posX, posY, posZ);
	}
	
	public static void updateFurnaceState(boolean stateValue, World world, int posX, int posY, int posZ)
	{
		BlockFurnace.updateFurnaceBlockState(stateValue, world, posX, posY, posZ);
	}
	
	public static boolean doesBlockHaveSolidTopSurface(World world, int posX, int posY, int posZ)
	{
		return world.doesBlockHaveSolidTopSurface(world, posX, posY, posZ);
	}
	
	public static TileEntity getTileEntity(World world, int posX, int posY, int posZ)
	{
		return world.getTileEntity(posX, posY, posZ);
	}
	
	public static boolean canBlockSeeTheSky(World world, int posX, int posY, int posZ)
	{
		return world.canBlockSeeTheSky(posX, posY, posZ);
	}
	
	private BlockHelper()
	{
	}
}
