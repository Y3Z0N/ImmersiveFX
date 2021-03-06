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
package dynamiclabs.immersivefx.environs.mixins;

import dynamiclabs.immersivefx.environs.config.Config;
import dynamiclabs.immersivefx.environs.handlers.FogHandler;
import dynamiclabs.immersivefx.environs.library.BiomeInfo;
import dynamiclabs.immersivefx.environs.library.BiomeUtil;
import dynamiclabs.immersivefx.environs.misc.IMixinBiomeData;
import dynamiclabs.immersivefx.lib.gui.Color;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(Biome.class)
public class MixinBiome implements IMixinBiomeData {

    private BiomeInfo environs_biomeInfo;

    @Nullable
    @Override
    public BiomeInfo getInfo() {
        return this.environs_biomeInfo;
    }

    @Override
    public void setInfo(@Nullable BiomeInfo info) {
        this.environs_biomeInfo = info;
    }

    @Inject(method = "getFogColor()I", at = @At("HEAD"), cancellable = true)
    public void getFogColor(@Nonnull final CallbackInfoReturnable<Integer> cir) {
        if (doFogColor()) {
            // Need to invoke getBiomeData() because it will populate environs_biomeInfo if not already set
            final BiomeInfo info = BiomeUtil.getBiomeData((Biome) (Object) this);
            final Color color = info.getFogColor();
            if (color != null) {
                cir.setReturnValue(color.rgb());
            }
        }
    }

    private boolean doFogColor() {
        return FogHandler.doFog() && Config.CLIENT.fog.enableBiomeFog.get();
    }
}
