package net.guipsp.liquidxp.proxies;

import static java.lang.Math.min;
import static java.lang.Math.max;

import buildcraft.core.render.TextureLiquidsFX;

public class TextureLiquidEnchantFX extends TextureLiquidsFX {

	public TextureLiquidEnchantFX(int i, int r, int g, int b) {
		super(max(0, r - 75), min(r - 20, 255), max(0, g - 75),
				min(r - 20, 255), max(0, b - 75), min(r - 20, 255), i,
				ClientProxy.ENCHANT_TEXTURES);
	}

}
