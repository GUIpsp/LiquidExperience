package net.guipsp.liquidxp.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.guipsp.liquidxp.LiquidExperience;
import net.guipsp.liquidxp.gui.GuiXpPurifier;
import net.guipsp.liquidxp.gui.GuiXpPurifier.LContainer;
import net.guipsp.liquidxp.packets.Packet1RequestXPPurifierGUI;
import net.guipsp.liquidxp.packets.PacketHandler;
import net.guipsp.liquidxp.proxies.ClientProxy;
import net.guipsp.liquidxp.proxies.TextureLiquidXPFX;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

public class XPPurifier extends Block {

	public XPPurifier(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public boolean hasTileEntity(int i) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3,
			int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		Packet1RequestXPPurifierGUI packet = new Packet1RequestXPPurifierGUI();
		packet.worldId = par1World.provider.dimensionId;
		packet.coordinates = new int[] { par2, par3, par4 };
		PacketHandler.dataify(packet);
		PacketDispatcher.sendPacketToServer(packet);
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new XPPurifierTile();
	}

	@Override
	public int getBlockTextureFromSide(int par1) {
		return par1 == 0 ? this.blockIndexInTexture + 2
				: (par1 == 1 ? this.blockIndexInTexture + 1
						: this.blockIndexInTexture);
	}

	@Override
	public String getTextureFile() {
		return ClientProxy.TEXTURES;
	}

}
