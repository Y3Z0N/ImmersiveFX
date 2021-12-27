/*
 * Dynamic Surroundings
 * Copyright (C) 2020  OreCruncher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package dynamiclabs.immersivefx.dsurround.gui;

import dynamiclabs.immersivefx.lib.GameUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import dynamiclabs.immersivefx.dsurround.DynamicSurroundings;
import dynamiclabs.immersivefx.dsurround.config.Config;
import dynamiclabs.immersivefx.dsurround.huds.lightlevel.LightLevelHUD;

@Mod.EventBusSubscriber(modid = DynamicSurroundings.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Keys {

    private static KeyBinding lightLevelHUD;
    private static KeyBinding chunkBorders;

    public static void register() {
        if ((Config.CLIENT.logging.flagMask.get() & Config.Flags.ALLOW_CHUNK_BORDER_HUD) != 0) {
            lightLevelHUD = new KeyBinding(
                    "dsurround.text.lightlevel.toggle",
                    InputMappings.INPUT_INVALID.getKeyCode(),
                    "dsurround.text.controls.group");
            ClientRegistry.registerKeyBinding(lightLevelHUD);
        }

        if ((Config.CLIENT.logging.flagMask.get() & Config.Flags.ALLOW_LIGHTLEVEL_HUD) != 0) {
            chunkBorders = new KeyBinding(
                    "dsurround.text.chunkborders.toggle",
                    InputMappings.INPUT_INVALID.getKeyCode(),
                    "dsurround.text.controls.group");
            ClientRegistry.registerKeyBinding(chunkBorders);
        }
    }

    @SubscribeEvent
    public static void keyPressed(InputEvent.KeyInputEvent event) {
        if (GameUtils.getMC().currentScreen == null && GameUtils.getPlayer() != null) {
            if (lightLevelHUD != null && lightLevelHUD.isPressed()) {
                LightLevelHUD.toggleDisplay();
            }

            if (chunkBorders != null && chunkBorders.isPressed()) {
                GameUtils.getMC().debugRenderer.toggleChunkBorders();
            }
        }
    }
}