package net.guipsp.liquidxp.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.guipsp.liquidxp.block.XPPurifierTile;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class Packet1RequestXPPurifierGUI extends LiquidExpPacket {

	public int ID = 1;

	public int worldId;
	public int[] coordinates;

	public Packet1RequestXPPurifierGUI() {
	}

	@Override
	public void doPacket(DataInputStream data, Player playerFML)
			throws IOException {
		readPacketData(data);
		WorldServer world = FMLCommonHandler.instance()
				.getMinecraftServerInstance().worldServerForDimension(worldId);
		if (world.getChunkFromBlockCoords(coordinates[0], coordinates[2]).isChunkLoaded) {
			XPPurifierTile tent = (XPPurifierTile) world.getBlockTileEntity(
					coordinates[0], coordinates[1], coordinates[2]);
			Packet2RespondPurifierGUI response = new Packet2RespondPurifierGUI();

			try {
				response.l1a = ((float) tent.tankarray[0].getLiquid().amount)
						/ tent.tankarray[0].getCapacity();
			} catch (NullPointerException p) {
				response.l1a = 0;
			}
			try {
				response.l2 = tent.tankarray[1].getLiquid().itemID;
				response.l2a = ((float) tent.tankarray[1].getLiquid().amount)
						/ tent.tankarray[1].getCapacity();
			} catch (NullPointerException p) {
				response.l2 = 0;
				response.l2a = 0;
			}
			response.currentLevel = tent.level;
			response.worldId = worldId;
			response.coordinates = coordinates;
			response.currEnch = tent.currentEnchant.getTranslatedName(1);
			PacketHandler.dataify(response);
			PacketDispatcher.sendPacketToPlayer(response, playerFML);
		}

	}

	@Override
	public void readPacketData(DataInputStream par1DataInputStream)
			throws IOException {
		worldId = par1DataInputStream.readInt();
		coordinates = new int[] { par1DataInputStream.readInt(),
				par1DataInputStream.readInt(), par1DataInputStream.readInt() };
	}

	@Override
	public void writePacketData(DataOutputStream par1DataOutputStream)
			throws IOException {
		par1DataOutputStream.writeInt(ID);
		par1DataOutputStream.writeInt(worldId);
		par1DataOutputStream.writeInt(coordinates[0]);
		par1DataOutputStream.writeInt(coordinates[1]);
		par1DataOutputStream.writeInt(coordinates[2]);
	}

}
