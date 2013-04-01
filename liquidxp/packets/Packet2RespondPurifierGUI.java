package net.guipsp.liquidxp.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import buildcraft.core.utils.Localization;

import net.guipsp.liquidxp.LiquidExperience;
import net.guipsp.liquidxp.gui.GuiXpPurifier;
import net.guipsp.liquidxp.gui.GuiXpPurifier.LContainer;
import net.guipsp.liquidxp.liquids.LiquidEnchantment;
import net.minecraft.enchantment.Enchantment;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Packet2RespondPurifierGUI extends LiquidExpPacket {

	public int ID = 2;
	public int[] coordinates;
	public int worldId;
	public float l1a;
	public float l2a;
	public int l2;
	public int currentLevel;
	public String currEnch;
	@Override
	public void doPacket(DataInputStream data, Player playerFML)
			throws IOException {
		readPacketData(data);
		Enchantment ench = null;
		for (Entry<Enchantment, LiquidEnchantment> enchant : LiquidExperience.liquidEnchants
				.entrySet()) {
			if (enchant.getValue().itemID == l2) {
				ench = enchant.getKey();
				break;
			}
		}
		
		FMLClientHandler
				.instance()
				.getClient()
				.displayGuiScreen(
						new GuiXpPurifier(new LContainer(
								LiquidExperience.liquidXP, l1a),
								new LContainer(LiquidExperience.liquidEnchants
										.get(ench), l2a), currentLevel,
								coordinates[0], coordinates[1], coordinates[2],
								worldId,currEnch));
	}

	@Override
	public void readPacketData(DataInputStream par1DataInputStream)
			throws IOException {
		l1a = par1DataInputStream.readFloat();
		l2a = par1DataInputStream.readFloat();
		l2 = par1DataInputStream.readInt();
		worldId = par1DataInputStream.readInt();
		coordinates = new int[] { par1DataInputStream.readInt(),
				par1DataInputStream.readInt(), par1DataInputStream.readInt() };
		currentLevel = par1DataInputStream.readInt();
		currEnch=par1DataInputStream.readUTF();
	}

	@Override
	public void writePacketData(DataOutputStream par1DataOutputStream)
			throws IOException {
		par1DataOutputStream.writeInt(ID);
		par1DataOutputStream.writeFloat(l1a);
		par1DataOutputStream.writeFloat(l2a);
		par1DataOutputStream.writeInt(l2);

		par1DataOutputStream.writeInt(worldId);
		par1DataOutputStream.writeInt(coordinates[0]);
		par1DataOutputStream.writeInt(coordinates[1]);
		par1DataOutputStream.writeInt(coordinates[2]);

		par1DataOutputStream.writeInt(currentLevel);
		
		par1DataOutputStream.writeUTF(currEnch);
	}

}
