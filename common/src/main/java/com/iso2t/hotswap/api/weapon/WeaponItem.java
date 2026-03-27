package com.iso2t.hotswap.api.weapon;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

public final class WeaponItem {

	private static final float BASE = 2.f;

	private final Item item;
	private final Player player;
	private final DamageSource source;
	private final Entity target;

	public WeaponItem (@NotNull ItemStack stack, Player player, DamageSource source, Entity target) {
		Preconditions.checkArgument(stack.is(ItemTags.SWORDS) || stack.is(ItemTags.AXES));
		this.item = stack.getItem();
		this.player = player;
		this.source = source; //player.level().damageSources().playerAttack(player);
		this.target = target;
	}

	public WeaponItem (Item item, Player player, DamageSource source, Entity target) {
		this(new ItemStack(item), player, source, target);
	}

	public float getDamageValue () {
		return getDamageFor(new ItemStack(getItem(), 1));
	}

	public float getSpeedValue () {
		return getSpeedFor(new ItemStack(getItem(), 1));
	}

	private float getDamageFor (@NotNull ItemStack stack) {
		AtomicDouble total = new AtomicDouble(0.f);
		stack.forEachModifier(EquipmentSlot.MAINHAND, (holder, modifier) -> {
			if (holder.value() == Attributes.ATTACK_DAMAGE) {
				total.getAndAdd(modifier.amount());
			}
		});

		return total.floatValue();
	}

	// TODO: Return attack speed for given tool.
	private float getSpeedFor (ItemStack stack) {
		AtomicDouble total = new AtomicDouble(0.f);
		stack.forEachModifier(EquipmentSlot.MAINHAND, (holder, modifier) -> {
			if (holder.value() == Attributes.ATTACK_SPEED) {
                total.getAndAdd(modifier.amount());
			}
		});

		return total.floatValue();
	}

	public Item getItem () {
		return item;
	}

	public Player getPlayer () {
		return player;
	}

	public Entity getTarget () {
		return target;
	}

	public DamageSource getSource () {
		return source;
	}

}
