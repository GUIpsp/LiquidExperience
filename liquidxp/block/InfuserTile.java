package net.guipsp.liquidxp.block;

import java.util.Map;
import java.util.Map.Entry;

import buildcraft.api.inventory.ISpecialInventory;
import net.guipsp.liquidxp.LiquidExperience;
import net.guipsp.liquidxp.liquids.LiquidEnchantment;
import net.guipsp.liquidxp.liquids.LiquidXP;
import net.guipsp.liquidxp.util.SinkTank;
import net.guipsp.liquidxp.util.SourceTank;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentArrowDamage;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

public class InfuserTile extends TileEntityEnchantmentTable implements
		ITankContainer, ISpecialInventory {

	public int level = 1;

	private void updateEnch() {
		try {
			for (Entry<Enchantment, LiquidEnchantment> pair : LiquidExperience.liquidEnchants
					.entrySet()) {
				if (tank.getLiquid().itemID == pair.getValue().itemID) {
					currench = pair.getKey();
				}
			}
			level = LiquidExperience.clamp(level, 1, currench.getMaxLevel());
		} catch (NullPointerException e) {

		}
	}

	@Override
	public void readFromNBT(NBTTagCompound data) {
		tank.setLiquid(LiquidStack.loadLiquidStackFromNBT(data
				.getCompoundTag("Tank")));
		stack = ItemStack.loadItemStackFromNBT(data.getCompoundTag("Stack"));
		level = data.getInteger("Level");
	}

	public Enchantment currench;

	@Override
	public void writeToNBT(NBTTagCompound data) {
		if (tank.getLiquid() != null) {
			data.setCompoundTag("Tank",
					tank.getLiquid().writeToNBT(new NBTTagCompound()));
		}
		if (stack != null) {
			data.setCompoundTag("Stack", stack.writeToNBT(new NBTTagCompound()));
		}
		data.setInteger("Level", level);
	}

	public SinkTank tank = new SinkTank(50000);

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		boolean update = false;
		if (tank.getLiquid() == null || tank.getLiquid().amount == 0) {
			update = true;
		}
		int filled = tank.fill(resource, doFill);
		if (update) {
			updateEnch();
		}
		return filled;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		boolean update = false;
		if (tank.getLiquid() == null || tank.getLiquid().amount == 0) {
			update = true;
		}
		int filled = tank.fill(resource, doFill);
		if (update) {
			updateEnch();
		}
		return filled;
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

	ItemStack stack = null;

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return stack;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		return stack.splitStack(var2);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		stack = var2;
	}

	@Override
	public String getInvName() {
		return "";
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return false;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public int addItem(ItemStack stack, boolean doAdd, ForgeDirection from) {
		if (this.stack == null || this.stack.stackSize == 0) {
			if (doAdd) {
				this.stack = stack.copy().splitStack(1);
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public ItemStack[] extractItem(boolean doRemove, ForgeDirection from,
			int maxItemCount) {
		try {
			if (stack.stackSize < 1) {
				stack = null;
			}
		} catch (NullPointerException n) {
		}
		ItemStack s = doEnchant();
		if (doRemove && s != null) {
			stack.splitStack(1);
		}
		return new ItemStack[] { s };
	}

	public ItemStack doEnchant() {
		try {
			ItemStack stack = this.stack.copy();
			if (currench.canApplyAtEnchantingTable(stack)) {
				Map enchlist = EnchantmentHelper.getEnchantments(stack);
				for (Object entryy : enchlist.entrySet()) {
					Map.Entry entry = (Entry) entryy;
					if (!Enchantment.enchantmentsList[(Integer) entry.getKey()]
							.canApplyTogether(currench)
							&& Enchantment.enchantmentsList[(Integer) entry
									.getKey()].effectId != currench.effectId) {
						return stack;
					}

				}
				try {
					if (tank.getLiquid().amount < 1000 * level) {
						return null;
					}
				} catch (NullPointerException n) {
					return null;
				}
				tank.forceDrain(1000, true);
				Map enchantments = EnchantmentHelper.getEnchantments(stack);
				enchantments.remove(currench.effectId);
				enchantments.put(currench.effectId, level);
				EnchantmentHelper.setEnchantments(enchantments, stack);
				return stack;
			} else {
				return stack;
			}
		} catch (NullPointerException n) {
			return null;
		}
	}
}
