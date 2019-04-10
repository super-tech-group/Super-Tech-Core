package com.supertechgroup.core.proxy;

import com.supertechgroup.core.BakedModelLoader;
import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.blocks.BlockColor;
import com.supertechgroup.core.metallurgy.Material;
import com.supertechgroup.core.metallurgy.MetalColor;
import com.supertechgroup.core.worldgen.ores.Ore;
import com.supertechgroup.core.worldgen.ores.OreColor;
import com.supertechgroup.core.worldgen.ores.OreItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Proxy for clients only.
 *
 * @author oa10712
 *
 */
@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ClientProxy extends CommonProxy {

	private static final Minecraft minecraft = Minecraft.getMinecraft();

	static ModelResourceLocation chunkLocation = new ModelResourceLocation(Reference.MODID + ":itemOreChunk",
			"inventory");
	static ModelResourceLocation crushedLocation = new ModelResourceLocation(Reference.MODID + ":itemOreCrushed",
			"inventory");
	static ModelResourceLocation ingotLocation = new ModelResourceLocation(Reference.MODID + ":itemIngot", "inventory");
	static ModelResourceLocation dustLocation = new ModelResourceLocation(Reference.MODID + ":itemDust", "inventory");
	static ModelResourceLocation foilLocation = new ModelResourceLocation(Reference.MODID + ":itemFoil", "inventory");
	static ModelResourceLocation gearLocation = new ModelResourceLocation(Reference.MODID + ":itemGear", "inventory");
	static ModelResourceLocation nuggetLocation = new ModelResourceLocation(Reference.MODID + ":itemNugget",
			"inventory");
	static ModelResourceLocation plateLocation = new ModelResourceLocation(Reference.MODID + ":itemPlate", "inventory");
	static ModelResourceLocation rodLocation = new ModelResourceLocation(Reference.MODID + ":itemRod", "inventory");
	static ModelResourceLocation clumpLocation = new ModelResourceLocation(Reference.MODID + ":itemClump", "inventory");
	static ModelResourceLocation shardLocation = new ModelResourceLocation(Reference.MODID + ":itemShard", "inventory");
	static ModelResourceLocation crystalLocation = new ModelResourceLocation(Reference.MODID + ":itemCrystal",
			"inventory");
	static ModelResourceLocation wireLocation = new ModelResourceLocation(Reference.MODID + ":itemWire", "inventory");
	static ModelResourceLocation tinyLocation = new ModelResourceLocation(Reference.MODID + ":itemTinyDust",
			"inventory");
	static ModelResourceLocation coinLocation = new ModelResourceLocation(Reference.MODID + ":itemCoin", "inventory");
	static ModelResourceLocation bladeLocation = new ModelResourceLocation(Reference.MODID + ":itemBlade", "inventory");
	static ModelResourceLocation hammerLocation = new ModelResourceLocation(Reference.MODID + ":itemHammer",
			"inventory");
	static ModelResourceLocation pickaxeLocation = new ModelResourceLocation(Reference.MODID + ":itemPickaxe",
			"inventory");
	static ModelResourceLocation pliersLocation = new ModelResourceLocation(Reference.MODID + ":itemPliers",
			"inventory");
	static ModelResourceLocation drawPlateLocation = new ModelResourceLocation(Reference.MODID + ":itemDrawPlate",
			"inventory");
	static ModelResourceLocation shovelLocation = new ModelResourceLocation(Reference.MODID + ":itemShovel",
			"inventory");
	static ModelResourceLocation axeLocation = new ModelResourceLocation(Reference.MODID + ":itemAxe", "inventory");
	public static ModelResourceLocation blockLocation = new ModelResourceLocation(Reference.MODID + ":blockMaterial",
			"normal");
	public static ModelResourceLocation itemLocation = new ModelResourceLocation(Reference.MODID + ":itemBlockMaterial",
			"inventory");

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModRegistry.initModels();
	}

	@Override
	public Side getSide() {
		return Side.CLIENT;
	}

	@Override
	public World getWorld(IBlockAccess world) {
		if (world != null && world instanceof World) {
			return (World) world;
		}
		return Minecraft.getMinecraft().world;
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);

		Render<EntityItem> previous = (Render<EntityItem>) Minecraft.getMinecraft().getRenderManager().entityRenderMap
				.get(EntityItem.class);
		Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityItem.class, new RenderEntityItem(
				Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()) {
			@Override
			public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
				float f1 = entity.getEntityData().getBoolean("onBelt") && shouldBob()
						? MathHelper.sin((entity.getAge() + Minecraft.getMinecraft().getRenderPartialTicks()) / 10.0F
								+ entity.hoverStart) * 0.1F + 0.1F
						: 0;
				GlStateManager.translate(0f, -f1, 0f);
				if (previous != null)
					previous.doRender(entity, x, y, z, entityYaw, partialTicks);
				else
					super.doRender(entity, x, y, z, entityYaw, partialTicks);
				GlStateManager.translate(0f, f1, 0f);
			}
		});
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);

		Material.REGISTRY.getValuesCollection().forEach((m) -> {
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(MetalColor.INSTANCE, m.getMaterialItem());
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(MetalColor.INSTANCE, m.getItemPickaxe());
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(MetalColor.INSTANCE, m.getItemAxe());
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(MetalColor.INSTANCE, m.getItemShovel());
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(MetalColor.INSTANCE,
					m.getItemDrawplate());
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(MetalColor.INSTANCE, m.getItemPliers());
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(MetalColor.INSTANCE, m.getItemHammer());
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(BlockColor.INSTANCE, m.getItemBlock());
			Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(BlockColor.INSTANCE, m.getBlock());
		});

		Ore.REGISTRY.getValuesCollection().forEach((o) -> {
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(OreColor.INSTANCE, o.getItemOre());
		});
		ModRegistry.initItemModels();
	}

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		// ((ItemTechComponent) ModRegistry.itemTechComponent).registerModels();
		ModelLoaderRegistry.registerLoader(new BakedModelLoader());
		OBJLoader.INSTANCE.addDomain(Reference.MODID);
		// ModelLoaderRegistry.registerLoader(ModelLoaderRock.INSTANCE);
	}

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		NonNullList<ItemStack> subItems = NonNullList.create();
		item.getSubItems(CreativeTabs.MISC, subItems);
		subItems.forEach((item2) -> {
			ModelLoader.setCustomModelResourceLocation(item, item2.getMetadata(),
					new ModelResourceLocation(Reference.MODID + ":" + id, "inventory"));
		});
	}
	/*
	 * @Override public void registerModels(Material mat) {
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.INGOT, ingotLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.DUST, dustLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.GEAR, gearLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.NUGGET, nuggetLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.PLATE, plateLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.ROD, rodLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.CLUMP, clumpLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.CRYSTAL, crystalLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.SHARD, shardLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.WIRE, wireLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.DIRTY, dustLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.FOIL, foilLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.TINY, tinyLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.COIN, coinLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getMaterialItem(),
	 * MaterialItem.BLADE, bladeLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getItemHammer(), 0,
	 * hammerLocation);
	 * 
	 * ModelLoader.setCustomModelResourceLocation(mat.getItemPickaxe(), 0,
	 * pickaxeLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getItemPliers(), 0,
	 * pliersLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getItemDrawplate(), 0,
	 * drawPlateLocation);
	 * ModelLoader.setCustomModelResourceLocation(mat.getItemShovel(), 0,
	 * shovelLocation); ModelLoader.setCustomModelResourceLocation(mat.getItemAxe(),
	 * 0, axeLocation); }
	 */

	@Override
	public void registerModels(Ore ore) {
		ModelLoader.setCustomModelResourceLocation(ore.getItemOre(), OreItem.ORE, chunkLocation);
		ModelLoader.setCustomModelResourceLocation(ore.getItemOre(), OreItem.NETHER_ORE, chunkLocation);
		ModelLoader.setCustomModelResourceLocation(ore.getItemOre(), OreItem.END_ORE, chunkLocation);
		ModelLoader.setCustomModelResourceLocation(ore.getItemOre(), OreItem.CRUSHED, crushedLocation);

	}
}