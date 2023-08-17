package net.blay09.mods.waystones;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.waystones.block.BlockWaystone;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.item.ItemReturnScroll;
import net.blay09.mods.waystones.item.ItemWarpStone;
import net.blay09.mods.waystones.network.NetworkHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = Waystones.MOD_ID, name = "Waystones", version = Waystones.VERSION)
@SuppressWarnings("unused")
public class Waystones {


	/** This mod's id. */
	public static final String MOD_ID = Tags.MODID;
	public static final String MOD_Name = Tags.MODNAME;


	/** This mod's version. */
	public static final String VERSION = Tags.VERSION;

	@SidedProxy(serverSide = Tags.GROUPNAME + ".CommonProxy", clientSide = Tags.GROUPNAME + ".client.ClientProxy")
	public static CommonProxy proxy;

	public static BlockWaystone blockWaystone;
	public static ItemReturnScroll itemReturnScroll;
	public static ItemWarpStone itemWarpStone;

	public static Configuration configuration;

	private static WaystoneConfig config;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		blockWaystone = new BlockWaystone();
		GameRegistry.registerBlock(blockWaystone, "waystone");
		GameRegistry.registerTileEntity(TileWaystone.class, MOD_ID + ":waystone");

		itemReturnScroll = new ItemReturnScroll();
		GameRegistry.registerItem(itemReturnScroll, "warpScroll");

		itemWarpStone = new ItemWarpStone();
		GameRegistry.registerItem(itemWarpStone, "warpStone");

		NetworkHandler.init();

		configuration = new Configuration(event.getSuggestedConfigurationFile());
		config = new WaystoneConfig();
		config.reloadLocal(configuration);
		if(configuration.hasChanged()) {
			configuration.save();
		}

		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		FMLInterModComms.sendMessage("Waila", "register", "net.blay09.mods.waystones.compat.WailaProvider.register");
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(config.allowReturnScrolls) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemReturnScroll, 3), "GEG", "PPP", 'G', "nuggetGold", 'E', Items.ender_pearl, 'P', Items.paper));
		}
		if(config.lootReturnScrolls) {
			// Item, min, max, weight
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH)
					.addItem(new WeightedRandomChestContent(new ItemStack(itemReturnScroll), 1, 1, 3));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR)
					.addItem(new WeightedRandomChestContent(new ItemStack(itemReturnScroll), 1, 1, 3));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST)
					.addItem(new WeightedRandomChestContent(new ItemStack(itemReturnScroll), 1, 2, 3));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST)
					.addItem(new WeightedRandomChestContent(new ItemStack(itemReturnScroll), 1, 1, 3));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST)
					.addItem(new WeightedRandomChestContent(new ItemStack(itemReturnScroll), 1, 1, 2));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY)
					.addItem(new WeightedRandomChestContent(new ItemStack(itemReturnScroll), 1, 1, 2));
		}

		if(config.allowWarpStone) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemWarpStone), "DED", "EGE", "DED", 'D', "dyePurple", 'E', Items.ender_pearl, 'G', "gemEmerald"));
		}

		if(!config.creativeModeOnly) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockWaystone), " S ", "SWS", "OOO", 'S', Blocks.stonebrick, 'W', itemWarpStone, 'O', Blocks.obsidian));
		}

	}
	public static WaystoneConfig getConfig() {
		return config;
	}

	public static void setConfig(WaystoneConfig config) {
		Waystones.config = config;
	}

}
