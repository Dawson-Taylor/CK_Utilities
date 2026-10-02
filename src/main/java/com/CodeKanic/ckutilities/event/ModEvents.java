package com.CodeKanic.ckutilities.event;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.items.custom.BatteryItem;
import com.CodeKanic.ckutilities.common.items.custom.DrillItem;
import com.CodeKanic.ckutilities.common.items.custom.HammerItem;
import com.CodeKanic.ckutilities.common.items.interfaces.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = CKUtilities.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();

    @SubscribeEvent
    public static void onHammerUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getMainHandItem();

        if(mainHandItem.getItem() instanceof HammerItem hammer && player instanceof ServerPlayer serverPlayer) {
            BlockPos initialBlockPos = event.getPos();
            if(HARVESTED_BLOCKS.contains(initialBlockPos)) {
                return;
            }

            for(BlockPos pos : HammerItem.getBlocksToBeDestroyed(1, initialBlockPos, serverPlayer)) {
                // A new BlockPos for the center never matches with ==, so the center was broken twice.
                if(pos.equals(initialBlockPos) || !hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) {
                    continue;
                }

                HARVESTED_BLOCKS.add(pos);
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }
    @SubscribeEvent
    public static void onBlockBreaking(BlockEvent.BreakEvent event) {
        final Player player = event.getPlayer();
        final LevelAccessor level = event.getLevel();
        final BlockPos pos = event.getPos();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof DrillItem drillItem) {
            boolean toReturn = drillItem.onBreakBlock(stack, pos, player);
            if (toReturn) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        ItemStack offhand = player.getOffhandItem();
        if (!(offhand.getItem() instanceof BatteryItem battery) || !ItemUtil.isEnabled(offhand)) {
            return;
        }
        // A battery that is also in the main inventory already charges from inventoryTick.
        for (ItemStack stack : player.getInventory().items) {
            if (stack == offhand) {
                return;
            }
        }
        if (!battery.claimChargeTick(player.level(), offhand)) {
            return;
        }
        battery.chargeInventory(offhand, player);
    }

}
