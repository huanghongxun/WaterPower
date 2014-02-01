package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.Materials;
import gregtechmod.api.enums.OrePrefixes;
import gregtechmod.api.enums.SubTag;
import gregtechmod.api.interfaces.IIconContainer;
import gregtechmod.api.interfaces.IOnItemClick;
import gregtechmod.api.util.GT_Config;
import gregtechmod.api.util.GT_LanguageManager;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;

import java.util.*;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Gregorius Techneticies
 * 
 * One Item for everything!
 * 
 * This brilliant Item Class is used for automatically generating all possible variations of Material Items, like Dusts, Ingots, Gems, Plates and similar.
 * It saves me a ton of work, when adding Items, because I always have to make a new Item Subtype for each OreDict Prefix, when adding a new Material.
 * 
 *  As you can see, up to 32766 Items can be generated using this Class. And the last 766 Items can be customly defined, just to save space and MetaData.
 */
public abstract class GT_MetaGenerated_Item extends GT_Generic_Item {
	/**
	 * All instances of this Item Class are listed here.
	 * This gets used to register the Renderer to all Items of this Type, if useStandardMetaItemRenderer() returns true.
	 * 
	 * You can also use the unlocalized Name gotten from getUnlocalizedName() as Key if you want to get a specific Item.
	 */
	public static final HashMap<String, GT_MetaGenerated_Item> sInstances = new HashMap<String, GT_MetaGenerated_Item>();
	
	/* ---------- CONSTRUCTOR AND MEMBER VARIABLES ---------- */
	
	private BitSet mEnabledItems = new BitSet(766);
	private Icon[] mIconList = new Icon[mEnabledItems.size()];
	private final OrePrefixes[] mGeneratedPrefixList;
	
	public final HashMap<Short, ArrayList<IOnItemClick>> mClickBehaviors = new HashMap<Short, ArrayList<IOnItemClick>>();
	
	/**
	 * Creates the Item using these Parameters.
	 * @param aID the Item ID.
	 * @param aUnlocalized The Unlocalized Name of this Item.
	 * @param aGeneratedPrefixList The OreDict Prefixes you want to have generated.
	 */
	public GT_MetaGenerated_Item(int aID, String aUnlocalized, OrePrefixes... aGeneratedPrefixList) {
		super(aID, aUnlocalized, "Generated Item", null, false);
		mGeneratedPrefixList = Arrays.copyOf(aGeneratedPrefixList, 32);
		setCreativeTab(GregTech_API.TAB_GREGTECH_MATERIALS);
        setHasSubtypes(true);
        setMaxDamage(0);
        
        sInstances.put(getUnlocalizedName(), this);
        
        for (int i = 0; i < 32000; i++) {
			OrePrefixes tPrefix = mGeneratedPrefixList[i / 1000];
			if (tPrefix == null) continue;
			Materials tMaterial = GregTech_API.sGeneratedMaterials[i % 1000];
			if (tMaterial == null) continue;
			if (doesMaterialAllowGeneration(tPrefix, tMaterial)) {
				GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + i + ".name", getDefaultLocalization(tPrefix, tMaterial, i));
				GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + i + ".tooltip", tMaterial.getToolTip(tPrefix.mMaterialAmount / GregTech_API.MATERIAL_UNIT));
				String tOreName = getOreDictString(tPrefix, tMaterial);
				tPrefix = OrePrefixes.getOrePrefix(tOreName);
//				if (tPrefix != null && tPrefix.mIsUnificatable) {
//					GT_OreDictUnificator.set(tOreName, new ItemStack(this, 1, i));
//				} else {
					GT_OreDictUnificator.registerOre(tOreName, new ItemStack(this, 1, i));
//				}
			}
		}
	}
	
	/* ---------- OVERRIDEABLE FUNCTIONS ---------- */
	
	/**
	 * @param aPrefix the OreDict Prefix
	 * @param aMaterial the Material
	 * @param aMetaData a Index from [0 - 31999]
	 * @return the Localized Name when default LangFiles are used.
	 */
	public String getDefaultLocalization(OrePrefixes aPrefix, Materials aMaterial, int aMetaData) {
		return aPrefix.mLocalizedMaterialPre + aMaterial.mDefaultLocalName + aPrefix.mLocalizedMaterialPost;
	}
	
	/**
	 * @param aMetaData a Index from [0 - 31999]
	 * @param aMaterial the Material
	 * @return an Icon Container for the Item Display.
	 */
	public abstract IIconContainer getIconContainer(int aMetaData, Materials aMaterial);
	
	/**
	 * @param aPrefix this can be null, you have to return false in that case
	 * @param aMaterial this can be null, you have to return false in that case
	 * @return if this Item should be generated and visible.
	 */
	public boolean doesMaterialAllowGeneration(OrePrefixes aPrefix, Materials aMaterial) {
		// You have to check for at least these Conditions in every Case! So add a super Call like the following for this before executing your Code:
		// if (!super.doesMaterialAllowGeneration(aPrefix, aMaterial)) return false;
		return aPrefix != null && aMaterial != null && !aPrefix.dontGenerateItem(aMaterial);
	}
	
	/**
	 * @param aPrefix always != null
	 * @param aMaterial always != null
	 * @param aDoShowAllItems this is the Configuration Setting of the User, if he wants to see all the Stuff like Tiny Dusts or Crushed Ores aswell.
	 * @return if this Item should be visible in NEI or Creative
	 */
	public boolean doesShowInCreative(OrePrefixes aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
		return true;
	}
	
	/**
	 * @return the name of the Item, to be registered at the OreDict.
	 */
	public String getOreDictString(OrePrefixes aPrefix, Materials aMaterial) {
		return aPrefix.get(aMaterial);
	}
	
	/**
	 * @return the Color Modulation the Material is going to be rendered with.
	 */
	public short[] getRGBa(ItemStack aStack) {
		int aMetaData = aStack.getItemDamage();
		if (aMetaData < 0) return Materials._NULL.mRGBa;
		if (aMetaData < 32000) {
			Materials tMaterial = GregTech_API.sGeneratedMaterials[aMetaData % 1000];
			if (tMaterial == null) return Materials._NULL.mRGBa;
			return tMaterial.mRGBa;
		}
        return Materials._NULL.mRGBa;
	}
	
	/**
	 * @return if this MetaGenerated Item should my Default Renderer System.
	 */
	public boolean useStandardMetaItemRenderer() {
		return true;
	}
	
	/* ---------- FOR ADDING CUSTOM ITEMS INTO THE REMAINING 766 RANGE ---------- */
	
	/**
	 * This adds a Custom Item to the ending Range.
	 * @param aID The Id of the assigned Item [0 - 765] (The MetaData gets auto-shifted by +30000)
	 * @param aEnglish The Default Localized Name of the created Item
	 * @param aToolTip The Default ToolTip of the created Item, you can also insert null for having no ToolTip
	 * @param aOreDictNames The OreDict Names you want to give the Item.
	 * @return An ItemStack containing the newly created Item.
	 */
	public final ItemStack addItem(int aID, String aEnglish, String aToolTip, Object... aOreDictNames) {
		if (aToolTip == null) aToolTip = "";
		if (aID >= 0 && aID < mEnabledItems.size()) {
			mEnabledItems.set(aID);
			GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + (32000+aID) + ".name", aEnglish);
			GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + (32000+aID) + ".tooltip", aToolTip);
			ItemStack rStack = new ItemStack(this, 1, 32000+aID);
			for (Object tOreDictName : aOreDictNames) GT_OreDictUnificator.registerOre(tOreDictName, rStack);
			return rStack;
		}
		return null;
	}
	
	/**
	 * Adds a special Clicking Behavior to the Item.
	 * 
	 * Note: the boolean Behaviors sometimes won't be executed if another boolean Behavior returned true before.
	 * 
	 * @param aMetaValue the Meta Value of the Item you want to add it to. [0 - 32765]
	 * @param aBehavior the Click Behavior you want to add.
	 * @return the Item itself for convenience in constructing
	 */
	public final GT_MetaGenerated_Item addClickBehavior(int aMetaValue, IOnItemClick aBehavior) {
		if (aMetaValue < 0 || aMetaValue >= 30000 + mEnabledItems.size()) return this;
		ArrayList<IOnItemClick> tList = mClickBehaviors.get(aMetaValue);
		if (tList == null) {
			tList = new ArrayList<IOnItemClick>(1);
			mClickBehaviors.put((short)aMetaValue, tList);
		}
		tList.add(aBehavior);
		return this;
	}
	
	/* ---------- INTERNAL OVERRIDES ---------- */
	
	@Override
	public final boolean onItemUse(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
		ArrayList<IOnItemClick> tList = mClickBehaviors.get(aStack.getItemDamage());
		if (tList != null) for (IOnItemClick tBehavior : tList) if (tBehavior.onItemUse(this, aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ)) return true;
		return false;
	}
	
	@Override
	public final boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
		ArrayList<IOnItemClick> tList = mClickBehaviors.get(aStack.getItemDamage());
		if (tList != null) for (IOnItemClick tBehavior : tList) if (tBehavior.onItemUseFirst(this, aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ)) return true;
		return false;
	}
	
    @Override
	public final ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
    	ArrayList<IOnItemClick> tList = mClickBehaviors.get(aStack.getItemDamage());
		if (tList != null) for (IOnItemClick tBehavior : tList) aStack = tBehavior.onItemRightClick(this, aStack, aWorld, aPlayer);
		return aStack;
    }
    
	public final IIconContainer getIconContainer(int aMetaData) {
		if (aMetaData < 0) return null;
		if (aMetaData < 32000) {
			Materials tMaterial = GregTech_API.sGeneratedMaterials[aMetaData % 1000];
			if (tMaterial == null) return null;
			return getIconContainer(aMetaData, tMaterial);
		}
		return null;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public final void getSubItems(int var1, CreativeTabs aCreativeTab, List aList) {
        for (int i = 0; i < 32000; i++) if (doesMaterialAllowGeneration(mGeneratedPrefixList[i / 1000], GregTech_API.sGeneratedMaterials[i % 1000]) && doesShowInCreative(mGeneratedPrefixList[i / 1000], GregTech_API.sGeneratedMaterials[i % 1000], GregTech_API.sDoShowAllItemsInCreative)) aList.add(new ItemStack(this, 1, i));
        for (int i = 0, j = mEnabledItems.size(); i < j; i++) if (mEnabledItems.get(i)) aList.add(new ItemStack(this, 1, i));
    }
	
	@Override
    public final String getUnlocalizedName(ItemStack aStack) {
    	return getUnlocalizedName() + "." + aStack.getItemDamage();
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public final void registerIcons(IconRegister aIconRegister) {
		for (int i = 0, j = mEnabledItems.size(); i < j; i++) if (mEnabledItems.get(i)) {
    		mIconList[i] = aIconRegister.registerIcon(GregTech_API.TEXTURE_PATH_ITEM + (GT_Config.system?"troll":getUnlocalizedName() + "/" + i));
    	}
    }
	
	@Override
    public final Icon getIconFromDamage(int aMetaData) {
		if (aMetaData < 0) return null;
		if (aMetaData < 32000) {
			Materials tMaterial = GregTech_API.sGeneratedMaterials[aMetaData % 1000];
			if (tMaterial == null) return null;
			IIconContainer tIcon = getIconContainer(aMetaData, tMaterial);
			if (tIcon != null) return tIcon.getIcon();
			return null;
		}
		return aMetaData-32000<mIconList.length?mIconList[aMetaData-32000]:null;
    }
	
	@Override
	@SuppressWarnings("deprecation")
    public final boolean hasEffect(ItemStack aStack) {
		if (super.hasEffect(aStack)) return true;
		int aMetaData = aStack.getItemDamage();
		if (aMetaData < 0) return false;
		if (aMetaData < 32000) {
			Materials tMaterial = GregTech_API.sGeneratedMaterials[aMetaData % 1000];
			if (tMaterial == null) return false;
			return tMaterial.isRadioactive() || tMaterial.contains(SubTag.ENCHANTMENT_GLOW);
		}
        return false;
    }
	
	@Override
    public final boolean hasEffect(ItemStack aStack, int aPass) {
		if (super.hasEffect(aStack, aPass)) return true;
		int aMetaData = aStack.getItemDamage();
		if (aMetaData < 0) return false;
		if (aMetaData < 32000) {
			Materials tMaterial = GregTech_API.sGeneratedMaterials[aMetaData % 1000];
			if (tMaterial == null) return false;
			return tMaterial.isRadioactive() || tMaterial.contains(SubTag.ENCHANTMENT_GLOW);
		}
        return false;
    }
    
	@Override
    public final void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
		String tKey = getUnlocalizedName() + "." + aStack.getItemDamage() + ".tooltip", tString = GT_LanguageManager.getTranslation(tKey);
		if (GT_Utility.isStringValid(tString) && !tKey.equals(tString)) aList.add(tString);
	}
}