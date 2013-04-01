package net.guipsp.liquidxp.block;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.guipsp.liquidxp.LiquidExperience;
import net.guipsp.liquidxp.packets.Packet1RequestXPPurifierGUI;
import net.guipsp.liquidxp.packets.Packet3RequestInfuserGUI;
import net.guipsp.liquidxp.packets.PacketHandler;
import net.guipsp.liquidxp.proxies.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnchantmentTable;
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

public class Infuser extends BlockEnchantmentTable {

	public Infuser(int par1) {
		super(par1);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
		return getBlockTextureFromSide(par1);
	}

	@Override
	public int getBlockTextureFromSide(int par1) {
		return par1 == 0 ? this.blockIndexInTexture + 2
				: (par1 == 1 ? this.blockIndexInTexture
						: this.blockIndexInTexture + 1);
	}

	@Override
	public boolean hasTileEntity(int i) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3,
			int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		Packet3RequestInfuserGUI packet = new Packet3RequestInfuserGUI();
		packet.worldId = par1World.provider.dimensionId;
		packet.coordinates = new int[] { par2, par3, par4 };
		PacketHandler.dataify(packet);
		PacketDispatcher.sendPacketToServer(packet);
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new InfuserTile();
	}

	@Override
	public String getTextureFile() {
		return ClientProxy.TEXTURES;
	}

}
