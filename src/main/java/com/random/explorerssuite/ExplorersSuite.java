package com.random.explorerssuite;

import com.random.explorerssuite.blocks.EffectCandleBlock;
import com.random.explorerssuite.blocks.EffectCandleBlockEntity;
import com.random.explorerssuite.blocks.TransformBlock;
import com.random.explorerssuite.config.ExplorersSuiteConfig;
import com.random.explorerssuite.effects.CalmingStatusEffect;
import com.random.explorerssuite.effects.InsomniaStatusEffect;
import com.random.explorerssuite.helper.EventListeners;
import com.random.explorerssuite.items.FireStarterItem;
import com.random.explorerssuite.items.PipsItem;
import com.random.explorerssuite.mixin.BrewingStandRegistryMixin;
import com.random.explorerssuite.mixin.ItemAccessor;
import jdk.jfr.Event;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExplorersSuite implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("explorerssuite");

	// tags
	public static final TagKey<Item> DISPLAYS_COORDS = TagKey.of(Registry.ITEM_KEY, new Identifier("explorerssuite", "displays_coords"));
	public static final TagKey<Item> DISPLAYS_TIME = TagKey.of(Registry.ITEM_KEY, new Identifier("explorerssuite", "displays_time"));
	public static final TagKey<Item> CAMPFIRE_LOGS = TagKey.of(Registry.ITEM_KEY, new Identifier("explorerssuite", "campfire_logs"));
	public static final TagKey<Item> LADDERS = TagKey.of(Registry.ITEM_KEY, new Identifier("explorerssuite", "ladders"));
	public static final TagKey<Item> CANCEL_PLACEMENT = TagKey.of(Registry.ITEM_KEY, new Identifier("explorerssuite", "cancel_placement"));

	public static final Item FIRE_STARTER = new FireStarterItem(new FabricItemSettings().maxDamage(10));
	public static final Item TALLOW = new Item(new FabricItemSettings());

	//public static final Item SWEET_BERRY_PIPS = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	//public static final Item GLOW_BERRY_PIPS = new Item(new FabricItemSettings().group(ItemGroup.MISC));

	public static BlockEntityType<EffectCandleBlockEntity> EFFECT_CANDLE_ENTITY;

	public static final StatusEffect CALMING = new CalmingStatusEffect();
	public static final StatusEffect INSOMNIA = new InsomniaStatusEffect();

	public static final Block INCENSED_CANDLE = new EffectCandleBlock(FabricBlockSettings.copyOf(Blocks.BLUE_CANDLE), ExplorersSuite.CALMING);
	public static final Block INFERNAL_CANDLE = new EffectCandleBlock(FabricBlockSettings.copyOf(Blocks.RED_CANDLE), ExplorersSuite.INSOMNIA);

	//public static final Block SWEET_BERRY_PIPS = new TransformBlock(FabricBlockSettings.of(Material.PLANT).noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH), Blocks.SWEET_BERRY_BUSH);
	//public static final Block GLOW_BERRY_PIPS = new TransformBlock(FabricBlockSettings.of(Material.PLANT).noCollision().sounds(BlockSoundGroup.CAVE_VINES), Blocks.CAVE_VINES);

	public static final Potion CALMING_POTION = new Potion(new StatusEffectInstance(CALMING, 3600));
	public static final Potion EXTENDED_CALMING_POTION = new Potion(new StatusEffectInstance(CALMING, 12000));
	public static final Potion ENHANCED_CALMING_POTION = new Potion(new StatusEffectInstance(CALMING, 1800, 1));

	public static final Potion INSOMNIA_POTION = new Potion(new StatusEffectInstance(INSOMNIA, 3600));
	public static final Potion EXTENDED_INSOMNIA_POTION = new Potion(new StatusEffectInstance(INSOMNIA, 12000));
	public static final Potion ENHANCED_INSOMNIA_POTION = new Potion(new StatusEffectInstance(INSOMNIA, 1800, 1));

	public static ExplorersSuiteConfig CONFIG;

	Item sweetBerries = new ItemStack(Items.SWEET_BERRIES).getItem();
	Item glowBerries = new ItemStack(Items.GLOW_BERRIES).getItem();

	public static final ItemGroup EXPLORERS_SUITE_TAB = FabricItemGroupBuilder.create(
					new Identifier("explorerssuite", "general"))
			.icon(() -> new ItemStack(Items.SWEET_BERRIES))
			.appendItems(stacks -> {
				stacks.add(new ItemStack(Items.SWEET_BERRIES));
				stacks.add(new ItemStack(Blocks.SWEET_BERRY_BUSH));
				stacks.add(new ItemStack(Items.GLOW_BERRIES));
				stacks.add(new ItemStack(Blocks.CAVE_VINES));
				stacks.add(new ItemStack(INCENSED_CANDLE));
				stacks.add(new ItemStack(INFERNAL_CANDLE));
				stacks.add(new ItemStack(FIRE_STARTER));
				stacks.add(new ItemStack(TALLOW));
			})
			.build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Fabric world!");
		CONFIG = AutoConfig.register(ExplorersSuiteConfig.class, GsonConfigSerializer::new).getConfig();
		Registry.register(Registry.ITEM, new Identifier("explorerssuite", "fire_starter"), FIRE_STARTER);
		Registry.register(Registry.ITEM, new Identifier("explorerssuite", "tallow"), TALLOW);

		Registry.register(Registry.STATUS_EFFECT, new Identifier("explorerssuite", "calming"), CALMING);
		Registry.register(Registry.STATUS_EFFECT, new Identifier("explorerssuite", "insomnia"), INSOMNIA);

		Registry.register(Registry.BLOCK, new Identifier("explorerssuite", "incensed_candle"), INCENSED_CANDLE);
		Registry.register(Registry.BLOCK, new Identifier("explorerssuite", "infernal_candle"), INFERNAL_CANDLE);

		Registry.register(Registry.ITEM, new Identifier("explorerssuite", "incensed_candle"), new BlockItem(INCENSED_CANDLE, new FabricItemSettings().group(EXPLORERS_SUITE_TAB)));
		Registry.register(Registry.ITEM, new Identifier("explorerssuite", "infernal_candle"), new BlockItem(INFERNAL_CANDLE, new FabricItemSettings().group(EXPLORERS_SUITE_TAB)));

		Registry.register(Registry.ITEM, new Identifier("explorerssuite", "sweet_berry_pips"), new AliasedBlockItem(Blocks.SWEET_BERRY_BUSH, new FabricItemSettings().group(EXPLORERS_SUITE_TAB)));
		Registry.register(Registry.ITEM, new Identifier("explorerssuite", "glow_berry_pips"), new AliasedBlockItem(Blocks.CAVE_VINES, new FabricItemSettings().group(EXPLORERS_SUITE_TAB)));

		((ItemAccessor)Items.SWEET_BERRIES).setGroup(null);
		((ItemAccessor)Items.GLOW_BERRIES).setGroup(null);
		//((ItemAccessor)sweetBerries).setGroup(ItemGroup.FOOD);
		//((ItemAccessor)glowBerries).setGroup(ItemGroup.FOOD);

		EFFECT_CANDLE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("explorerssuite", "incensed_candle"), FabricBlockEntityTypeBuilder.create(EffectCandleBlockEntity::new, INCENSED_CANDLE, INFERNAL_CANDLE).build(null));

		Registry.register(Registry.POTION, new Identifier("explorerssuite", "calming"), CALMING_POTION);
		Registry.register(Registry.POTION, new Identifier("explorerssuite", "extended_calming"), EXTENDED_CALMING_POTION);
		Registry.register(Registry.POTION, new Identifier("explorerssuite", "enhanced_calming"), ENHANCED_CALMING_POTION);
		Registry.register(Registry.POTION, new Identifier("explorerssuite", "insomnia"), INSOMNIA_POTION);
		Registry.register(Registry.POTION, new Identifier("explorerssuite", "extended_insomnia"), EXTENDED_INSOMNIA_POTION);
		Registry.register(Registry.POTION, new Identifier("explorerssuite", "enhanced_insomnia"), ENHANCED_INSOMNIA_POTION);

		BrewingStandRegistryMixin.registerPotionRecipe(Potions.MUNDANE, Items.GHAST_TEAR, CALMING_POTION);
		BrewingStandRegistryMixin.registerPotionRecipe(CALMING_POTION, Items.REDSTONE, EXTENDED_CALMING_POTION);
		BrewingStandRegistryMixin.registerPotionRecipe(CALMING_POTION, Items.GLOWSTONE_DUST, ENHANCED_CALMING_POTION);
		BrewingStandRegistryMixin.registerPotionRecipe(CALMING_POTION, Items.FERMENTED_SPIDER_EYE, INSOMNIA_POTION);
		BrewingStandRegistryMixin.registerPotionRecipe(INSOMNIA_POTION, Items.REDSTONE, EXTENDED_INSOMNIA_POTION);
		BrewingStandRegistryMixin.registerPotionRecipe(INSOMNIA_POTION, Items.GLOWSTONE_DUST, ENHANCED_INSOMNIA_POTION);

		if (CONFIG.pigsDropTallow)
			EventListeners.injectLoot();
		EventListeners.injectLadderBehavior();
		//EventListeners.injectBerriesBehavior();
		//EventListeners.injectBarkBehavior();
	}
}
