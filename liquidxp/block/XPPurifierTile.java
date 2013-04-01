package net.guipsp.liquidxp.block;

import java.util.List;
import java.util.Random;

import net.guipsp.liquidxp.LiquidExperience;
import net.guipsp.liquidxp.liquids.LiquidEnchantment;
import net.guipsp.liquidxp.liquids.LiquidXP;
import net.guipsp.liquidxp.util.SinkTank;
import net.guipsp.liquidxp.util.SourceTank;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

public class XPPurifierTile extends TileEntity implements ITankContainer {

	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		tankarray[0].setLiquid(LiquidStack.loadLiquidStackFromNBT(data
				.getCompoundTag("uptank")));
		tankarray[1].setLiquid(LiquidStack.loadLiquidStackFromNBT(data
				.getCompoundTag("downtank")));

		String ench = data.getString("Enchant");
		for (int i = 0; i < LiquidExperience.enchantlist.size(); i++) {
			Enchantment enchant = LiquidExperience.enchantlist.get(i);
			if (enchant.getName().equals(ench)) {
				currentEnchant = enchant;
				currindex = i;
				break;
			}
		}
		level = data.getInteger("Level");
	}

	@Override
	public void writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		if (tankarray[0].getLiquid() != null) {
			data.setCompoundTag("uptank",
					tankarray[0].getLiquid().writeToNBT(new NBTTagCompound()));
		}
		if (tankarray[1].getLiquid() != null) {
			data.setCompoundTag("downtank", tankarray[1].getLiquid()
					.writeToNBT(new NBTTagCompound()));
		}
		data.setString("Enchant", currentEnchant.getName());
		data.setInteger("Level", level);
	}

	int currindex = 0;

	public void nextEnchant() {
		currindex++;
		if (currindex >= LiquidExperience.enchantlist.size()) {
			currindex = 0;
		}
		currentEnchant = LiquidExperience.enchantlist.get(currindex);
	}

	public void prevEnchant() {
		currindex--;
		if (currindex < 0) {
			currindex = LiquidExperience.enchantlist.size() - 1;
		}
		currentEnchant = LiquidExperience.enchantlist.get(currindex);
	}

	public LiquidTank[] tankarray = new LiquidTank[] { new SinkTank(60000) {

		@Override
		public int fill(LiquidStack resource, boolean doFill) {
			if (resource.asItemStack().getItem() instanceof LiquidXP) {
				checkEnchantConversion();
				return super.fill(resource, doFill);
			} else {
				return 0;
			}
		}

	}, new SourceTank(60000) };

	public Enchantment currentEnchant = (Enchantment) LiquidExperience.enchantlist
			.get(0);

	public int level = 30;

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		if (from == ForgeDirection.UP) {
			int a = tankarray[0].fill(resource, doFill);
			return a;
		} else if (from == ForgeDirection.DOWN) {
			return tankarray[1].fill(resource, doFill);
		} else {
			return 0;
		}
	}

	private void checkEnchantConversion() {
		try {
			while (((SinkTank) tankarray[0]).forceDrain(level, false).amount == level
					&& ((SourceTank) tankarray[1]).forceFill(
							new LiquidStack(LiquidExperience.liquidEnchants
									.get(currentEnchant), currentEnchant
									.getMaxLevel()), false) == currentEnchant
							.getMaxLevel()) {
				((SinkTank) tankarray[0]).forceDrain(level, true);
				@SuppressWarnings("unchecked")
				List<EnchantmentData> enchlist = EnchantmentHelper
						.buildEnchantmentList(new Random(), new ItemStack(
								Item.book, 1), level);
				for (EnchantmentData enchantmentData : enchlist) {
					if (enchantmentData.enchantmentobj.equals(currentEnchant)) {
						((SourceTank) tankarray[1]).forceFill(
								new LiquidStack(LiquidExperience.liquidEnchants
										.get(currentEnchant), level
										* enchantmentData.enchantmentLevel),
								true);

					}
				}
			}
		} catch (NullPointerException n) {
		}
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return tankarray[tankIndex].fill(resource, doFill);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (from == ForgeDirection.UP) {
			return tankarray[0].drain(maxDrain, doDrain);
		} else if (from == ForgeDirection.DOWN) {
			return tankarray[1].drain(maxDrain, doDrain);
		} else {
			return null;
		}
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		return tankarray[tankIndex].drain(maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		if (direction == ForgeDirection.UP) {
			return new ILiquidTank[] { tankarray[0] };
		} else if (direction == ForgeDirection.DOWN) {
			return new ILiquidTank[] { tankarray[1] };
		} else {
			return null;
		}
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		if (direction == ForgeDirection.UP
				&& type.asItemStack().getItem() instanceof LiquidEnchantment) {
			return tankarray[0];
		} else if (direction == ForgeDirection.DOWN
				&& type.asItemStack().getItem() instanceof LiquidXP) {
			return tankarray[1];
		} else {
			return null;
		}
	}

}
