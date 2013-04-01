package net.guipsp.liquidxp.gui;

import java.nio.ByteBuffer;
import java.util.regex.Pattern;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.FMLTextureFX;
import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.common.network.PacketDispatcher;

import net.guipsp.liquidxp.LiquidExperience;
import net.guipsp.liquidxp.packets.Packet1RequestXPPurifierGUI;
import net.guipsp.liquidxp.packets.Packet5UpdatePurifier;
import net.guipsp.liquidxp.packets.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

/**
 * 
 * To my one and only, Guillermo <3 Texture by MrCompost
 * 
 * @author Tundmatu
 * 
 */
public class GuiXpPurifier extends GuiScreen {

	protected static final int SHIFT_LEFT = 0x0;
	protected static final int SHIFT_RIGHT = 0x1;
	protected static final int DEFAULT_SHIFT_VALUE = 10;

	protected static final Pattern INPUT_FILTER = Pattern.compile("[0-9]");
	protected String text = "This is a bug.";

	protected boolean prevFocus = false;

	protected int containerTexture;
	protected int centerX;
	protected int centerY;
	protected int maxLevels = 30;
	protected int minLevels = 0;

	public LContainer leftContainer;
	public LContainer rightContainer;
	public GuiTextField inputField;
	public String inputText;
	public int x;
	public int y;
	public int z;
	public int world;

	public GuiXpPurifier(LContainer leftContainer, LContainer rightContainer,
			int currlevel, int x, int y, int z, int world, String string) {
		this(leftContainer, rightContainer, currlevel, x, y, z, world);
		this.text = string;
	}

	public GuiXpPurifier(LContainer leftContainer, LContainer rightContainer,
			int currlevel, int x, int y, int z, int world) {
		this.inputText = "" + DEFAULT_SHIFT_VALUE;
		this.leftContainer = leftContainer;
		this.rightContainer = rightContainer;
		this.containerTexture = Minecraft.getMinecraft().renderEngine
				.getTexture("/net/guipsp/liquidxp/liquidc.png");
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		this.inputText = "" + currlevel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		this.centerX = this.width / 2;
		this.centerY = this.height / 2;

		this.controlList.clear();
		this.controlList.add(new GuiButton(SHIFT_LEFT, centerX - (50 + 10),
				centerY + 30, 20, 20, "<"));
		this.controlList.add(new GuiButton(SHIFT_RIGHT, centerX + (50 - 10),
				centerY + 30, 20, 20, ">"));
		this.inputField = new GuiTextField(this.fontRenderer, centerX - 20,
				centerY + 46, 40, 16);
		this.inputField.setText(this.inputText);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (this.inputField.isFocused()) {
			if (par2 == 28) {
				this.inputField.setFocused(false);
				{
					Packet5UpdatePurifier p = new Packet5UpdatePurifier();
					p.worldId = world;
					p.coordinates = new int[] { x, y, z };
					p.action = false;
					p.level = Integer.parseInt(this.inputField.getText());
					PacketHandler.dataify(p);
					PacketDispatcher.sendPacketToServer(p);
				}
				{
					Packet1RequestXPPurifierGUI p = new Packet1RequestXPPurifierGUI();
					p.worldId = world;
					p.coordinates = new int[] { x, y, z };
					PacketHandler.dataify(p);
					PacketDispatcher.sendPacketToServer(p);
				}
			}
			if (INPUT_FILTER.matcher(Keyboard.getKeyName(par2)).find()
					|| par2 == 14) {
				this.inputField.textboxKeyTyped(par1, par2);
				this.inputText = this.inputField.getText();
			}
		}
		super.keyTyped(par1, par2);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		this.inputField.mouseClicked(par1, par2, par3);
		super.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void actionPerformed(GuiButton btn) {
		{
			Packet5UpdatePurifier p = new Packet5UpdatePurifier();
			p.worldId = world;
			p.coordinates = new int[] { x, y, z };
			p.action = true;
			p.level = Integer.parseInt(this.inputField.getText());
			switch (btn.id) {
			case SHIFT_LEFT:
				p.decrease = true;
				break;
			case SHIFT_RIGHT:
				p.decrease = false;
				break;
			}
			PacketHandler.dataify(p);
			PacketDispatcher.sendPacketToServer(p);
		}
		{
			Packet1RequestXPPurifierGUI p = new Packet1RequestXPPurifierGUI();
			p.worldId = world;
			p.coordinates = new int[] { x, y, z };
			PacketHandler.dataify(p);
			PacketDispatcher.sendPacketToServer(p);
		}
	}

	@Override
	public void updateScreen() {
		if (this.prevFocus) {
			if (!this.inputField.isFocused()) {
				int input = Integer.parseInt(this.inputField.getText());
				this.inputField.setText(""
						+ LiquidExperience.clamp(input, this.minLevels,
								this.maxLevels));
			}
		}
		if (this.inputField.isFocused()) {
			this.inputField.updateCursorCounter();
		}
		this.prevFocus = this.inputField.isFocused();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public void renderContainer(LContainer container) {
		if (container.item == null || container.value == 0F) {
			return;
		}
		RenderEngine renderEngine = FMLClientHandler.instance().getClient().renderEngine;
		renderEngine.bindTexture(renderEngine.getTexture(container.item
				.getTextureFile()));
		int var8 = container.item.getIconFromDamage(0);
		this.renderTexturedQuad(centerX, centerY + 38
				- (int) (container.value * 106), var8 % 16 * 16,
				var8 / 16 * 16, 60, (int) (container.value * 106));
	}

	public void renderTexturedQuad(int par1, int par2, int par3, int par4,
			int par5, int par6) {
		float var7 = 0.00390625F;
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV((double) (par1 + 0), (double) (par2 + par6),
				(double) this.zLevel, (double) ((float) (par3 + 0) * var7),
				(double) ((float) (par4 + 16) * var7));
		var9.addVertexWithUV((double) (par1 + par5), (double) (par2 + par6),
				(double) this.zLevel, (double) ((float) (par3 + 16) * var7),
				(double) ((float) (par4 + 16) * var7));
		var9.addVertexWithUV((double) (par1 + par5), (double) (par2 + 0),
				(double) this.zLevel, (double) ((float) (par3 + 16) * var7),
				(double) ((float) (par4 + 0) * var7));
		var9.addVertexWithUV((double) (par1 + 0), (double) (par2 + 0),
				(double) this.zLevel, (double) ((float) (par3 + 0) * var7),
				(double) ((float) (par4 + 0) * var7));
		var9.draw();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		this.drawDefaultBackground();
		this.fontRenderer.drawStringWithShadow(text, centerX
				- (this.fontRenderer.getStringWidth(text) / 2), centerY + 20,
				0xFFFFFF);
		if (Keyboard.isKeyDown(Keyboard.KEY_U))
			this.initGui();
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(-140, 0, 0);
			renderContainer(leftContainer);
			GL11.glPopMatrix();
		}
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(80, 0, 0);
			renderContainer(rightContainer);
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
		this.drawTexturedModalRect(centerX + 80, centerY - 80, 0, 0,
				centerX + 140, centerY + 50);
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
		super.drawScreen(mouseX, mouseY, partialTick);
	}

	public static class LContainer {

		public float value;
		public Item item;

		public LContainer(Item item, float value) {
			this.value = value;
			this.item = item;
		}

	}

}