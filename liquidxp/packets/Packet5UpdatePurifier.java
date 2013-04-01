package net.guipsp.liquidxp.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.guipsp.liquidxp.LiquidExperience;
import net.guipsp.liquidxp.block.XPPurifierTile;
import net.minecraft.world.WorldServer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;

public class Packet5UpdatePurifier extends LiquidExpPacket {
	public int ID = 5;
	public int level;
	public boolean action;
	public boolean decrease;
	public int worldId;
	public int[] coordinates;

	@Override
	public void doPacket(DataInputStream data, Player playerFML)
			throws IOException {
		readPacketData(data);
		WorldServer world = FMLCommonHandler.instance()
				.getMinecraftServerInstance().worldServerForDimension(worldId);
		if (world.getChunkFromBlockCoords(coordinates[0], coordinates[2]).isChunkLoaded) {
			XPPurifierTile tent = (XPPurifierTile) world.getBlockTileEntity(
					coordinates[0], coordinates[1], coordinates[2]);
			if (action) {
				if (decrease) {
					tent.prevEnchant();
				} else {
					tent.nextEnchant();
				}
			}
			tent.level = LiquidExperience.clamp(level, 1, 30);
		}

	}

	@Override
	public void readPacketData(DataInputStream par1DataInputStream)
			throws IOException {
		worldId = par1DataInputStream.readInt();
		coordinates = new int[] { par1DataInputStream.readInt(),
				par1DataInputStream.readInt(), par1DataInputStream.readInt() };
		level = par1DataInputStream.readInt();
		action = par1DataInputStream.readBoolean();
		decrease = par1DataInputStream.readBoolean();
	}

	@Override
	public void writePacketData(DataOutputStream par1DataOutputStream)
			throws IOException {
		par1DataOutputStream.writeInt(ID);
		par1DataOutputStream.writeInt(worldId);
		par1DataOutputStream.writeInt(coordinates[0]);
		par1DataOutputStream.writeInt(coordinates[1]);
		par1DataOutputStream.writeInt(coordinates[2]);

		par1DataOutputStream.writeInt(level);
		par1DataOutputStream.writeBoolean(action);
		par1DataOutputStream.writeBoolean(decrease);
	}
}
