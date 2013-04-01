package net.guipsp.liquidxp.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.guipsp.liquidxp.LiquidExperience;
import net.guipsp.liquidxp.block.InfuserTile;
import net.guipsp.liquidxp.block.XPPurifierTile;
import net.minecraft.world.WorldServer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;

public class Packet6UpdateInfuser extends LiquidExpPacket {
	public int ID = 6;
	public int level;
	public int worldId;
	public int[] coordinates;

	@Override
	public void doPacket(DataInputStream data, Player playerFML)
			throws IOException {
		readPacketData(data);
		WorldServer world = FMLCommonHandler.instance()
				.getMinecraftServerInstance().worldServerForDimension(worldId);
		if (world.getChunkFromBlockCoords(coordinates[0], coordinates[2]).isChunkLoaded) {
			InfuserTile tent = (InfuserTile) world.getBlockTileEntity(
					coordinates[0], coordinates[1], coordinates[2]);
			try {
				tent.level = LiquidExperience.clamp(level, 1,
						tent.currench.getMaxLevel());
			} catch (NullPointerException n) {
				tent.level = level;
			}
		}

	}

	@Override
	public void readPacketData(DataInputStream par1DataInputStream)
			throws IOException {
		worldId = par1DataInputStream.readInt();
		coordinates = new int[] { par1DataInputStream.readInt(),
				par1DataInputStream.readInt(), par1DataInputStream.readInt() };
		level = par1DataInputStream.readInt();
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
	}
}
