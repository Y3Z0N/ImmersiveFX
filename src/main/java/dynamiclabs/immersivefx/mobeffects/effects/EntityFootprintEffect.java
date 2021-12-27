/*
 * Dynamic Surroundings: Mob Effects
 * Copyright (C) 2019  OreCruncher
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

package dynamiclabs.immersivefx.mobeffects.effects;

import javax.annotation.Nonnull;

import dynamiclabs.immersivefx.mobeffects.MobEffects;
import dynamiclabs.immersivefx.mobeffects.config.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import dynamiclabs.immersivefx.mobeffects.footsteps.FootprintStyle;
import dynamiclabs.immersivefx.mobeffects.footsteps.Generator;
import dynamiclabs.immersivefx.mobeffects.library.FootstepLibrary;
import dynamiclabs.immersivefx.sndctrl.api.effects.AbstractEntityEffect;
import dynamiclabs.immersivefx.sndctrl.api.effects.IEntityEffectManager;

@OnlyIn(Dist.CLIENT)
public class EntityFootprintEffect extends AbstractEntityEffect {

	private static final ResourceLocation NAME = new ResourceLocation(MobEffects.MOD_ID, "footprint");
	public static final FactoryHandler FACTORY = new FactoryHandler(
			EntityFootprintEffect.NAME,
			entity -> entity instanceof PlayerEntity ? new PlayerFootprintEffect() : new EntityFootprintEffect());

	protected Generator generator;

	public EntityFootprintEffect() {
		super(NAME);
	}

	@Override
	public void intitialize(@Nonnull final IEntityEffectManager state) {
		super.intitialize(state);
		this.generator = FootstepLibrary.createGenerator(getEntity());
	}

	@Override
	public void update() {
		this.generator.generateFootsteps(getEntity());
	}

	@Override
	public String toString() {
		return super.toString() + ": " + this.generator.getPedometer();
	}

	@OnlyIn(Dist.CLIENT)
	private static class PlayerFootprintEffect extends EntityFootprintEffect {

		protected FootprintStyle lastStyle;

		@Override
		public void intitialize(@Nonnull final IEntityEffectManager state) {
			super.intitialize(state);
			this.lastStyle = Config.CLIENT.footsteps.playerFootprintStyle.get();
		}

		@Override
		public void update() {
			final FootprintStyle currentStyle = Config.CLIENT.footsteps.playerFootprintStyle.get();
			if (this.lastStyle != currentStyle) {
				this.generator = FootstepLibrary.createGenerator(getEntity());
				this.lastStyle = currentStyle;
			}
			super.update();
		}

	}

}
