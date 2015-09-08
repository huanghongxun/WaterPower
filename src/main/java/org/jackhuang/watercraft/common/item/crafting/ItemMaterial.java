package org.jackhuang.watercraft.common.item.crafting;

import java.util.List;

import gregtech.api.GregTech_API;

import org.jackhuang.watercraft.client.render.IIconContainer;
import org.jackhuang.watercraft.client.render.RecolorableTextures;
import org.jackhuang.watercraft.common.item.ItemRecolorable;
import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.common.recipe.IRecipeRegistrar;
import org.jackhuang.watercraft.common.recipe.MyRecipeInputOreDictionary;
import org.jackhuang.watercraft.common.recipe.RecipeAdder;
import org.jackhuang.watercraft.integration.EnderIOModule;
import org.jackhuang.watercraft.integration.MekanismModule;
import org.jackhuang.watercraft.integration.RailcraftModule;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.registry.GameRegistry;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import static org.jackhuang.watercraft.common.item.crafting.MaterialTypes.*;
import static org.jackhuang.watercraft.common.item.crafting.MaterialForms.*;
import static org.jackhuang.watercraft.common.recipe.IRecipeRegistrar.addShapelessRecipeByOreDictionary;
import static cpw.mods.fml.common.registry.GameRegistry.addShapedRecipe;

public class ItemMaterial extends ItemRecolorable {

    public static ItemMaterial instance;

    public ItemMaterial() {
        super("cptItemMeterial");
        setHasSubtypes(true);

        instance = this;

        registerAllRecipes();
        registerOreDict();
    }

    @Override
    public String getTextureFolder() {
        return "material";
    }

    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack) {
        int meta = par1ItemStack.getItemDamage();
        int craftingType = meta / space;
        int levelType = meta % space;
        return MaterialTypes.values()[craftingType].getShowedName() + " "
                + MaterialForms.values()[levelType].getShowedName();
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        try {
            int meta = itemstack.getItemDamage();
            int meterialType = meta / space;
            int meterialForm = meta % space;
            return "item." + "watermill."
                    + MaterialTypes.values()[meterialType].name() + "."
                    + MaterialForms.values()[meterialForm].name();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean stopScanning(ItemStack stack) {
        return false;
    }

    public static ItemStack get(MaterialTypes craftingTypes,
            MaterialForms levelTypes) {
        return get(craftingTypes, levelTypes, 1);
    }

    public static ItemStack get(MaterialTypes craftingTypes,
            MaterialForms levelTypes, int mount) {
        return new ItemStack(instance, mount, craftingTypes.ind()
                + levelTypes.ordinal());
    }

    public void registerOreDict() {
        for (MaterialForms forms : MaterialForms.values()) {
            for (MaterialTypes types : MaterialTypes.values()) {
                IRecipeRegistrar.registerOreDict(forms.name() + types.name(),
                        get(types, forms));
            }
        }
    }

    public void registerAllRecipes() {
        if (Mods.ExNihilo.isAvailable) {

        }
        if (IRecipeRegistrar.gregtechRecipe) {
            GregTech_API.sRecipeAdder.addAlloySmelterRecipe(
                    get(IndustrialSteel, ingot), get(Neodymium, ingot),
                    get(NeodymiumMagnet, ingot, 2), 240 * 20, 128);
            GregTech_API.sRecipeAdder.addBlastRecipe(get(Vanadium, dust),
                    get(Steel, ingot, 2), get(VanadiumSteel, ingot, 3), null,
                    240 * 20, 512, 2000);
            GregTech_API.sRecipeAdder.addBlastRecipe(get(Manganese, dust),
                    get(Steel, ingot, 2), get(ManganeseSteel, ingot, 3), null,
                    240 * 20, 512, 2000);
            GregTech_API.sRecipeAdder.addBlastRecipe(get(Steel, dust),
                    get(Steel, ingot), get(IndustrialSteel, ingot, 2), null,
                    240 * 20, 512, 2000);
            addShapelessRecipeByOreDictionary(
                    get(ZincAlloy, MaterialForms.dust, 5),
                    "dustSmallMagnesium", "dustSmallAluminium",
                    "dustSmallTitanium", "dustSmallCopper", "dustZinc",
                    "dustZinc", "dustZinc", "dustZinc");
        } else {
            if (Mods.EnderIO.isAvailable) {
                EnderIOModule.alloySmelter("Zinc Alloy Dust",
                        get(VanadiumSteel, ingot, 3),
                        new MyRecipeInputOreDictionary("ingotVanadium"),
                        new MyRecipeInputOreDictionary("ingotSteel"),
                        new MyRecipeInputOreDictionary("ingotSteel"));
                EnderIOModule.alloySmelter("Neodymium Magnet Dust",
                        get(NeodymiumMagnet, ingot, 2),
                        new MyRecipeInputOreDictionary("ingotNeodymium"),
                        new MyRecipeInputOreDictionary("ingotMagnet"));
                EnderIOModule.alloySmelter("Vanadium Steel Dust",
                        get(VanadiumSteel, ingot, 3),
                        new MyRecipeInputOreDictionary("ingotVanadium"),
                        new MyRecipeInputOreDictionary("ingotSteel"),
                        new MyRecipeInputOreDictionary("ingotSteel"));
                EnderIOModule.alloySmelter("Manganese Steel Dust 3",
                        get(ManganeseSteel, ingot, 3),
                        new MyRecipeInputOreDictionary("ingotManganese"),
                        new MyRecipeInputOreDictionary("ingotSteel"),
                        new MyRecipeInputOreDictionary("ingotSteel"));
                EnderIOModule.alloySmelter("Manganese Steel Dust 4",
                        get(ManganeseSteel, ingot, 4),
                        new MyRecipeInputOreDictionary("ingotManganese"),
                        new MyRecipeInputOreDictionary("ingotSteel"),
                        new MyRecipeInputOreDictionary("ingotSteel"),
                        new MyRecipeInputOreDictionary("ingotCoal"));
            }
            addShapelessRecipeByOreDictionary(get(ZincAlloy, dust, 5),
                    "dustZinc", "dustZinc", "dustZinc", "dustZinc",
                    "dustCopper");
            addShapelessRecipeByOreDictionary(get(VanadiumSteel, dust, 3),
                    "dustVanadium", "dustSteel", "dustSteel");
            addShapelessRecipeByOreDictionary(get(NeodymiumMagnet, dust, 2),
                    "dustNeodymium", "dustMagnet");
            addShapelessRecipeByOreDictionary(get(ManganeseSteel, dust, 4),
                    "dustManganese", "dustSteel", "dustSteel", "dustCoal");
            addShapelessRecipeByOreDictionary(get(ManganeseSteel, dust, 3),
                    "dustManganese", "dustSteel", "dustSteel");
            List<ItemStack> steelIngots = OreDictionary.getOres("ingotSteel");
            boolean flag = false;
            for (ItemStack is : steelIngots)
                flag |= RecipeAdder.blastFurnace(is,
                        get(IndustrialSteel, ingot), 1000);
            if (!flag) {
                GameRegistry.addSmelting(new ItemStack(Items.iron_ingot),
                        get(Steel, ingot), 0);
            }
        }

        for (MaterialTypes types : MaterialTypes.values()) {
            addShapedRecipe(get(types, dust), // 4 small dusts -> 1 dust
                    "AA", "AA", 'A', get(types, dustSmall));

            addShapedRecipe(get(types, dustSmall, 4), // 1 dust -> 4 small dusts
                    "A", 'A', get(types, dust));

            addShapedRecipe(get(types, dust), // 9 tiny dusts -> 1 dust
                    "AAA", "AAA", "AAA", 'A', get(types, dustTiny));

            addShapedRecipe(get(types, dustTiny, 9), // 1 dust -> 9 tiny dust
                    "A", 'A', get(types, dust));

            addShapedRecipe(get(types, block), // 9 ingots -> 1 block
                    "AAA", "AAA", "AAA", 'A', get(types, ingot));
            RecipeAdder.compressor(get(types, ingot, 9), get(types, block));

            addShapedRecipe(get(types, ingot), // 9 nuggets -> 1 ingot
                    "AAA", "AAA", "AAA", 'A', get(types, nugget));
            RecipeAdder.compressor(get(types, nugget, 9), get(types, ingot));

            addShapedRecipe(get(types, nugget, 9), // 1 ingot -> 9 nuggets
                    "A", 'A', get(types, ingot));

            addShapedRecipe(get(types, stick, 4), // 2 ingots -> 2 stick
                    "A", "A", 'A', get(types, ingot));

            addShapedRecipe(
                    get(types, gear), // some sticks & plates -> 1 gear
                    "SPS", "P P", "SPS", 'S', get(types, stick), 'P',
                    get(types, plate));

            addShapedRecipe(get(types, ring), // 4 sticks -> 1 ring
                    " S ", "S S", " S ", 'S', get(types, stick));
            
            addShapelessRecipeByOreDictionary(get(types, ingot, 9), get(types, block));

            ItemStack d = get(types, dust);
            GameRegistry.addSmelting(d, get(types, ingot), 0);
            d = get(types, dustTiny);
            GameRegistry.addSmelting(d, get(types, nugget), 0);
            
            
            GameRegistry.addSmelting(get(types, block), get(types, ingot, 9), 0);

            RecipeAdder.bender(get(types, ingot), get(types, plate));
            RecipeAdder.bender(get(types, plate, 9), get(types, plateDense));
            RecipeAdder.macerator(get(types, ingot), get(types, dust));
            RecipeAdder.macerator(get(types, plate), get(types, dust));
            RecipeAdder.macerator(get(types, block), get(types, dust, 9));
            RecipeAdder.macerator(get(types, plateDense), get(types, dust, 9));
            RecipeAdder.macerator(get(types, screw), get(types, dustSmall));
            RecipeAdder.cutter(get(types, block), get(types, plate, 9));
            RecipeAdder.lathe(get(types, stick), get(types, screw, 4));

            addShapelessRecipeByOreDictionary(get(types, plateDense),
                    ItemType.WoodenHammer.item(), get(types, block));
        }
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs,
            List par3List) {
        for (MaterialTypes c : MaterialTypes.values())
            for (MaterialForms l : MaterialForms.values())
                par3List.add(get(c, l));
    }

    public IIconContainer getIconContainer(int meta, MaterialTypes type) {
        return getIconContainers()[meta % space];
    }

    @Override
    public IIconContainer getIconContainer(int meta) {
        return getIconContainer(meta, MaterialTypes.values()[meta / space]);
    }

    @Override
    public short[] getRGBA(ItemStack stack) {
        int meta = stack.getItemDamage();
        int craftingType = meta / space;
        MaterialTypes type = MaterialTypes.values()[craftingType];
        return new short[] { type.R, type.G, type.B, type.A };
    }

    @Override
    public IIconContainer[] getIconContainers() {
        return RecolorableTextures.METAL;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
            World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {
        if (stack.stackSize > 0) {
            int meta = stack.getItemDamage();
            MaterialTypes t = MaterialTypes.values()[meta / space];
            MaterialForms f = MaterialForms.values()[meta % space];
            if (t == MaterialTypes.Magnet && f == MaterialForms.ingot) {
                player.inventory.addItemStackToInventory(new ItemStack(
                        Items.iron_ingot));
                stack.stackSize--;
            }
        }
        return false;
    }
}
