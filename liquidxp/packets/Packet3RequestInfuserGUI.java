package net.guipsp.liquidxp.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.guipsp.liquidxp.block.Infuser;
import net.guipsp.liquidxp.block.InfuserTile;
import net.guipsp.liquidxp.block.XPPurifierTile;
import net.minecraft.world.WorldServer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class Packet3RequestInfuserGUI extends Packet1RequestXPPurifierGUI {
	public int ID = 3;

	@Override
	public void doPacket(DataInputStream data, Player playerFML)
			throws IOException {
		readPacketData(data);
		WorldServer world = FMLCommonHandler.instance()
				.getMinecraftServerInstance().worldServerForDimension(worldId);
		if (world.getChunkFromBlockCoords(coordinates[0], coordinates[2]).isChunkLoaded) {
			InfuserTile tent = (InfuserTile) world.getBlockTileEntity(
					coordinates[0], coordinates[1], coordinates[2]);
			Packet4RespondInfuserGUI response = new Packet4RespondInfuserGUI();

			try {
				response.l2 = tent.tank.getLiquid().itemID;
				response.l2a = ((float) tent.tank.getLiquid().amount)
						/ tent.tank.getCapacity();
			} catch (NullPointerException p) {
				response.l2 = 0;
				response.l2a = 0;
			}try {
				response.l1a=tent.currench.getMaxLevel();
			} catch (NullPointerException p) {
				response.l1a=1;
			}
			response.currentLevel=tent.level;
			response.worldId=worldId;
			response.coordinates=coordinates;
			response.currEnch="";
			PacketHandler.dataify(response);
			PacketDispatcher.sendPacketToPlayer(response, playerFML);
		}

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
