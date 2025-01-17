package com.CodeKanic.ckutilities.common.items.custom;


import com.CodeKanic.ckutilities.common.items.CKUItems;
import com.CodeKanic.ckutilities.common.items.CKUTags;
import com.CodeKanic.ckutilities.common.items.baseitems.ItemBase;
import com.CodeKanic.ckutilities.common.items.baseitems.ItemEnergy;

import com.CodeKanic.ckutilities.common.items.utils.ToolTier;
import com.CodeKanic.ckutilities.common.items.utils.Util;
import com.CodeKanic.ckutilities.common.items.utils.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DrillItem extends ItemEnergy {
//    public static final int HARVEST_LEVEL = 4;
    private static final int ENERGY_USE = 300;
    private static final List<ItemAbility> ACTIONS = List.of(ItemAbilities.SHOVEL_DIG, ItemAbilities.PICKAXE_DIG);

    private final ItemAttributeModifiers attributes_unpowered;
    private final ItemAttributeModifiers attributes_powered;

    public DrillItem() {
        super(CKUItems.defaultProps()
                        .stacksTo(1)
                        .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                        .component(DataComponents.TOOL, ToolTier.COPPER_ALLOY_DRILL.createToolProperties(CKUTags.Blocks.MINEABLE_WITH_DRILL))
                , 250000, 1000);

        attributes_unpowered = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 0.1F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -3.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();

        attributes_powered = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 5.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -3.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ItemAbility toolAction) {
        return ACTIONS.contains(toolAction);
    }

    @Nonnull
    @Override
    public InteractionResult interactLivingEntity(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull LivingEntity entityHit, @Nonnull InteractionHand hand) {
        int use = this.getEnergyUsePerBlock(stack);
        if (!(entityHit instanceof Player) || !((Player) entityHit).isCreative()) {
            if (this.getEnergyStored(stack) >= use) {
                this.extractEnergy(stack, use, false);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nonnull
    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(@Nonnull ItemStack stack) {
        return this.getEnergyStored(stack) >= ENERGY_USE
                ? this.attributes_powered
                : this.attributes_unpowered;
    }


    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull BlockState state) {
        return this.getEnergyStored(stack) >= this.getEnergyUsePerBlock(stack)
                ? 6.5f : 0.1f;
    }

    private Set<UUID> breakers = new HashSet<>();

    public boolean onBreakBlock(@Nonnull ItemStack stack, @Nonnull BlockPos pos, @Nonnull Player player) {
        if (!breakers.add(player.getUUID())) return false; // Prevent multiple break operations from cascading, and don't execute when sneaking. (Borrowed from Apotheosis)
        Level level = player.level();
        boolean toReturn = false;
        int use = this.getEnergyUsePerBlock(stack);

            //Block hit
            HitResult ray = player.pick(Util.getReachDistance(player), 1f, false);
            if (ray instanceof BlockHitResult trace) {

                    toReturn = breakBlocks(stack, 0, player.level(), pos, trace.getDirection(), player);
        }
        breakers.remove(player.getUUID());
        return toReturn;
    }

    @Override
    public boolean isCorrectToolForDrops(@Nonnull ItemStack stack, @Nonnull BlockState state) {
        return this.getEnergyStored(stack) >= this.getEnergyUsePerBlock(stack) && super.isCorrectToolForDrops(stack, state);
    }

    public int getEnergyUsePerBlock(ItemStack stack) {
        return ENERGY_USE;
    }

    public boolean breakBlocks(ItemStack stack, int radius, Level world, BlockPos aPos, Direction side, Player player) {
        int xRange = radius;
        int yRange = radius;
        int zRange = 0;

        //Corrects Blocks to hit depending on Side of original Block hit
        if (side.getAxis() == Direction.Axis.Y) {
            zRange = radius;
            yRange = 0;
        }
        if (side.getAxis() == Direction.Axis.X) {
            xRange = 0;
            zRange = radius;
        }

        //Not defined later because main Block is getting broken below
        BlockState state = world.getBlockState(aPos);
        float mainHardness = state.getDestroySpeed(world, aPos);

        //Break Middle Block first
        int use = this.getEnergyUsePerBlock(stack);
        if (this.getEnergyStored(stack) >= use) {
            if (!this.tryHarvestBlock(world, aPos, false, stack, player, use)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean tryHarvestBlock ( Level level, BlockPos pos, boolean isExtra, ItemStack stack, Player player, int use) {
        BlockState state = level.getBlockState(pos);
        float hardness = state.getDestroySpeed(level, pos);

        boolean canHarvest = (player.hasCorrectToolForDrops(state) || this.isCorrectToolForDrops(stack, state)) && (!isExtra || this.getDestroySpeed(stack, level.getBlockState(pos)) > 1.0F);
        if (hardness >= 0.0F && (!isExtra || canHarvest && !state.hasBlockEntity())) {
            if (!player.isCreative()) {
                this.extractEnergy(stack, use, false);
            }
            //Break the Block
            return WorldUtil.breakExtraBlock(stack, level, player, pos);
        }
        return false;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(@Nonnull ItemStack oldStack, @Nonnull ItemStack newStack) {
        return !ItemStack.isSameItem(newStack, oldStack);
    }



}
