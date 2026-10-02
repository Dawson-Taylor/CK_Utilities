package com.codekanic.ckutilities.common.items.custom;

import com.codekanic.ckutilities.common.items.datacomponents.CKUDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/**
 * Sneak-use applies this upgrade to the copper alloy drill in the other hand.
 * A duplicate or a lower tier is rejected and is not consumed.
 */
public class DrillUpgradeItem extends Item {
    public enum Kind {
        HAMMER,
        EFFICIENCY,
        FORTUNE,
        SILK_TOUCH
    }

    private final Kind kind;
    /** Hammer radius (1, 2, or 4) or enchantment level. Silk touch is always 1. */
    private final int tier;

    public DrillUpgradeItem(Properties properties, Kind kind, int tier) {
        super(properties.stacksTo(64));
        this.kind = kind;
        this.tier = tier;
    }

    public Kind kind() {
        return this.kind;
    }

    public int tier() {
        return this.tier;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && player.isShiftKeyDown()) {
            return this.use(context.getLevel(), player, context.getHand());
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        ItemStack used = player.getItemInHand(hand);
        ItemStack drill = findDrill(player, hand, used);
        if (!(drill.getItem() instanceof DrillItem)) {
            if (!level.isClientSide()) {
                player.sendOverlayMessage(Component.translatable("tooltip.ckutilities.upgrade.no_drill"));
            }
            return InteractionResult.FAIL;
        }
        if (!canApply(level, drill)) {
            if (!level.isClientSide()) {
                player.sendOverlayMessage(Component.translatable("tooltip.ckutilities.upgrade.rejected"));
            }
            return InteractionResult.FAIL;
        }
        if (!level.isClientSide()) {
            apply(level, drill);
            Component name = used.getHoverName();
            used.consume(1, player);
            player.sendOverlayMessage(Component.translatable("tooltip.ckutilities.upgrade.applied", name));
            return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(hand));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable("tooltip.ckutilities.upgrade.apply").withStyle(ChatFormatting.GOLD));
        if (this.kind == Kind.SILK_TOUCH) {
            tooltip.accept(Component.translatable("tooltip.ckutilities.upgrade.silk").withStyle(ChatFormatting.GRAY));
        } else if (this.kind == Kind.FORTUNE) {
            tooltip.accept(Component.translatable("tooltip.ckutilities.upgrade.fortune").withStyle(ChatFormatting.GRAY));
        }
    }

    private static ItemStack findDrill(Player player, InteractionHand usedHand, ItemStack usedStack) {
        InteractionHand other = usedHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otherStack = player.getItemInHand(other);
        if (otherStack.getItem() instanceof DrillItem) {
            return otherStack;
        }
        ItemStack main = player.getMainHandItem();
        if (main != usedStack && main.getItem() instanceof DrillItem) {
            return main;
        }
        return ItemStack.EMPTY;
    }

    private boolean canApply(Level level, ItemStack drill) {
        if (this.kind == Kind.HAMMER) {
            return this.tier > drill.getOrDefault(CKUDataComponents.HAMMER_RADIUS.get(), 0);
        }
        Holder<Enchantment> enchantment = enchantment(level, enchantmentKey());
        return this.tier > EnchantmentHelper.getItemEnchantmentLevel(enchantment, drill);
    }

    private void apply(Level level, ItemStack drill) {
        if (this.kind == Kind.HAMMER) {
            drill.set(CKUDataComponents.HAMMER_RADIUS.get(), this.tier);
            return;
        }
        if (!drill.has(DataComponents.ENCHANTMENTS)) {
            drill.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        }
        Holder<Enchantment> enchantment = enchantment(level, enchantmentKey());
        Holder<Enchantment> fortune = enchantment(level, Enchantments.FORTUNE);
        Holder<Enchantment> silk = enchantment(level, Enchantments.SILK_TOUCH);
        EnchantmentHelper.updateEnchantments(drill, mutable -> {
            if (this.kind == Kind.FORTUNE) {
                mutable.set(silk, 0);
            } else if (this.kind == Kind.SILK_TOUCH) {
                mutable.set(fortune, 0);
            }
            mutable.set(enchantment, this.tier);
        });
    }

    private ResourceKey<Enchantment> enchantmentKey() {
        return switch (this.kind) {
            case EFFICIENCY -> Enchantments.EFFICIENCY;
            case FORTUNE -> Enchantments.FORTUNE;
            case SILK_TOUCH -> Enchantments.SILK_TOUCH;
            case HAMMER -> throw new IllegalStateException("Hammer upgrades are not enchantments");
        };
    }

    private static Holder<Enchantment> enchantment(Level level, ResourceKey<Enchantment> key) {
        return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
    }
}
