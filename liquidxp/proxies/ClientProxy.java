package net.guipsp.liquidxp.proxies;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy{
	
	public static final String TEXTURES = "/net/guipsp/liquidxp/textures.png";
	public static final String ENCHANT_TEXTURES = "/net/guipsp/liquidxp/enchants.png";
	@Override
	public void registerRenderers(){
		MinecraftForgeClient.preloadTexture(TEXTURES);
		MinecraftForgeClient.preloadTexture(ENCHANT_TEXTURES);
		MinecraftForgeClient.preloadTexture("/net/guipsp/liquidxp/liquidc.png");
		FMLClientHandler.instance().getClient().renderEngine.registerTextureFX(new TextureLiquidXPFX());
	}
	@Override
	public void registerEnchantRenderers(int i, int r, int g, int b){
		FMLClientHandler.instance().getClient().renderEngine.registerTextureFX(new TextureLiquidEnchantFX(i,r,g,b));
	}
}
