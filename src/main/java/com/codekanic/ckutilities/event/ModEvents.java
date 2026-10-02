package com.codekanic.ckutilities.event;

import com.codekanic.ckutilities.CKUtilities;
import com.codekanic.ckutilities.common.items.custom.BatteryItem;
import com.codekanic.ckutilities.common.items.custom.DrillItem;
import com.codekanic.ckutilities.common.items.custom.HammerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = CKUtilities.MODID)
public class ModEvents {
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();

    @SubscribeEvent
    public static void onHammerUsage(BreakBlockEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getMainHandItem();
        if (mainHandItem.getItem() instanceof HammerItem hammer && player instanceof ServerPlayer serverPlayer) {
            BlockPos initialBlockPos = event.getPos();
            if (HARVESTED_BLOCKS.contains(initialBlockPos)) {
                return;
            }

            for (BlockPos pos : HammerItem.getBlocksToBeDestroyed(1, initialBlockPos, serverPlayer)) {
                // New BlockPos instances never match with ==, so the center was broken twice.
                if (pos.equals(initialBlockPos) || !hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) {
                    continue;
                }

                HARVESTED_BLOCKS.add(pos);
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreaking(BreakBlockEvent event) {
        Player player = event.getPlayer();
        BlockPos pos = event.getPos();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof DrillItem drillItem && drillItem.onBreakBlock(stack, pos, player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        ItemStack offhand = player.getOffhandItem();
        if (!(offhand.getItem() instanceof BatteryItem battery)) {
            return;
        }
        // A battery sitting in the main inventory already charges from inventoryTick.
        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            if (stack == offhand) {
                return;
            }
        }
        battery.chargeInventory(offhand, player);
    }
}
