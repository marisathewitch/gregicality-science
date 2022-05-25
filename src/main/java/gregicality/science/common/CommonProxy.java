package gregicality.science.common;

import gregicality.science.GregicalityScience;
import gregicality.science.common.block.GCYSMetaBlocks;
import gregicality.science.common.pipelike.axle.BlockAxlePipe;
import gregicality.science.common.pipelike.axle.ItemBlockAxlePipe;
import gregicality.science.common.pipelike.axle.tile.TileEntityAxlePipe;
import gregicality.science.common.pipelike.pressure.BlockPressurePipe;
import gregicality.science.common.pipelike.pressure.ItemBlockPressurePipe;
import gregicality.science.common.pipelike.pressure.tile.TileEntityPressurePipe;
import gregicality.science.loaders.recipe.GCYSMaterialInfoLoader;
import gregicality.science.loaders.recipe.GCYSRecipeLoader;
import gregtech.common.blocks.VariantItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = GregicalityScience.MODID)
public class CommonProxy {

    public void preLoad() {
        GameRegistry.registerTileEntity(TileEntityPressurePipe.class, new ResourceLocation(GregicalityScience.MODID, "pressure_pipe"));
        GameRegistry.registerTileEntity(TileEntityAxlePipe.class, new ResourceLocation(GregicalityScience.MODID, "axle_pipe"));
    }

    @SubscribeEvent
    public static void syncConfigValues(@Nonnull ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(GregicalityScience.MODID)) {
            ConfigManager.sync(GregicalityScience.MODID, Config.Type.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void registerBlocks(@Nonnull RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.register(GCYSMetaBlocks.CRUCIBLE);
        registry.register(GCYSMetaBlocks.MULTIBLOCK_CASING);

        for (BlockPressurePipe pipe : GCYSMetaBlocks.PRESSURE_PIPES) registry.register(pipe);
        for (BlockAxlePipe pipe : GCYSMetaBlocks.AXLE_PIPES) registry.register(pipe);
    }

    @SubscribeEvent
    public static void registerItems(@Nonnull RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(createItemBlock(GCYSMetaBlocks.CRUCIBLE, VariantItemBlock::new));
        registry.register(createItemBlock(GCYSMetaBlocks.MULTIBLOCK_CASING, VariantItemBlock::new));

        for (BlockPressurePipe pipe : GCYSMetaBlocks.PRESSURE_PIPES) registry.register(createItemBlock(pipe, ItemBlockPressurePipe::new));
        for (BlockAxlePipe pipe : GCYSMetaBlocks.AXLE_PIPES) registry.register(createItemBlock(pipe, ItemBlockAxlePipe::new));
    }

    @Nonnull
    private static <T extends Block> ItemBlock createItemBlock(@Nonnull T block, @Nonnull Function<T, ItemBlock> producer) {
        ItemBlock itemBlock = producer.apply(block);
        itemBlock.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        return itemBlock;
    }

    @SubscribeEvent()
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        // Main recipe registration
        // This is called AFTER GregTech registers recipes, so
        // anything here is safe to call removals in
        GCYSRecipeLoader.init();
        GCYSMaterialInfoLoader.init();
    }
}
