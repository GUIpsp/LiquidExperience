package net.guipsp.liquidxp.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import net.guipsp.liquidxp.LiquidExperience;
import net.guipsp.liquidxp.gui.GuiInfuser;
import net.guipsp.liquidxp.gui.GuiXpPurifier;
import net.guipsp.liquidxp.gui.GuiXpPurifier.LContainer;
import net.guipsp.liquidxp.liquids.LiquidEnchantment;
import net.minecraft.enchantment.Enchantment;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.Player;

public class Packet4RespondInfuserGUI extends Packet2RespondPurifierGUI {
	public int ID = 4;

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
						new GuiInfuser(
								new LContainer(LiquidExperience.liquidEnchants
										.get(ench), l2a), (int) l1a,
								currentLevel, coordinates[0], coordinates[1],
								coordinates[2], worldId));

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
