package net.guipsp.liquidxp.util;

import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

public class SourceTank extends LiquidTank {

	public SourceTank(int cap) {
		super(cap);
	}

	public int fill(LiquidStack resource, boolean doFill) {
		return 0;
	}

	public int forceFill(LiquidStack resource, boolean doFill) {
		return super.fill(resource, doFill);
	}
}