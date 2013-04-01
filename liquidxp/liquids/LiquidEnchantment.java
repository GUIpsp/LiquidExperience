package net.guipsp.liquidxp.liquids;

import net.guipsp.liquidxp.proxies.ClientProxy;
import net.minecraft.item.Item;
import net.minecraftforge.liquids.ILiquid;

public class LiquidEnchantment extends Item implements ILiquid {

	
	
	public LiquidEnchantment(int id) {
		super(id);
		setTextureFile(ClientProxy.ENCHANT_TEXTURES);
		setIconIndex(9);
	}

	@Override
	public int stillLiquidId() {
		return itemID;
	}

	@Override
	public boolean isMetaSensitive() {
		return false;
	}

	@Override
	public int stillLiquidMeta() {
		return 0;
	}

}
