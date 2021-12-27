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

package dynamiclabs.immersivefx.specialfx;

import dynamiclabs.immersivefx.dsurround.DynamicSurroundings;
import net.minecraft.entity.monster.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLEnvironment;

@EventBusSubscriber(modid = DynamicSurroundings.MOD_ID)
public class BloodParticles
{
	@SuppressWarnings("unused")
	private static Logger log = LogManager.getLogger(BloodParticles.class);

	// @formatter:off
	private static final IParticleData
			ZOMBIFIED_PIG_PARTICLE_DATA = new BlockParticleData(ParticleTypes.BLOCK, Blocks.LIME_TERRACOTTA.getDefaultState()),
			PHANTOM_PARTICLE_DATA = new BlockParticleData(ParticleTypes.BLOCK, Blocks.GRAY_STAINED_GLASS.getDefaultState()),
			BLAZE_PARTICLE_DATA = ParticleTypes.FLAME, SLIME_PARTICLE_DATA = ParticleTypes.ITEM_SLIME,
			MAGMA_CUBE_PARTICLE_DATA = new BlockParticleData(ParticleTypes.BLOCK, Blocks.MAGMA_BLOCK.getDefaultState()),
			ENDER_PARTICLE_DATA = new BlockParticleData(ParticleTypes.BLOCK, Blocks.PURPLE_CONCRETE.getDefaultState()),
			LAVA_PARTICLE_DATA = new BlockParticleData(ParticleTypes.BLOCK, Blocks.LAVA.getDefaultState()),
			CREEPER_PARTICLE_DATA = new BlockParticleData(ParticleTypes.BLOCK, Blocks.GREEN_TERRACOTTA.getDefaultState()),
			DEFAULT_PARTICLE_DATA = new BlockParticleData(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState());
	// @formatter:on

	@SubscribeEvent
	public static void onLivingAttacked(LivingAttackEvent event)
	{
		if (!isValidDamageSource(event.getSource()))
			return;

		if (FMLEnvironment.dist != Dist.CLIENT || !event.getEntity().world.isRemote)
			return;

		LivingEntity entity = event.getEntityLiving();
		float amount = event.getAmount();
		DamageSource source = event.getSource();

		// compute number of particles to spawn based on damage dealt

		int numParticles;

		switch (source.damageType)
		{
		case "arrow":
			numParticles = 1 + ((int) (amount * 3));
			break;
		case "trident":
			numParticles = 1 + ((int) (amount * 3));
			break;
		case "player":
			PlayerEntity player = (PlayerEntity) source.getTrueSource();
			ItemStack weapon = player.getHeldItemMainhand();
			if (!weapon.isEmpty())
			{
				Item item = weapon.getItem();
				float itemAttackDamage;
				if (item instanceof ToolItem)
				{
					itemAttackDamage = ((ToolItem) item).attackDamage;
				}
				else if (item instanceof SwordItem)
				{
					itemAttackDamage = ((SwordItem) item).getAttackDamage();
				}
				else
				{
					itemAttackDamage = 1.0f;
				}
				/*msgBuilder.append("item attack dmg = ").append(String.format("% 4.1f", itemAttackDamage)).append(", ");*/
				numParticles = 1 + ((int) ((amount + itemAttackDamage) * 2));
				break;
			}
			else
			{
				// msgBuilder.append("no item, ");
			}
			// fallthrough to default case
		default:
			if (amount == 0.0f)
			{
				numParticles = 20;
			}
			else
			{
				numParticles = 5 + ((int) (amount * 15));
			}
		}

		// msgBuilder.append("numParticles = ").append(String.format("%3d",
		// numParticles)).append(", ");

		// Select which particle to use based on the entity

		IParticleData particleData;

		if (entity instanceof AbstractSkeletonEntity || entity instanceof SkeletonHorseEntity)
			return; // skeletons have no blood
		else if (entity instanceof ZombifiedPiglinEntity || entity instanceof ZoglinEntity || entity.getType().getRegistryName().getPath().contains("skeleton"))
			particleData = ZOMBIFIED_PIG_PARTICLE_DATA;
		else if (entity instanceof PhantomEntity)
			particleData = PHANTOM_PARTICLE_DATA;
		else if (entity instanceof BlazeEntity)
			particleData = BLAZE_PARTICLE_DATA;
		else if (entity instanceof MagmaCubeEntity)
			particleData = MAGMA_CUBE_PARTICLE_DATA;
		else if (entity instanceof SlimeEntity)
			particleData = SLIME_PARTICLE_DATA;
		else if (entity instanceof CreeperEntity)
			particleData = CREEPER_PARTICLE_DATA;
		else if (entity instanceof EndermanEntity || entity instanceof EnderDragonEntity || entity instanceof EndermiteEntity)
			particleData = ENDER_PARTICLE_DATA;
		else if (entity.getType().getRegistryName().getPath().equals("lava_monster"))
			particleData = LAVA_PARTICLE_DATA;
		else
			particleData = DEFAULT_PARTICLE_DATA;

		// compute particle speed and location, other misc variables

		WorldRenderer worldRenderer = Minecraft.getInstance().worldRenderer;

		Vector3d pos = entity.getPositionVec();

		double x = pos.x;
		double y = pos.y + entity.getHeight() / 1.5;
		double z = pos.z;

		// actually spawn the particles

		for (int i = 0; i < numParticles; i++)
		{
			worldRenderer.addParticle(particleData, false, x, y, z, 0, 0, 0);
		}

		/*msgBuilder.append("damage source = ").append(damageSourceToString(source)).append(", ");

		log.debug(msgBuilder.toString());*/
	}

	private static boolean isValidDamageSource(DamageSource source)
	{
		String damageType = source.damageType;
		return (source == DamageSource.FALL || source == DamageSource.GENERIC || source.isProjectile() || damageType.equalsIgnoreCase("player") || damageType.equalsIgnoreCase("mob") || damageType.equalsIgnoreCase("thorns"));
	}

	@SuppressWarnings("unused")
	private static String damageSourceToString(DamageSource source)
	{
		if (source == null)
		{
			return null;
		}
		else
		{
			StringBuilder sb = new StringBuilder();
			sb.append(source.damageType).append(" {");

			if (source.getHungerDamage() != 0)
				sb.append(" hunger damage = ").append(source.getHungerDamage()).append(',');

			if (source.isFireDamage())
				sb.append(" fire damage,");

			if (source.isProjectile())
				sb.append(" projectile,");

			if (source.isDifficultyScaled())
				sb.append(" difficulty scaled,");

			if (source.isMagicDamage())
				sb.append(" magic,");

			if (source.isExplosion())
				sb.append(" explosion,");

			if (source.isDamageAbsolute())
				sb.append(" absolute damage,");

			if (source.isUnblockable())
				sb.append(" unblockable,");

			if (source.canHarmInCreative())
				sb.append(" can harm creative players,");

			if (source.getImmediateSource() != null)
				sb.append(" immediate source = ").append(source.getImmediateSource().getType().getRegistryName()).append(',');

			if (source.getTrueSource() != null)
				sb.append(" true source = ").append(source.getTrueSource().getType().getRegistryName()).append(',');

			if (sb.charAt(sb.length() - 1) == ',')
				sb.setCharAt(sb.length() - 1, ' ');

			sb.append('}');
			return sb.toString();
		}
	}

}
