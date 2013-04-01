package net.guipsp.liquidxp.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler {
	class ClientPacketHandler implements IPacketHandler {

		@Override
		public void onPacketData(INetworkManager manager,
				Packet250CustomPayload packet, Player player) {
			try {
				DataInputStream data = new DataInputStream(
						new ByteArrayInputStream(packet.data));
				int ID = data.readInt();
				switch (ID) {
				case 2:
					Packet2RespondPurifierGUI p2 = new Packet2RespondPurifierGUI();
					p2.doPacket(data, player);
					break;
				case 4:
					Packet4RespondInfuserGUI p4 = new Packet4RespondInfuserGUI();
					p4.doPacket(data, player);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	class ServerPacketHandler implements IPacketHandler {

		@Override
		public void onPacketData(INetworkManager manager,
				Packet250CustomPayload packet, Player player) {
			try {
				DataInputStream data = new DataInputStream(
						new ByteArrayInputStream(packet.data));
				int ID = data.readInt();
				switch (ID) {
				case 1:
					Packet1RequestXPPurifierGUI p1 = new Packet1RequestXPPurifierGUI();
					p1.doPacket(data, player);
					break;

				case 3:
					Packet3RequestInfuserGUI p3 = new Packet3RequestInfuserGUI();
					p3.doPacket(data, player);
					break;
				case 5:
					Packet5UpdatePurifier p5 = new Packet5UpdatePurifier();
					p5.doPacket(data, player);
					break;
				case 6:
					Packet6UpdateInfuser p6 = new Packet6UpdateInfuser();
					p6.doPacket(data, player);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private ServerPacketHandler serverPacketHandler = new ServerPacketHandler();
	private ClientPacketHandler clientPacketHandler = new ClientPacketHandler();

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.CLIENT) {
			clientPacketHandler.onPacketData(manager, packet, player);
		} else {
			serverPacketHandler.onPacketData(manager, packet, player);
		}

	}

	public static void dataify(LiquidExpPacket p) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream s = new DataOutputStream(byteArrayOutputStream);
		try {
			p.writePacketData(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			s.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.data = byteArrayOutputStream.toByteArray();

	}

}
