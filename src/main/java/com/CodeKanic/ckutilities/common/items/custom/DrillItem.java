package com.CodeKanic.ckutilities.common.items.custom;

import com.CodeKanic.ckutilities.common.items.CKUTags;
import com.CodeKanic.ckutilities.common.items.baseitems.ItemEnergy;
import com.CodeKanic.ckutilities.common.items.utils.ToolTier;
import com.CodeKanic.ckutilities.common.items.utils.Util;
import com.CodeKanic.ckutilities.common.items.utils.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DrillItem extends ItemEnergy {
    private static final int ENERGY_USE = 300;

    private final ItemAttributeModifiers attributesUnpowered;
    private final ItemAttributeModifiers attributesPowered;
    private final Set<UUID> breakers = new HashSet<>();

    public DrillItem(Properties properties) {
        super(properties
                        .stacksTo(1)
                        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                        .component(DataComponents.TOOL, drillTool()),
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
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entityHit, InteractionHand hand) {
        int use = this.getEnergyUsePerBlock(stack);
        if (!(entityHit instanceof Player target) || !target.isCreative()) {
            if (this.getEnergyStored(stack) >= use) {
                this.extractEnergy(stack, use, false);
            }
        }
        return InteractionResult.SUCCESS;
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
        if (!breakers.add(player.getUUID())) {
            return false;
        }
        boolean broken = false;
        HitResult ray = player.pick(Util.getReachDistance(player), 1f, false);
        if (ray instanceof BlockHitResult) {
            broken = breakBlocks(stack, player.level(), pos, player);
        }
        breakers.remove(player.getUUID());
        return broken;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return this.getEnergyStored(stack) >= this.getEnergyUsePerBlock(stack) && super.isCorrectToolForDrops(stack, state);
    }

    public int getEnergyUsePerBlock(ItemStack stack) {
        return ENERGY_USE;
    }

    public boolean breakBlocks(ItemStack stack, Level world, BlockPos pos, Player player) {
        int use = this.getEnergyUsePerBlock(stack);
        if (this.getEnergyStored(stack) >= use) {
            return this.tryHarvestBlock(world, pos, false, stack, player, use);
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
}
