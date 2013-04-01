package net.guipsp.liquidxp;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.guipsp.liquidxp.block.Infuser;
import net.guipsp.liquidxp.block.InfuserTile;
import net.guipsp.liquidxp.block.XPLiquifier;
import net.guipsp.liquidxp.block.XPLiquifierTile;
import net.guipsp.liquidxp.block.XPPurifier;
import net.guipsp.liquidxp.block.XPPurifierTile;
import net.guipsp.liquidxp.liquids.LiquidEnchantment;
import net.guipsp.liquidxp.liquids.LiquidXP;
import net.guipsp.liquidxp.packets.PacketHandler;
import net.guipsp.liquidxp.proxies.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "LiquidExperience", name = "LiquidXP", version = "0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class, channels = { "LiquidExperience" })
public class LiquidExperience {

	@Instance("LiquidExperience")
	public static LiquidExperience instance;

	@SidedProxy(clientSide = "net.guipsp.liquidxp.proxies.ClientProxy", serverSide = "net.guipsp.liquidxp.proxies.CommonProxy")
	public static CommonProxy proxy;

	public static int baseBlockID = 500;

	public static int baseItemID = 5500;

	public static LiquidXP liquidXP;

	public static XPLiquifier xpLiquifier;

	public static XPPurifier xpPurifier;

	public static Map<Enchantment, LiquidEnchantment> liquidEnchants;

	public static List<Enchantment> enchantlist;

	public static Infuser infuser;

	@Init
	public void load(FMLInitializationEvent event) {
		{
			liquidXP = new LiquidXP(baseItemID);
			liquidXP.setItemName("LiquidXP");
			LanguageRegistry.addName(liquidXP, "Liquid XP");
			GameRegistry.registerItem(liquidXP, "LiquidXP");
			LiquidDictionary.getOrCreateLiquid("LiquidXP", new LiquidStack(
					liquidXP, 1));
		}
		{
			xpLiquifier = new XPLiquifier(baseBlockID, Material.rock);
			xpLiquifier.setBlockName("XPLiquifier")
					.setStepSound(Block.soundStoneFootstep).setHardness(1.5f)
					.setResistance(5.0F);
			GameRegistry.registerBlock(xpLiquifier, "XPLiquifier");
			GameRegistry.registerTileEntity(XPLiquifierTile.class,
					"XPLiquifier");
			LanguageRegistry.addName(xpLiquifier, "XP Liquifier");
			MinecraftForge.setBlockHarvestLevel(xpLiquifier, "pickaxe", 1);
		}
		{
			xpPurifier = new XPPurifier(baseBlockID + 1, Material.rock);
			xpPurifier.setBlockName("XPPurifier")
					.setStepSound(Block.soundStoneFootstep).setHardness(2.0f)
					.setResistance(5.0F).blockIndexInTexture = 6;
			GameRegistry.registerBlock(xpPurifier, "XPPurifier");
			GameRegistry.registerTileEntity(XPPurifierTile.class, "XPPurifier");
			LanguageRegistry.addName(xpPurifier, "XP Purifier");
			MinecraftForge.setBlockHarvestLevel(xpPurifier, "pickaxe", 1);
		}
		{
			infuser = new Infuser(baseBlockID + 2);
			infuser.setBlockName("Infuser")
					.setStepSound(Block.soundStoneFootstep).setHardness(5.0f)
					.setResistance(10.0F).blockIndexInTexture = 2;
			GameRegistry.registerBlock(infuser, "Infuser");
			GameRegistry.registerTileEntity(InfuserTile.class, "Infuser");
			LanguageRegistry.addName(infuser, "Infuser");
			MinecraftForge.setBlockHarvestLevel(infuser, "pickaxe", 1);
		}
		proxy.registerRenderers();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent evt) {
		int id = 0;
		enchantlist = new ArrayList<Enchantment>();
		liquidEnchants = new HashMap<Enchantment, LiquidEnchantment>();
		for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
			try {
				Enchantment enchant = Enchantment.enchantmentsList[i];
				enchant.getName();
				id++;
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(enchant.getName().getBytes("UTF-8"));
				int inthash = (int) Math.abs(ByteBuffer.wrap(md.digest())
						.getInt());
				String hash = String.valueOf(inthash);
				while (hash.length() < 9) {
					hash = "0" + hash;
				}

				LiquidEnchantment liquidEnchantment = new LiquidEnchantment(id
						+ baseItemID + 1);
				liquidEnchantment.setIconIndex(id);
				liquidEnchantment.setItemName(enchant.getName());
				LiquidDictionary.getOrCreateLiquid(enchant.getName(),
						new LiquidStack(liquidEnchantment, 1));
				liquidEnchants.put(enchant, liquidEnchantment);
				proxy.registerEnchantRenderers(id,
						Integer.parseInt(hash.substring(0, 3)) % 255,
						Integer.parseInt(hash.substring(3, 6)) % 255,
						Integer.parseInt(hash.substring(6, 9)) % 255);
				enchantlist.add(enchant);
			} catch (NullPointerException t) {
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static int clamp(int val, int min, int max) {
		return (val > max) ? max : (val < min) ? min : val;
	}
}
