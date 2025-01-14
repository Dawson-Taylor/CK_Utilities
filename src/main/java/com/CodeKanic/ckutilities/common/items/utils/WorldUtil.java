package com.CodeKanic.ckutilities.common.items.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;

public class WorldUtil {

    public static boolean breakExtraBlock(ItemStack stack, Level level, Player player, BlockPos pos) {


        if (player.isCreative()) {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            if (block.onDestroyedByPlayer(state, level, pos, player, false, state.getFluidState())) {
                block.destroy(level, pos, state);
            }
            return true;
        }

        BlockState tempstate = level.getBlockState(pos);
        BlockState state = tempstate.getBlock().playerWillDestroy(level, pos, tempstate, player);
        Block block = state.getBlock();

        if (state.isAir()) {
            return false;
        }


        stack.mineBlock(level, state, pos, player);


        if (!level.isClientSide) {

            var event = CommonHooks.fireBlockBreak(level, ((ServerPlayer) player).gameMode.getGameModeForPlayer(), (ServerPlayer) player, pos, state);
            if (event.isCanceled()) {
                return false;
            }

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (block.onDestroyedByPlayer(state, level, pos, player, true, state.getFluidState())) { // boolean is if block can be harvested, checked above
                block.destroy(level, pos, state);
                block.playerDestroy(level, player, pos, state, blockEntity, stack);
            }

            return true;
        }

        else {

            level.levelEvent(2001, pos, Block.getId(state));
            if (block.onDestroyedByPlayer(state, level, pos, player, true, state.getFluidState())) {
                block.destroy(level, pos, state);
            }

            return true;
        }
    }
}
