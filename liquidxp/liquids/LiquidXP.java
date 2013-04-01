package net.guipsp.liquidxp.liquids;

import net.guipsp.liquidxp.proxies.ClientProxy;
import net.minecraft.item.Item;
import net.minecraftforge.liquids.ILiquid;

public class LiquidXP extends Item implements ILiquid {

	public LiquidXP(int id) {
		super(id);
		setTextureFile(ClientProxy.TEXTURES);
		setIconIndex(5);
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
