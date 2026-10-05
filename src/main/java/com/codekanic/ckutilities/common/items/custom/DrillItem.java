package com.codekanic.ckutilities.common.items.custom;

import com.codekanic.ckutilities.common.items.CKUTags;
import com.codekanic.ckutilities.common.items.baseitems.ItemEnergy;
import com.codekanic.ckutilities.common.items.datacomponents.CKUDataComponents;
import com.codekanic.ckutilities.common.items.utils.ToolTier;
import com.codekanic.ckutilities.common.items.utils.Util;
import com.codekanic.ckutilities.common.items.utils.WorldUtil;
import com.codekanic.ckutilities.common.menu.DrillUpgradeMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.function.Consumer;

public class DrillItem extends ItemEnergy {
    private static final int ENERGY_USE = 300;
    private static final ThreadLocal<Boolean> BREAKING = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private final ItemAttributeModifiers attributesUnpowered;
    private final ItemAttributeModifiers attributesPowered;

    public DrillItem(Properties properties) {
        super(properties
                        .stacksTo(1)
                        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                        .component(DataComponents.TOOL, drillTool())
                        .component(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY),
                250000, 1000);

        attributesUnpowered = attributes(0.1F);
        attributesPowered = attributes(5.0F);
    }

    private static ItemAttributeModifiers attributes(float attackDamage) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -3.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    private static Tool drillTool() {
        HolderGetter<Block> blocks = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return new Tool(List.of(
                Tool.Rule.deniesDrops(blocks.getOrThrow(BlockTags.INCORRECT_FOR_NETHERITE_TOOL)),
                Tool.Rule.minesAndDrops(blocks.getOrThrow(CKUTags.Blocks.MINEABLE_WITH_DRILL), ToolTier.COPPER_ALLOY_DRILL.speed())
        ), 1.0F, 1, true);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return this.getEnergyStored(stack) >= ENERGY_USE ? this.attributesPowered : this.attributesUnpowered;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return this.getEnergyStored(stack) >= this.getEnergyUsePerBlock(stack) ? 6.5F : 0.1F;
    }

    public boolean onBreakBlock(ItemStack stack, BlockPos pos, Player player) {
        if (BREAKING.get()) {
            return false;
        }
        BREAKING.set(Boolean.TRUE);
        try {
            HitResult ray = player.pick(Util.getReachDistance(player), 1f, false);
            if (!(ray instanceof BlockHitResult)) {
                return false;
            }
            boolean brokeCenter = breakBlock(stack, player.level(), pos, player, false);
            if (brokeCenter) {
                breakHammerArea(stack, pos, player);
            }
            return brokeCenter;
        } finally {
            BREAKING.set(Boolean.FALSE);
        }
    }

    /**
     * Breaks the hammer square around the block that was just mined.
     * The center is skipped so it is not dropped twice. Radius 1, 2, and 4 are 3x3, 5x5, and 9x9.
     */
    private void breakHammerArea(ItemStack stack, BlockPos origin, Player player) {
        int radius = stack.getOrDefault(CKUDataComponents.HAMMER_RADIUS.get(), 0);
        if (radius <= 0 || !(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        for (BlockPos extra : HammerItem.getBlocksToBeDestroyed(radius, origin, serverPlayer)) {
            if (extra.equals(origin)) {
                continue;
            }
            breakBlock(stack, player.level(), extra, player, true);
        }
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return this.getEnergyStored(stack) >= this.getEnergyUsePerBlock(stack) && super.isCorrectToolForDrops(stack, state);
    }

    public int getEnergyUsePerBlock(ItemStack stack) {
        return ENERGY_USE;
    }

    public boolean breakBlock(ItemStack stack, Level world, BlockPos pos, Player player) {
        return breakBlock(stack, world, pos, player, false);
    }

    private boolean breakBlock(ItemStack stack, Level world, BlockPos pos, Player player, boolean extra) {
        int use = this.getEnergyUsePerBlock(stack);
        // Creative players still need a charge to run the drill, but tryHarvestBlock does not deduct it.
        if (this.getEnergyStored(stack) >= use) {
            return this.tryHarvestBlock(world, pos, extra, stack, player, use);
        }
        return false;
    }

    private boolean tryHarvestBlock(Level level, BlockPos pos, boolean isExtra, ItemStack stack, Player player, int use) {
        BlockState state = level.getBlockState(pos);
        float hardness = state.getDestroySpeed(level, pos);
        boolean canHarvest = (player.hasCorrectToolForDrops(state) || this.isCorrectToolForDrops(stack, state))
                && (!isExtra || this.getDestroySpeed(stack, state) > 1.0F);
        if (hardness >= 0.0F && (!isExtra || canHarvest && !state.hasBlockEntity())) {
            if (!player.isCreative()) {
                this.extractEnergy(stack, use, false);
            }
            return WorldUtil.breakExtraBlock(stack, level, player, pos);
        }
        return false;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return !ItemStack.isSameItem(newStack, oldStack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.isEnchanted() || stack.getOrDefault(CKUDataComponents.HAMMER_RADIUS.get(), 0) > 0;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }
        return this.use(context.getLevel(), player, context.getHand());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = player.getItemInHand(hand);
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, inventory, ignored) -> new DrillUpgradeMenu(containerId, inventory, stack),
                    Component.translatable("container.ckutilities.drill_upgrades")
            ));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable("tooltip.ckutilities.drill.open").withStyle(ChatFormatting.GRAY));
        int radius = stack.getOrDefault(CKUDataComponents.HAMMER_RADIUS.get(), 0);
        if (radius > 0) {
            int size = radius * 2 + 1;
            tooltip.accept(Component.translatable("tooltip.ckutilities.drill.hammer", size, size).withStyle(ChatFormatting.GOLD));
        }
    }
}
