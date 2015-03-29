package radixcore.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemEffect extends Item
{
	@Override
	public boolean hasEffect(ItemStack itemStack) 
	{
		return true;
	}
}
