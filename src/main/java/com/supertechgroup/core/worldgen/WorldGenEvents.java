package com.supertechgroup.core.worldgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.tuple.MutablePair;

import com.supertechgroup.core.Config;
import com.supertechgroup.core.network.PacketHandler;
import com.supertechgroup.core.network.UpdateOresPacket;
import com.supertechgroup.core.proxy.CommonProxy;
import com.supertechgroup.core.util.SimplexNoise;
import com.supertechgroup.core.worldgen.rocks.RockManager;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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

	HashMap<UUID, ArrayList<MutablePair<Integer, Integer>>> sentChunks = new HashMap<>();

	@SubscribeEvent
	public void handleOreGenEvent(OreGenEvent.GenerateMinable event) {
		if (Config.removeVanilla && vanillaOreGeneration.contains(event.getType())) {
			event.setResult(Result.DENY);
		}
	}

	private void handleOreUpdate(EntityPlayerMP e, int newChunkX, int newChunkZ) {
		if (e.world != null) {
			if (!sentChunks.get(e.getUniqueID()).contains(new MutablePair<>(newChunkX, newChunkZ))) {
				UpdateOresPacket packet = new UpdateOresPacket(OreSavedData.get(e.world), newChunkX, newChunkZ);
				PacketHandler.INSTANCE.sendTo(packet, e);
				sentChunks.get(e.getUniqueID()).add(new MutablePair<>(newChunkX, newChunkZ));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLogin(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) e.getEntity();
			sentChunks.put(player.getUniqueID(), new ArrayList<MutablePair<Integer, Integer>>());
		}
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent e) {
		if (e.player instanceof EntityPlayerMP) {
			sentChunks.remove(e.player.getUniqueID());// remove the player from
														// the chunk tracker
		}
	}

	/**
	 * cleanup to keep the server ram usage down
	 *
	 * @param e
	 */
	@SubscribeEvent
	public void onPlayerUnWatchChunk(ChunkWatchEvent.UnWatch e) {
		int x = e.getChunkInstance().x;
		int z = e.getChunkInstance().z;
		if (sentChunks.containsKey(e.getPlayer().getUniqueID())) {
			sentChunks.get(e.getPlayer().getUniqueID()).remove(new MutablePair<>(x, z));
		}
	}

	@SubscribeEvent
	public void onPlayerWatchChunk(ChunkWatchEvent.Watch e) {
		int x = e.getChunkInstance().x;
		int z = e.getChunkInstance().z;

		OreSavedData get = OreSavedData.get(e.getPlayer().world);
		Chunk chunk = e.getPlayer().world.getChunkFromChunkCoords(x, z);
		NBTTagCompound forChunk = get.getForChunk(x, z);

		World world = e.getPlayer().world;
		if (forChunk.hasNoTags() || !get.isChunkGenerated(x, z)) {
			Random random = chunk.getRandomWithSeed(world.getSeed());
			CommonProxy.parsed.forEach((gen) -> {
				gen.generate(random, chunk.x, chunk.z, world, null, world.getChunkProvider());
			});

			get.setChunkGenerated(x, z);
		}

		handleOreUpdate(e.getPlayer(), x, z);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public void onPopulateChunk(PopulateChunkEvent.Pre event) {
		// replace all blocks of a type with another block type
		// diesieben07 came up with this method
		// (http://www.minecraftforge.net/forum/index.php/topic,21625.0.html)
		Chunk chunk = event.getWorld().getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
		long seed = event.getWorld().getSeed();
		SimplexNoise noise = new SimplexNoise();
		Random offsetRandom = new Random(seed);
		Vec3d layerOffset = new Vec3d(10000.0F * offsetRandom.nextFloat(), 10000.0F * offsetRandom.nextFloat(),
				10000.0F * offsetRandom.nextFloat());
		Vec3d geomeOffset = new Vec3d(10000.0F * offsetRandom.nextFloat(), 10000.0F * offsetRandom.nextFloat(),
				10000.0F * offsetRandom.nextFloat());
		for (ExtendedBlockStorage storage : chunk.getBlockStorageArray()) {
			if (storage != null) {
				for (int x = 0; x < 16; ++x) {
					for (int z = 0; z < 16; ++z) {
						int igneous = (int) (noise.get2dNoiseValue(x + chunk.x * 16, z + chunk.z * 16, layerOffset,
								genScale) * 15) + 20;
						int sedimentary = (int) (noise.get2dNoiseValue(x + chunk.x * 16, z + chunk.z * 16, layerOffset,
								genScale) * 15) + 20;
						int height = chunk.getHeightValue(x & 15, z & 15);
						for (int y = storage.getYLocation(); y < storage.getYLocation() + 16; y++) {

							BlockPos coord = new BlockPos(x, y, z);

							if (CommonProxy.vanillaReplace.contains(chunk.getBlockState(coord))) {
								double val = noise.get3dNoiseValue(x + chunk.x * 16, y, z + chunk.z * 16, geomeOffset,
										genScale);
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
		chunk.setModified(true);// this is important as it marks it to be saved
	}

	private IBlockState pickBlockFromSet(double value, Set<IBlockState> list) {
		value = ((value + 1) / 2) * list.size();
		return list.stream().skip((int) value).findFirst().get();
	}
}