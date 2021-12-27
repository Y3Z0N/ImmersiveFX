/*
 *  Dynamic Surroundings
 *  Copyright (C) 2020  OreCruncher
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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package dynamiclabs.immersivefx.environs.handlers;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;
import dynamiclabs.immersivefx.environs.shaders.ShaderPrograms;
import dynamiclabs.immersivefx.environs.shaders.aurora.AuroraFactory;
import dynamiclabs.immersivefx.environs.shaders.aurora.AuroraUtils;
import dynamiclabs.immersivefx.environs.shaders.aurora.IAurora;
import dynamiclabs.immersivefx.lib.GameUtils;
import dynamiclabs.immersivefx.lib.events.DiagnosticEvent;
import dynamiclabs.immersivefx.lib.math.LoggingTimerEMA;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import dynamiclabs.immersivefx.environs.config.Config;
import dynamiclabs.immersivefx.environs.Environs;
import dynamiclabs.immersivefx.lib.logging.IModLog;

@OnlyIn(Dist.CLIENT)
public final class AuroraHandler extends HandlerBase {

	private static final IModLog LOGGER = Environs.LOGGER.createChild(AuroraHandler.class);
	private static AuroraHandler handler;

	private final LoggingTimerEMA render = new LoggingTimerEMA("Render Aurora");
	private IAurora current;
	private int dimensionId;

	public AuroraHandler() {
		super("Aurora");
	}

	@Override
	public void onConnect() {
		handler = this;
		this.current = null;
	}

	@Override
	public void onDisconnect() {
		this.current = null;
		handler = null;
	}

	private boolean isAuroraTimeOfDay() {
		return CommonState.getDayCycle().isAuroraVisible();
	}

	private boolean canSpawnAurora() {
		return this.current == null && canAuroraStay();
	}

	private boolean canAuroraStay() {
		if (!Config.CLIENT.aurora.auroraEnabled.get())
			return false;

		return isAuroraTimeOfDay()
				&& AuroraUtils.getChunkRenderDistance() >= 6
				&& AuroraUtils.dimensionHasAuroras()
				&& CommonState.getTruePlayerBiome().getHasAurora();
	}

	@Override
	public void process(@Nonnull final PlayerEntity player) {

		// Process the current aurora
		if (this.current != null) {
			// If completed or the player changed dimensions we want to kill
			// outright
			if (this.current.isComplete() || this.dimensionId != CommonState.getDimensionId()
					|| !Config.CLIENT.aurora.auroraEnabled.get()) {
				this.current = null;
			} else {
				this.current.update();
				final boolean isDying = this.current.isDying();
				final boolean canStay = canAuroraStay();
				if (isDying && canStay) {
					LOGGER.debug("Unfading aurora...");
					this.current.setFading(false);
				} else if (!isDying && !canStay) {
					LOGGER.debug("Aurora fade...");
					this.current.setFading(true);
				}
			}
		}

		// If there isn't a current aurora see if it needs to spawn
		if (canSpawnAurora()) {
			this.current = AuroraFactory.produce(AuroraUtils.getSeed());
			LOGGER.debug("New aurora [%s]", this.current.toString());
		}

		// Set the dimension in case it changed
		this.dimensionId = CommonState.getDimensionId();
	}

	private void doRender(@Nonnull final MatrixStack matrixStack, final float partialTick) {
		this.render.begin();
		if (this.current != null) {
			this.current.render(matrixStack, partialTick);
		}
		this.render.end();
	}

	/**
	 * Hook called by a Mixin to render the aurora.  Hook is at the tail end of particle rendering.
	 * @param matrixStack Matrix stack of the current environment
	 * @param partialTick Partial tick, duh
	 */
	public static void renderHook(@Nonnull final MatrixStack matrixStack, final float partialTick) {
		if (handler != null) {
			final IProfiler profiler = GameUtils.getMC().getProfiler();
			profiler.startSection("Aurora Render");
			handler.doRender(matrixStack, partialTick);
			profiler.endSection();
		}
	}

	@SubscribeEvent
	public void diagnostic(@Nonnull final DiagnosticEvent event) {
		if (Config.CLIENT.logging.enableLogging.get()) {
			if (ShaderPrograms.MANAGER.supported()) {
				event.getLeft().add("Aurora: " + (this.current == null ? "NONE" : this.current.toString()));
				event.getRenderTimers().add(this.render);
			} else {
				event.getLeft().add("Aurora: Disabled");
			}
		}
	}

}
