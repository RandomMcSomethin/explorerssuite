package com.random.explorerssuite.items;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;

public class PipsItem extends AliasedBlockItem {

    public PipsItem(Block block, Settings settings) {
        super(block, settings);
    }

    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            //stacks.add(getBlock().)
        }

    }
}
