/*
 *  Dynamic Surroundings: Environs
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

package dynamiclabs.immersivefx.environs.shaders.aurora;

import dynamiclabs.immersivefx.lib.gui.Color;
import dynamiclabs.immersivefx.lib.gui.ColorPalette;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public final class AuroraColor {

    private static final List<AuroraColor> COLOR_SETS = new ArrayList<>();
    private static final float WARMER = 0.3F;
    private static final float COOLER = -0.3F;

    static {

        COLOR_SETS.add(new AuroraColor(new Color(0x0, 0xff, 0x99), new Color(0x33, 0xff, 0x00)));
        // Old Aurora Colors
        /*
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_GREEN, ColorPalette.AURORA_RED, ColorPalette.AURORA_BLUE));
        COLOR_SETS.add(new AuroraColor(ColorPalette.BLUE, ColorPalette.GREEN));
        COLOR_SETS.add(new AuroraColor(ColorPalette.MAGENTA, ColorPalette.GREEN));
        COLOR_SETS.add(new AuroraColor(ColorPalette.INDIGO, ColorPalette.GREEN));
        COLOR_SETS.add(new AuroraColor(ColorPalette.TURQOISE, ColorPalette.LGREEN));
        COLOR_SETS.add(new AuroraColor(ColorPalette.YELLOW, ColorPalette.RED));
        COLOR_SETS.add(new AuroraColor(ColorPalette.GREEN, ColorPalette.RED));
        COLOR_SETS.add(new AuroraColor(ColorPalette.GREEN, ColorPalette.YELLOW));
        COLOR_SETS.add(new AuroraColor(ColorPalette.RED, ColorPalette.YELLOW));
        COLOR_SETS.add(new AuroraColor(ColorPalette.NAVY, ColorPalette.INDIGO));
        COLOR_SETS.add(new AuroraColor(ColorPalette.CYAN, ColorPalette.MAGENTA));
         */

        //Better Aurora Colors // Edited Y3
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_CYAN,ColorPalette.AURORA_CYAN));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_GREEN_ISH,ColorPalette.AURORA_GREEN_ISH));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_PINK,ColorPalette.AURORA_PINK));

        // Warmer versions // Edited Y3
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_GREEN_ISH.luminance(WARMER),
                ColorPalette.AURORA_PINK.luminance(WARMER)));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_GREEN_ISH.luminance(WARMER),
                ColorPalette.AURORA_PINK.luminance(WARMER)));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_GREEN_ISH.luminance(WARMER),
                ColorPalette.AURORA_CYAN.luminance(WARMER)));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_CYAN.luminance(WARMER),
                ColorPalette.AURORA_GREEN_ISH.luminance(WARMER)));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_CYAN.luminance(WARMER),
                ColorPalette.AURORA_GREEN_ISH.luminance(WARMER)));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_GREEN_ISH.luminance(WARMER),
                ColorPalette.AURORA_PINK.luminance(WARMER), ColorPalette.AURORA_CYAN.luminance(WARMER)));

        // Cooler versions // Edited Y3
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_GREEN_ISH.luminance(COOLER),
                ColorPalette.AURORA_PINK.luminance(COOLER)));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_GREEN_ISH.luminance(COOLER),
                ColorPalette.AURORA_PINK.luminance(COOLER)));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_GREEN_ISH.luminance(COOLER),
                ColorPalette.AURORA_CYAN.luminance(COOLER)));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_CYAN.luminance(COOLER),
                ColorPalette.AURORA_GREEN_ISH.luminance(COOLER)));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_CYAN.luminance(COOLER),
                ColorPalette.AURORA_GREEN_ISH.luminance(COOLER)));
        COLOR_SETS.add(new AuroraColor(ColorPalette.AURORA_GREEN_ISH.luminance(COOLER),
                ColorPalette.AURORA_PINK.luminance(COOLER), ColorPalette.AURORA_CYAN.luminance(COOLER)));

    }

    /**
     * Color that forms the base of the aurora and is the brightest.
     */
    public final Color baseColor;
    /**
     * Color that forms the top of the aurora and usually fades to black.
     */
    public final Color fadeColor;
    /**
     * Mid-band color for aurora styles that use it.
     */
    public final Color middleColor;

    private AuroraColor(@Nonnull final Color base, @Nonnull final Color fade) {
        this(base, fade, base);
    }

    private AuroraColor(@Nonnull final Color base, @Nonnull final Color fade, @Nonnull final Color mid) {
        this.baseColor = base;
        this.fadeColor = fade;
        this.middleColor = mid;
    }

    @Nonnull
    public static AuroraColor get(@Nonnull final Random random) {
        final int idx = random.nextInt(COLOR_SETS.size());
        return COLOR_SETS.get(idx);
    }
}
