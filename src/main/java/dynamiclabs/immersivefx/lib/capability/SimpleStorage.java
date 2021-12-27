/*
 * Licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package dynamiclabs.immersivefx.lib.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * A simple implementation of {@link ICapabilityProvider} that supports a single {@link Capability} handler instance.
 *
 * @author Choonster
 */
public final class SimpleStorage<T extends INBTSerializable<CompoundNBT>> implements Capability.IStorage<T> {

	@Nullable
	@Override
	public INBT writeNBT(@Nonnull final Capability<T> capability, @Nonnull final T instance, @Nullable final Direction side) {
		return instance.serializeNBT();
	}

	@Override
	public void readNBT(@Nonnull final Capability<T> capability, @Nonnull final T instance, @Nullable final Direction side, @Nonnull final INBT nbt) {
		instance.deserializeNBT((CompoundNBT) nbt);
	}
}