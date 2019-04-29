package com.supertechgroup.core.worldgen;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import com.supertechgroup.core.Config;
import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.capabilities.ore.IOreCapability;
import com.supertechgroup.core.capabilities.ore.OreCapabilityProvider;
import com.supertechgroup.core.capabilities.ore.OreCapabilityStorage;
import com.supertechgroup.core.network.PacketHandler;
import com.supertechgroup.core.network.UpdateOresPacket;
import com.supertechgroup.core.proxy.CommonProxy;
import com.supertechgroup.core.util.SimplexNoise;
import com.supertechgroup.core.worldgen.rocks.RockManager;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This class handles most events relating to ores and terrain generation
 *
 * @author oa10712
 *
 */
public class WorldGenEvents {
	private static final ArrayList<EventType> vanillaOreGeneration = new ArrayList<>();

	static {
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.COAL);
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.DIAMOND);
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.DIRT);
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.GOLD);
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.IRON);
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.LAPIS);
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.REDSTONE);
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.QUARTZ);
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.EMERALD);
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.ANDESITE);
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.DIORITE);
		vanillaOreGeneration.add(OreGenEvent.GenerateMinable.EventType.GRANITE);
	}

	double genScale = 0.004;

	@SubscribeEvent
	public void handleOreGenEvent(OreGenEvent.GenerateMinable event) {
		if (Config.removeVanilla && vanillaOreGeneration.contains(event.getType())) {
			event.setResult(Result.DENY);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public void onChunkLoadEvent(ChunkEvent.Load event) {
		if (!event.getWorld().isRemote) {
			// replace all blocks of a type with another block type
			// diesieben07 came up with this method
			// (http://www.minecraftforge.net/forum/index.php/topic,21625.0.html)

			Chunk chunk = event.getChunk();
			IOreCapability cap = chunk.getCapability(OreCapabilityProvider.ORE_CAP, null);

			if (!cap.isGenerated()) {
				long seed = event.getWorld().getSeed();
				SimplexNoise noise = new SimplexNoise();
				Random offsetRandom = new Random(seed);
				Vec3d offset1 = new Vec3d(10000.0F * offsetRandom.nextFloat(), 10000.0F * offsetRandom.nextFloat(),
						10000.0F * offsetRandom.nextFloat());
				Vec3d offset3 = new Vec3d(10000.0F * offsetRandom.nextFloat(), 10000.0F * offsetRandom.nextFloat(),
						10000.0F * offsetRandom.nextFloat());
				for (ExtendedBlockStorage storage : chunk.getBlockStorageArray()) {
					if (storage != null) {
						for (int x = 0; x < 16; ++x) {
							for (int z = 0; z < 16; ++z) {
								int igneous = (int) (noise.get2dNoiseValue(x + chunk.x * 16, z + chunk.z * 16, offset1,
										genScale) * 15) + 20;
								int sedimentary = (int) (noise.get2dNoiseValue(x + chunk.x * 16, z + chunk.z * 16,
										offset1, genScale) * 15) + 20;
								int height = chunk.getHeightValue(x & 15, z & 15);
//						int height = 255;
								for (int y = 0; y < height; y++) {

									BlockPos coord = new BlockPos(x, y, z);

									if (CommonProxy.vanillaReplace.contains(chunk.getBlockState(coord))) {
										double val = noise.get3dNoiseValue(x + chunk.x * 16, y, z + chunk.z * 16,
												offset3, genScale);
										if (y < igneous) {
											// RockType.IGNEOUS;
											chunk.setBlockState(coord,
													pickBlockFromSet(val, RockManager.stoneSpawns.get("igneous")));
										} else if (y > height - sedimentary) {
											// RockType.SEDIMENTARY;
											chunk.setBlockState(coord,
													pickBlockFromSet(val, RockManager.stoneSpawns.get("sedimentary")));
										} else {
											// RockType.METAMORPHIC;
											chunk.setBlockState(coord,
													pickBlockFromSet(val, RockManager.stoneSpawns.get("metamorphic")));
										}
									}
								}
							}
						}
					}
				}
				Random chunkRandom = chunk.getRandomWithSeed(seed);
				if (chunkRandom.nextDouble() <= 0.25) {
					int cx = chunkRandom.nextInt(10) + 3;
					int cz = chunkRandom.nextInt(10) + 3;
					double height = chunkRandom.nextInt(6) + 12;
					IBlockState kimberlite = RockManager.stoneSpawns.get("kimberlite").iterator().next();
					for (double y = 0; y < height; y++) {
						int s = (int) (4.0d * ((height - y) / height)) + 1;
						for (int x = -s; x < s; x++) {
							for (int z = -s; z < s; z++) {
								BlockPos pos = new BlockPos(cx + x + chunk.x * 16, y, cz + z + chunk.z * 16);
								if (!chunk.getBlockState(pos).equals(Blocks.BEDROCK.getDefaultState())) {
									if (chunkRandom.nextDouble() < .1) {
										Object[] data = new Object[] {
												kimberlite.getBlockHardness(chunk.getWorld(), pos),
												RockManager.getTexture(kimberlite),
												new ResourceLocation("supertechcore:diamond") };
										cap.setData(pos.getX(), pos.getY(), pos.getZ(), data);
										chunk.markDirty();
										chunk.setBlockState(pos, ModRegistry.superore.getDefaultState());
									} else {
										chunk.setBlockState(pos, kimberlite);
									}
								}
							}
						}
					}
				}
				Random random = chunk.getRandomWithSeed(event.getWorld().getSeed());
				CommonProxy.parsed.forEach((gen) -> {
					// gen.generate(random, chunk.x, chunk.z, event.getWorld(), null,
					// event.getWorld().getChunkProvider());
				});
			}
			chunk.setModified(true);// this is important as it marks it to be saved
		}
	}

	@SubscribeEvent
	public void onPlayerWatchChunk(ChunkWatchEvent.Watch event) {
		IOreCapability cap = event.getChunkInstance().getCapability(OreCapabilityProvider.ORE_CAP, null);
		NBTBase tag = (new OreCapabilityStorage()).writeNBT(OreCapabilityProvider.ORE_CAP, cap, null);
		UpdateOresPacket packet = new UpdateOresPacket(tag, event.getChunkInstance().x, event.getChunkInstance().z);
		PacketHandler.INSTANCE.sendTo(packet, event.getPlayer());
	}

	public static final ResourceLocation ORE_CAP = new ResourceLocation(Reference.MODID, "ores");

	@SubscribeEvent
	public void attachCapabilityChunk(AttachCapabilitiesEvent<Chunk> event) {
		if (!(event.getObject() instanceof Chunk)) {
			return;
		}
		event.addCapability(ORE_CAP, new OreCapabilityProvider());
	}

	private IBlockState pickBlockFromSet(double value, Set<IBlockState> list) {
		value = ((value + 1) / 2) * list.size();
		return list.stream().skip((int) value).findFirst().get();
	}
}