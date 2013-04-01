package net.guipsp.liquidxp.packets;

import java.io.DataInputStream;
import java.io.IOException;

import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public abstract class LiquidExpPacket extends Packet250CustomPayload {

	public int ID;
	
	public LiquidExpPacket() {
		this.channel="LiquidExperience";
	}
	
	public abstract void doPacket(DataInputStream data, Player playerFML) throws IOException;
}
