package gregtechmod.api.util;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.GT_Items;
import gregtechmod.api.enums.Materials;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * This File contains the functions used for Recipes. Please do not include this File AT ALL in your Moddownload as it ruins compatibility
 * This is just the Core of my Recipe System, if you just want to GET the Recipes I add, then you can access this File.
 * Do NOT add Recipes using the Constructors inside this Class, The GregTech_API File calls the correct Functions for these Constructors.
 * 
 * I know this File causes some Errors, because of missing Main Functions, but if you just need to compile Stuff, then remove said erroreous Functions.
 */
public class GT_Recipe {
	public static volatile int VERSION = 407;
	
	/**
	 * If you want to remove Recipes, then set the Index to null, instead of removing the complete Entry!
	 * That's because I have a mapping for quick access, so you should also remove the Mapping of the Recipe.
	 * 
	 * However, every single one of these Recipes has a Config, so you could just disable the Config Setting.
	 */
	public static final ArrayList<GT_Recipe> pFusionRecipes				= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pCentrifugeRecipes			= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pElectrolyzerRecipes		= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pGrinderRecipes			= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pBlastRecipes				= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pImplosionRecipes			= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pSawmillRecipes			= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pVacuumRecipes				= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pChemicalRecipes			= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pDistillationRecipes		= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pWiremillRecipes			= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pBenderRecipes				= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pAlloySmelterRecipes		= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pAssemblerRecipes			= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pCannerRecipes				= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pCNCRecipes				= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pLatheRecipes				= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pCutterRecipes				= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pExtruderRecipes			= new ArrayList<GT_Recipe>();
	
	public static final ArrayList<GT_Recipe> pDieselFuels				= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pTurbineFuels				= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pHotFuels					= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pDenseLiquidFuels			= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pPlasmaFuels				= new ArrayList<GT_Recipe>();
	public static final ArrayList<GT_Recipe> pMagicFuels				= new ArrayList<GT_Recipe>();
	
	@Deprecated public static ArrayList<GT_Recipe> sDieselFuels			= new ArrayList<GT_Recipe>();
	@Deprecated public static ArrayList<GT_Recipe> sTurbineFuels		= new ArrayList<GT_Recipe>();
	@Deprecated public static ArrayList<GT_Recipe> sHotFuels			= new ArrayList<GT_Recipe>();
	@Deprecated public static ArrayList<GT_Recipe> sDenseLiquidFuels	= new ArrayList<GT_Recipe>();
	@Deprecated public static ArrayList<GT_Recipe> sPlasmaFuels			= new ArrayList<GT_Recipe>();
	@Deprecated public static ArrayList<GT_Recipe> sMagicFuels			= new ArrayList<GT_Recipe>();
	
	public static ArrayList<ArrayList<GT_Recipe>> mRecipeMaps = new ArrayList<ArrayList<GT_Recipe>>();
	
	static {
    	mRecipeMaps.add(pFusionRecipes);
    	mRecipeMaps.add(pCentrifugeRecipes);
    	mRecipeMaps.add(pElectrolyzerRecipes);
    	mRecipeMaps.add(pGrinderRecipes);
    	mRecipeMaps.add(pBlastRecipes);
    	mRecipeMaps.add(pImplosionRecipes);
    	mRecipeMaps.add(pSawmillRecipes);
    	mRecipeMaps.add(pVacuumRecipes);
    	mRecipeMaps.add(pChemicalRecipes);
    	mRecipeMaps.add(pDistillationRecipes);
    	mRecipeMaps.add(pWiremillRecipes);
    	mRecipeMaps.add(pBenderRecipes);
    	mRecipeMaps.add(pAlloySmelterRecipes);
    	mRecipeMaps.add(pAssemblerRecipes);
    	mRecipeMaps.add(pCannerRecipes);
    	mRecipeMaps.add(pCNCRecipes);
    	mRecipeMaps.add(pLatheRecipes);
    	mRecipeMaps.add(pCutterRecipes);
    	mRecipeMaps.add(pExtruderRecipes);
    	
    	mRecipeMaps.add(pDieselFuels);
    	mRecipeMaps.add(pTurbineFuels);
    	mRecipeMaps.add(pHotFuels);
    	mRecipeMaps.add(pDenseLiquidFuels);
    	mRecipeMaps.add(pPlasmaFuels);
    	mRecipeMaps.add(pMagicFuels);
	}
	
	public static void reinit() {
        GT_Log.out.println("GT_Mod: Re-Unificating Recipes.");
        for (ArrayList<GT_Recipe> tList : mRecipeMaps) {
        	for (GT_Recipe tRecipe : tList) {
            	GT_OreDictUnificator.setStackArray(true, tRecipe.mInputs);
            	GT_OreDictUnificator.setStackArray(true, tRecipe.mOutputs);
        	}
        }
	}
	
	public final ItemStack[] mInputs, mOutputs;
	public final int mDuration, mEUt, mStartEU;
	
	public ItemStack getRepresentativeInput1() {
		if (mInputs.length < 1) return null;
		return GT_Utility.copy(mInputs[0]);
	}
	
	public ItemStack getRepresentativeInput2() {
		if (mInputs.length < 2) return null;
		return GT_Utility.copy(mInputs[1]);
	}
	
	@SuppressWarnings("null") // And again too stupid to get that it cannot be null at that point.
	private GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt, int aStartEU) {
		aInput1  = GT_OreDictUnificator.get(true, aInput1);
		aInput2  = GT_OreDictUnificator.get(true, aInput2);
		aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
		aOutput2 = GT_OreDictUnificator.get(true, aOutput2);
		aOutput3 = GT_OreDictUnificator.get(true, aOutput3);
		aOutput4 = GT_OreDictUnificator.get(true, aOutput4);
		
		if (aInput1 != null && aInput1.getItemDamage() != GregTech_API.ITEM_WILDCARD_DAMAGE) {
			if (GT_Utility.areUnificationsEqual(aInput1, aOutput1)) {
				if (aInput1.stackSize >= aOutput1.stackSize) {
					aInput1.stackSize -= aOutput1.stackSize;
					aOutput1 = null;
				} else {
					aOutput1.stackSize -= aInput1.stackSize;
				}
			}
			if (GT_Utility.areUnificationsEqual(aInput1, aOutput2)) {
				if (aInput1.stackSize >= aOutput2.stackSize) {
					aInput1.stackSize -= aOutput2.stackSize;
					aOutput2 = null;
				} else {
					aOutput2.stackSize -= aInput1.stackSize;
				}
			}
			if (GT_Utility.areUnificationsEqual(aInput1, aOutput3)) {
				if (aInput1.stackSize >= aOutput3.stackSize) {
					aInput1.stackSize -= aOutput3.stackSize;
					aOutput3 = null;
				} else {
					aOutput3.stackSize -= aInput1.stackSize;
				}
			}
			if (GT_Utility.areUnificationsEqual(aInput1, aOutput4)) {
				if (aInput1.stackSize >= aOutput4.stackSize) {
					aInput1.stackSize -= aOutput4.stackSize;
					aOutput4 = null;
				} else {
					aOutput4.stackSize -= aInput1.stackSize;
				}
			}
		}
		
		if (aInput2 != null && aInput2.getItemDamage() != GregTech_API.ITEM_WILDCARD_DAMAGE) {
			if (GT_Utility.areUnificationsEqual(aInput2, aOutput1)) {
				if (aInput2.stackSize >= aOutput1.stackSize) {
					aInput2.stackSize -= aOutput1.stackSize;
					aOutput1 = null;
				} else {
					aOutput1.stackSize -= aInput2.stackSize;
				}
			}
			if (GT_Utility.areUnificationsEqual(aInput2, aOutput2)) {
				if (aInput2.stackSize >= aOutput2.stackSize) {
					aInput2.stackSize -= aOutput2.stackSize;
					aOutput2 = null;
				} else {
					aOutput2.stackSize -= aInput2.stackSize;
				}
			}
			if (GT_Utility.areUnificationsEqual(aInput2, aOutput3)) {
				if (aInput2.stackSize >= aOutput3.stackSize) {
					aInput2.stackSize -= aOutput3.stackSize;
					aOutput3 = null;
				} else {
					aOutput3.stackSize -= aInput2.stackSize;
				}
			}
			if (GT_Utility.areUnificationsEqual(aInput2, aOutput4)) {
				if (aInput2.stackSize >= aOutput4.stackSize) {
					aInput2.stackSize -= aOutput4.stackSize;
					aOutput4 = null;
				} else {
					aOutput4.stackSize -= aInput2.stackSize;
				}
			}
		}
		
		for (byte i = 64; i > 1; i--) if (aDuration / i > 0) {
			if (aInput1  == null || aInput1 .stackSize % i == 0)
			if (aInput2  == null || aInput2 .stackSize % i == 0)
			if (aOutput1 == null || aOutput1.stackSize % i == 0)
			if (aOutput2 == null || aOutput2.stackSize % i == 0)
			if (aOutput3 == null || aOutput3.stackSize % i == 0)
			if (aOutput4 == null || aOutput4.stackSize % i == 0) {
				if (aInput1  != null) aInput1 .stackSize /= i;
				if (aInput2  != null) aInput2 .stackSize /= i;
				if (aOutput1 != null) aOutput1.stackSize /= i;
				if (aOutput2 != null) aOutput2.stackSize /= i;
				if (aOutput3 != null) aOutput3.stackSize /= i;
				if (aOutput4 != null) aOutput4.stackSize /= i;
				aDuration /= i;
			}
		}
		
		if (aInput1 == null) mInputs = new ItemStack [] {}; else if (aInput2 == null) mInputs = new ItemStack[] {aInput1}; else mInputs = new ItemStack[] {aInput1, aInput2};
		mOutputs = new ItemStack[] {aOutput1, aOutput2, aOutput3, aOutput4};
		mDuration = aDuration;
		mStartEU = aStartEU;
		mEUt = aEUt;
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, int aStartEU, int aType) {
		this(aInput1, aOutput1, null, null, null, aStartEU, aType);
	}
	
	// aStartEU = EU per Liter! If there is no Liquid for this Object, then it gets multiplied with 1000!
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aStartEU, int aType) {
		this(aInput1, null, aOutput1, aOutput2, aOutput3, aOutput4, 0, 0, Math.max(1, aStartEU));
		
		if (mInputs.length > 0 && aStartEU > 0) {
			switch (aType) {
			// Diesel Generator
			case 0:
				pDieselFuels.add(this);
				sDieselFuels.add(this);
				break;
			// Gas Turbine
			case 1:
				pTurbineFuels.add(this);
				sTurbineFuels.add(this);
				break;
			// Thermal Generator
			case 2:
				pHotFuels.add(this);
				sHotFuels.add(this);
				break;
			// Plasma Generator
			case 4:
				pPlasmaFuels.add(this);
				sPlasmaFuels.add(this);
				break;
			// Magic Generator
			case 5:
				pMagicFuels.add(this);
				sMagicFuels.add(this);
				break;
			// Fluid Generator. Usually 3. Every wrong Type ends up in the Semifluid Generator
			default:
				pDenseLiquidFuels.add(this);
				sDenseLiquidFuels.add(this);
				break;
			}
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt, int aStartEU) {
		this(aInput1, aInput2, aOutput1, null, null, null, Math.max(aDuration, 1), aEUt, Math.max(Math.min(aStartEU, 160000000), 0));
		if (mInputs.length > 1 && findEqualFusionRecipe(mInputs) == null) {
			pFusionRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration) {
		this(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, Math.max(aDuration, 1), 5, 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualCentrifugeRecipe(mInputs) == null) {
			pCentrifugeRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
		this(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualElectrolyzerRecipe(mInputs) == null) {
			pElectrolyzerRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
		this(aInput1, null, aOutput1, aOutput2, null, null, aDuration, aEUt, 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualLatheRecipe(mInputs[0]) == null) {
			pLatheRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aDuration, ItemStack aOutput1, int aEUt) {
		this(aInput1, null, aOutput1, null, null, null, aDuration, aEUt, 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualCutterRecipe(mInputs[0]) == null) {
			pCutterRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3) {
		this(aInput1, aInput2, aOutput1, aOutput2, aOutput3, null, 200*aInput1.stackSize, 30, 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualSawmillRecipe(mInputs) == null) {
			pSawmillRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4) {
		this(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, 100*aInput1.stackSize, 120, 0);
		checkCellBalance();
		if (mInputs.length > 0 && aInput2 != null && mOutputs[0] != null && findEqualGrinderRecipe(mInputs) == null) {
			pGrinderRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aCellAmount, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
		this(aInput1, aCellAmount>0?GT_Items.Cell_Empty.get(Math.min(64, Math.max(1, aCellAmount))):null, aOutput1, aOutput2, aOutput3, aOutput4, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualDistillationRecipe(mInputs) == null) {
			pDistillationRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
		this(aInput1, aInput2, aOutput1, aOutput2, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), aLevel > 0 ? aLevel : 100);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualBlastRecipe(mInputs) == null) {
			pBlastRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2) {
		this(aInput1, GT_ModHandler.getIC2Item("industrialTnt", aInput2>0?aInput2<64?aInput2:64:1, new ItemStack(Block.tnt, aInput2>0?aInput2<64?aInput2:64:1)), aOutput1, aOutput2, null, null, 20, 30, 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualImplosionRecipe(mInputs) == null) {
			pImplosionRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aEUt, int aDuration, ItemStack aOutput1) {
		this(aInput1, null, aOutput1, null, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualWiremillRecipe(mInputs[0]) == null) {
			pWiremillRecipes.add(this);
		}
	}
	
	public GT_Recipe(int aEUt, int aDuration, ItemStack aInput1, ItemStack aOutput1) {
		this(aInput1, GT_Items.Circuit_Integrated.getWithDamage(0, aInput1.stackSize), aOutput1, null, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualBenderRecipe(mInputs) == null) {
			pBenderRecipes.add(this);
		}
	}
	
	public GT_Recipe(int aEUt, int aDuration, ItemStack aInput1, ItemStack aShape, ItemStack aOutput1) {
		this(aInput1, aShape, aOutput1, null, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInputs.length > 1 && mOutputs[0] != null && findEqualExtruderRecipe(mInputs) == null) {
			pExtruderRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aEUt, ItemStack aInput2, int aDuration, ItemStack aOutput1) {
		this(aInput1, aInput2, aOutput1, null, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualAssemblerRecipe(mInputs) == null) {
			pAssemblerRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, int aEUt, int aDuration, ItemStack aOutput1) {
		this(aInput1, aInput2, aOutput1, null, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualAlloySmelterRecipe(mInputs) == null) {
			pAlloySmelterRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aEUt, ItemStack aInput2, int aDuration, ItemStack aOutput1, ItemStack aOutput2) {
		this(aInput1, aInput2, aOutput1, aOutput2, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualCannerRecipe(mInputs) == null) {
			pCannerRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, int aDuration) {
		this(aInput1, null, aOutput1, null, null, null, Math.max(aDuration, 1), 120, 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualVacuumRecipe(mInputs[0]) == null) {
			pVacuumRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration) {
		this(aInput1, aInput2, aOutput1, null, null, null, Math.max(aDuration, 1), 30, 0);
		checkCellBalance();
		if (mInputs.length > 0 && mOutputs[0] != null && findEqualChemicalRecipe(mInputs) == null) {
			pChemicalRecipes.add(this);
		}
	}
	
	public static GT_Recipe findEqualRecipe(boolean aShapeless, List<GT_Recipe> aList, ItemStack... aInputs) {
		for (ItemStack tStack : aInputs) if (tStack != null) {
			for (GT_Recipe tRecipe : aList) if (isRecipeInputEqual(aShapeless, false, tRecipe, aInputs)) return tRecipe;
			return null;
		}
		return null;
	}
	
	public void checkCellBalance() {
		try {
			if (mInputs.length < 1) return;
			int tInputAmount  = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mInputs);
			int tOutputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutputs);
			
			if (tInputAmount < tOutputAmount) {
				if (!Materials.Tin.contains(mInputs)) {
					GT_Log.err.println("You get more Cells, than you put in? There must be something wrong.");
					if (GregTech_API.DEBUG_MODE) new Exception().printStackTrace(GT_Log.err);
				}
			} else if (tInputAmount > tOutputAmount) {
				if (!Materials.Tin.contains(mOutputs)) {
					GT_Log.err.println("You get less Cells, than you put in? GT Machines usually don't destroy Cells.");
					if (GregTech_API.DEBUG_MODE) new Exception().printStackTrace(GT_Log.err);
				}
			}
		} catch(Throwable e) {
			e.printStackTrace(GT_Log.err);
		}
	}
	
	public boolean isRecipeInputEqual(boolean aShapeless, boolean aDecreaseStacksizeBySuccess, ItemStack... aInputs) {
		if (aInputs.length < 1 || mInputs.length < 1) return false;
		if (aShapeless && aInputs.length > 1 && aInputs[1] != null && isRecipeInputEqual(false, aDecreaseStacksizeBySuccess, aInputs[1], aInputs[0])) return true;
		
		if (aInputs[0] != null && (GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aInputs[0]), mInputs[0], true) && aInputs[0].stackSize >= mInputs[0].stackSize))
		if (mInputs.length < 2 || (aInputs.length > 1 && GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aInputs[1]), mInputs[1], true) && aInputs[1].stackSize >= mInputs[1].stackSize)) {
			if (aDecreaseStacksizeBySuccess) {
				aInputs[0].stackSize -= mInputs[0].stackSize;
				if (mInputs.length > 1) aInputs[1].stackSize -= mInputs[1].stackSize;
			}
			return true;
		}
		return false;
	}
	
	public static boolean isRecipeInputEqual(boolean aShapeless, boolean aDecreaseStacksizeBySuccess, GT_Recipe aRecipe, ItemStack... aInputs) {
		if (aRecipe == null) return false;
		return aRecipe.isRecipeInputEqual(aShapeless, aDecreaseStacksizeBySuccess, aInputs);
	}
	
	public static GT_Recipe findEqualWiremillRecipe(ItemStack... aInputs) {
		return findEqualRecipe(false, pWiremillRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualBenderRecipe(ItemStack... aInputs) {
		return findEqualRecipe(false, pBenderRecipes, aInputs);
	}

	public static GT_Recipe findEqualAssemblerRecipe(ItemStack... aInputs) {
		return findEqualRecipe(true, pAssemblerRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualAlloySmelterRecipe(ItemStack... aInputs) {
		return findEqualRecipe(true, pAlloySmelterRecipes, aInputs);
	}

	public static GT_Recipe findEqualCannerRecipe(ItemStack... aInputs) {
		return findEqualRecipe(true, pCannerRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualDistillationRecipe(ItemStack... aInputs) {
		return findEqualRecipe(false, pDistillationRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualFusionRecipe(ItemStack... aInputs) {
		return findEqualRecipe(true, pFusionRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualCentrifugeRecipe(ItemStack... aInputs) {
		return findEqualRecipe(false, pCentrifugeRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualElectrolyzerRecipe(ItemStack... aInputs) {
		return findEqualRecipe(false, pElectrolyzerRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualSawmillRecipe(ItemStack... aInputs) {
		return findEqualRecipe(false, pSawmillRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualGrinderRecipe(ItemStack... aInputs) {
		return findEqualRecipe(false, pGrinderRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualBlastRecipe(ItemStack... aInputs) {
		return findEqualRecipe(true, pBlastRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualImplosionRecipe(ItemStack... aInputs) {
		return findEqualRecipe(false, pImplosionRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualVacuumRecipe(ItemStack... aInputs) {
		return findEqualRecipe(false, pVacuumRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualChemicalRecipe(ItemStack... aInputs) {
		return findEqualRecipe(true, pChemicalRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualLatheRecipe(ItemStack... aInputs) {
		return findEqualRecipe(true, pLatheRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualCutterRecipe(ItemStack... aInputs) {
		return findEqualRecipe(true, pCutterRecipes, aInputs);
	}
	
	public static GT_Recipe findEqualExtruderRecipe(ItemStack... aInputs) {
		return findEqualRecipe(false, pExtruderRecipes, aInputs);
	}
}