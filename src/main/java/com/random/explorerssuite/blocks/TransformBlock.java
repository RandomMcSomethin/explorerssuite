package com.random.explorerssuite.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;

public class TransformBlock extends Block {
    private final Block block;
    public TransformBlock(Settings settings, Block bl) {
        super(settings);
        this.block = bl;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.block.getPlacementState(ctx);
    }
}
