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

package dynamiclabs.immersivefx.mobeffects;

import dynamiclabs.immersivefx.dsurround.DynamicSurroundings;
import dynamiclabs.immersivefx.lib.logging.ModLog;
import dynamiclabs.immersivefx.mobeffects.config.Config;
import dynamiclabs.immersivefx.mobeffects.effects.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import dynamiclabs.immersivefx.mobeffects.effects.*;
import dynamiclabs.immersivefx.mobeffects.library.Constants;
import dynamiclabs.immersivefx.mobeffects.library.Libraries;
import dynamiclabs.immersivefx.sndctrl.api.IMC;

@Mod(MobEffects.MOD_ID)
public final class MobEffects {

    /**
     * ID of the mod
     */
    public static final String MOD_ID = "mobeffects";
    /**
     * Logging instance for trace
     */
    public static final ModLog LOGGER = new ModLog(MobEffects.class);

    public MobEffects() {

        // Since we are 100% client side
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        if (FMLEnvironment.dist == Dist.CLIENT) {
            // Various event bus registrations
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
            MinecraftForge.EVENT_BUS.register(this);

            // Initialize our configuration
            Config.setup();

            DynamicSurroundings.doConfigMenuSetup();
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Sound Categories
        IMC.registerSoundCategory(
                Constants.FOOTSTEPS,
                Constants.TOOLBAR
        );

        // Register our AcousticEvents
        IMC.registerAcousticEvent(
                Constants.WALK,
                Constants.WANDER,
                Constants.SWIM,
                Constants.RUN,
                Constants.JUMP,
                Constants.LAND,
                Constants.CLIMB,
                Constants.CLIMB_RUN,
                Constants.DOWN,
                Constants.DOWN_RUN,
                Constants.UP,
                Constants.UP_RUN
        );

        // Register our effect handlers
        IMC.registerEffectFactoryHandler(EntityFootprintEffect.FACTORY);
        if (Config.CLIENT.effects.showBreath.get())
            IMC.registerEffectFactoryHandler(EntityBreathEffect.FACTORY);
        if (Config.CLIENT.effects.enableBowEffect.get())
            IMC.registerEffectFactoryHandler(EntityBowEffect.FACTORY);
        if (Config.CLIENT.effects.enableToolbarEffect.get())
            IMC.registerEffectFactoryHandler(PlayerToolbarEffect.FACTORY);
        if (Config.CLIENT.effects.enableSwingEffect.get())
            IMC.registerEffectFactoryHandler(EntitySwingEffect.FACTORY);

        // Callback for completions
        IMC.registerCompletionCallback(Libraries::initialize);
        IMC.registerCompletionCallback(Libraries::complete);
    }
}
