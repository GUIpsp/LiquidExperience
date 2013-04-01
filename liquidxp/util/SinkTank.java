package net.guipsp.liquidxp.util;

import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

public class SinkTank extends LiquidTank {

	public SinkTank(int capacity) {
		super(capacity);
	}

	@Override
	public LiquidStack drain(int maxDrain, boolean doDrain) {
		return null;
	}

	public LiquidStack forceDrain(int maxDrain, boolean doDrain) {
		return super.drain(maxDrain, doDrain);
	}

}
