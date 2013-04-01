package net.guipsp.liquidxp.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

import net.guipsp.liquidxp.packets.Packet1RequestXPPurifierGUI;
import net.guipsp.liquidxp.packets.Packet3RequestInfuserGUI;
import net.guipsp.liquidxp.packets.Packet5UpdatePurifier;
import net.guipsp.liquidxp.packets.Packet6UpdateInfuser;
import net.guipsp.liquidxp.packets.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

public class GuiInfuser extends GuiXpPurifier {

	protected String SHIFT_STRING = "Level:";

	public GuiInfuser(LContainer container, int l1a, int currentLevel, int x,
			int y, int z, int world) {
		super(container, null, currentLevel, x, y, z, world);
		this.minLevels = 1;
		this.maxLevels = 20000;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.controlList.clear();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		this.drawDefaultBackground();
		this.fontRenderer.drawStringWithShadow(SHIFT_STRING, centerX
				- (this.fontRenderer.getStringWidth(SHIFT_STRING) / 2),
				centerY + 35, 0xFFFFFF);
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(-140, 0, 0);
			renderContainer(leftContainer);
			GL11.glPopMatrix();
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.containerTexture);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(false);
		this.drawTexturedModalRect(centerX - 140, centerY - 80, 0, 0,
				centerX - 80, centerY + 50);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		if (this.inputField.isFocused()) {
			this.inputField.drawTextBox();
		} else {
			String inputFieldString = String.format("[%s]",
					this.inputField.getText());
			this.fontRenderer.drawStringWithShadow(inputFieldString, centerX
					- (this.fontRenderer.getStringWidth(inputFieldString) / 2),
					centerY + 50, 0xFFFFFF);
		}

	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (this.inputField.isFocused()) {
			if (par2 == 28) {
				this.inputField.setFocused(false);
				{
					Packet6UpdateInfuser p = new Packet6UpdateInfuser();
					p.worldId = world;
					p.coordinates = new int[] { x, y, z };
					p.level = Integer.parseInt(this.inputField.getText());
					PacketHandler.dataify(p);
					PacketDispatcher.sendPacketToServer(p);
				}
				{
					Packet3RequestInfuserGUI p = new Packet3RequestInfuserGUI();
					p.worldId = world;
					p.coordinates = new int[] { x, y, z };
					PacketHandler.dataify(p);
					PacketDispatcher.sendPacketToServer(p);
				}
			}
		}
		super.keyTyped(par1, par2);
	}

}
