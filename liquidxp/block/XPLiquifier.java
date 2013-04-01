package net.guipsp.liquidxp.block;

import net.guipsp.liquidxp.LiquidExperience;
import net.guipsp.liquidxp.proxies.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquid;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

public class XPLiquifier extends Block {

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3,
			int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		int toFill = 1000;
		XPLiquifierTile tileEnt = (XPLiquifierTile) par1World
				.getBlockTileEntity(par2, par3, par4);
		boolean canFill = tileEnt.tank.forceFill(new LiquidStack(
				LiquidExperience.liquidXP, toFill), false) == toFill;
		if (par5EntityPlayer.experienceLevel >= 1 && canFill) {
			par5EntityPlayer.experienceLevel--;
			tileEnt.tank.forceFill(new LiquidStack(LiquidExperience.liquidXP,
					toFill), true);
			return true;
		}

		return false;
	}

	public XPLiquifier(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public boolean hasTileEntity(int i) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new XPLiquifierTile();
	}

	@Override
	public int getBlockTextureFromSide(int side) {
		return super.getBlockTextureFromSide(side) + (side == 1 ? 1 : 0);
	}

	@Override
	public String getTextureFile() {
		return ClientProxy.TEXTURES;
	}

}
