package radixcore.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
/**
 * Utility class used for easy migration to/from 1.8 where block operations are considered.
 */
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.World;
import radixcore.math.Point3D;
import radixcore.math.Point3D;

public final class BlockHelper 
{
	public static void setBlock(World world, Point3D point, Block block)
	{
		setBlock(world, point.iPosX, point.iPosY, point.iPosZ, block.getDefaultState());
	}
	
	public static void setBlock(World world, int posX, int posY, int posZ, Block block)
	{
		setBlock(world, posX, posY, posZ, block.getDefaultState());
	}
	
	public static void setBlock(World world, int posX, int posY, int posZ, IBlockState state)
	{
		world.setBlockState(new BlockPos(posX, posY, posZ), state, 2);
	}

	public static Block getBlock(World world, Point3D point)
	{
		return getBlock(world, point.iPosX, point.iPosY, point.iPosZ);
	}
	
	public static Block getBlock(World world, int posX, int posY, int posZ)
	{
		return world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
	}

	public static void setBlockMetadataWithNotify(World world, int posX, int posY, int posZ, IBlockState blockState, int flags)
	{
		world.setBlockState(new BlockPos(posX, posY, posZ), blockState, flags);
	}

	public static int getBlockMetadata(World world, Point3D point)
	{
		return getBlockMetadata(world, point.iPosX, point.iPosY, point.iPosZ);
	}
	
	public static int getBlockMetadata(World world, int posX, int posY, int posZ)
	{
		IBlockState state = world.getBlockState(new BlockPos(posX, posY, posZ));
		return state.getBlock().getMetaFromState(state);
	}
	
	public static void updateFurnaceState(boolean stateValue, World world, int posX, int posY, int posZ)
	{
		BlockFurnace.setState(stateValue, world, new BlockPos(posX, posY, posZ));
	}
	
	public static boolean doesBlockHaveSolidTopSurface(World world, int posX, int posY, int posZ)
	{
		final BlockPos pos = new BlockPos(posX, posY, posZ);
		return world.getBlockState(pos).isSideSolid(world, pos, EnumFacing.UP);
	}
	
	public static TileEntity getTileEntity(World world, int posX, int posY, int posZ)
	{
		return world.getTileEntity(new BlockPos(posX, posY, posZ));
	}
	
	public static boolean canBlockSeeTheSky(World world, int posX, int posY, int posZ)
	{
		return world.canBlockSeeSky(new BlockPos(posX, posY, posZ));
	}
	
	public static void setBlock(World world, BlockPos pos, Block block)
	{
		setBlock(world, pos.getX(), pos.getY(), pos.getZ(), block);
	}
	
	private BlockHelper()
	{
	}
}
