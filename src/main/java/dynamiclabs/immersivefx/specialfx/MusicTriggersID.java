package dynamiclabs.immersivefx.specialfx;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

import net.minecraft.world.IWorld;

import java.util.Map;
import java.util.HashMap;

import dynamiclabs.immersivefx.dsurround.DynamicSurroundings;

public class MusicTriggersID {
	@Mod.EventBusSubscriber
	private static class GlobalTrigger {
		@SubscribeEvent
		public static void onWorldLoad(WorldEvent.Load event) {
			IWorld world = event.getWorld();
			Map<String, Object> dependencies = new HashMap<>();
			dependencies.put("world", world);
			dependencies.put("event", event);
			executeProcedure(dependencies);
		}
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				Immersivefx.LOGGER.warn("ImmersiveFX: Failed to load checks for MusicTriggers.");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		if (net.minecraftforge.fml.ModList.get().isLoaded("musictriggers")) {
			Immersivefx.LOGGER.warn(
					"ImmersiveFX: You are using MusicTriggers. Normally there should be no problems. If you should have problems use ingame CTRL + I and mute the sounds that overlap.");
		}
	}
}
