package net.guipsp.liquidxp.block;

import net.guipsp.liquidxp.LiquidExperience;
import net.guipsp.liquidxp.liquids.LiquidEnchantment;
import net.guipsp.liquidxp.liquids.LiquidXP;
import net.guipsp.liquidxp.util.SourceTank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

public class XPLiquifierTile extends TileEntity implements ITankContainer {

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		tank.setLiquid(LiquidStack.loadLiquidStackFromNBT(par1nbtTagCompound));
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
		try {
			tank.getLiquid().writeToNBT(par1nbtTagCompound);
		} catch (NullPointerException e) {
		}
	}

	public SourceTank tank = new SourceTank(50000);

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return new ILiquidTank[] { tank };
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		if (type.asItemStack().getItem() instanceof LiquidXP) {
			return tank;
		} else {
			return null;
		}
	}

}
