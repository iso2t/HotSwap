// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package superscary.hotswap;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

/**
 * The Swapper class is a static utility class that contains methods for swapping items in the player's hotbar. This is used to swap items in the hotbar when the player clicks, and is also used to swap 
 * swords when the player clicks on an entity. The main logic for swapping items is performed in the swap and swapSword methods, which are called from the ClientEvent class when the player clicks. The swap 
 * method is used to swap items in the hotbar when the player clicks on a block, while the swapSword method is used to swap swords when the player clicks on an entity. The checkForAxe method is used to 
 * determine if a block should be treated as an axe, which is used to determine if we should swap an axe instead of a pickaxe or shovel when the player clicks on certain blocks. The getItemTools method is 
 * used to get a list of all the tools in the player's hotbar, which is used to determine which items to swap when the player clicks on a block. Overall, the Swapper class is a crucial part of the mod, as 
 * it contains the main logic for swapping items in the hotbar when the player clicks, and is also used to swap swords when the player clicks on an entity.
 */
public class Swapper {

   public Swapper() {
   }

   /**
    * This method is used to swap the item in the player's hand with the item in their hotbar slot when they click on a block. We check what block the player is looking at, and then we check if the player 
    * has a tool in their hotbar that can harvest that block. If they do, we swap to that tool. If they don't, we check if the block can be harvested with an axe, and if it can, we check if the player has 
    * an axe in their hotbar and swap to it if they do. If the block cannot be harvested with an axe, we check if the player has any tool that can harvest the block and swap to it if they do. This is used 
    * to swap items in the hotbar when the player clicks on a block, since the mod is meant to swap items in the hotbar when the player clicks, and we want to prioritize swapping tools that can harvest the 
    * block that the player is looking at.
    * 
    * @param entity The player entity to swap the item for. This is used to access the player's inventory and swap to the slot containing the tool that can harvest the block.
    * @param pos The position of the block that the player is looking at. This is used to determine what block the player is looking at, and therefore what tool we should swap to.
    * @param world The world the player is in. This is used to determine the context of the swap, such as whether we are swapping a sword or swapping an item in the hotbar.
    */
   public static void swap(PlayerEntity entity, BlockPos pos, World world) {
      Block block = world.func_180495_p(pos).func_177230_c();
      BlockState state = world.func_180495_p(pos);
      ToolType toolType = block.getHarvestTool(state);
      int harvestLevel = block.getHarvestLevel(state);

      for(Item i : getItemTools(entity)) {
         ItemStack stack = new ItemStack(i);
         if (i.getToolTypes(stack).contains(toolType)) {
            // I think this is where we check if the tool in the player's hotbar is able to harvest the block and if it is the best tool in the inventory for harvesting the block.
            int itemHarvestLevel = stack.func_77973_b().getHarvestLevel(stack, toolType, entity, state);
            if (itemHarvestLevel >= harvestLevel) {
               for(int j = 0; j < 10; ++j) {
                  if (entity.field_71071_by.func_70301_a(j).func_77969_a(stack)) {
                     entity.field_71071_by.field_70461_c = j;
                     return;
                  }
               }
            }
         } else if (checkForAxe(block)) {
            if (stack.func_77973_b().getToolTypes(stack).contains(ToolType.AXE)) {
               for(int l = 0; l < 10; ++l) {
                  if (entity.field_71071_by.func_70301_a(l).func_77969_a(stack)) {
                     entity.field_71071_by.field_70461_c = l;
                     return;
                  }
               }
            }
         } else if (stack.func_77973_b().func_150897_b(state)) {
            for(int j = 0; j < 10; ++j) {
               if (entity.field_71071_by.func_70301_a(j).func_77969_a(stack)) {
                  entity.field_71071_by.field_70461_c = j;
                  return;
               }
            }
         }
      }

   }

   /**
    * This method is used to swap the player's sword when they click on an entity. We iterate through the player's hotbar (the first 9 slots of their inventory) and check if each item is a sword. 
    * If it is a sword, we swap to that slot and return. This is used to swap the player's sword when they click on an entity, since the mod is meant to swap items in the hotbar when the player 
    * clicks, and we want to prioritize swapping swords when the player clicks on an entity.
    * 
    * @param entity The player entity to swap the sword for. This is used to access the player's inventory and swap to the slot containing the sword.
    * @param world The world the player is in. This is used to determine the context of the swap, such as whether we are swapping a sword or swapping an item in the hotbar.
    */
   public static void swapSword(PlayerEntity entity, World world) {
      PlayerInventory inventory = entity.field_71071_by;

      for(int i = 0; i < 10; ++i) {
         if (inventory.func_70301_a(i).func_77973_b().getRegistryName().toString().contains("sword")) {
            entity.field_71071_by.field_70461_c = i;
            return;
         }
      }

   }

   /**
    * This may be one of the worst ways to do this, but we check if the block's registry name contains certain strings that are commonly associated with blocks that should be treated as axes, such as 
    * logs, chests, crafting tables, bookshelves, and stairs. We also check if the registry name does not contain "ender", since ender chests should not be treated as axes. This is used to determine 
    * if we should swap an axe instead of a pickaxe or shovel when the player clicks on certain blocks, since some blocks can be harvested with multiple tools, and we want to prioritize swapping an 
    * axe for certain blocks that are commonly associated with axes.
    * 
    * @param block The block to check.
    * @return True if the block should be treated as an axe, false otherwise.
    */
   private static boolean checkForAxe(Block block) {
      String[] list = new String[]{"log", "axe", "chest", "crafting", "bookshelf", "stair"};

      for(String s : list) {
         if (block.getRegistryName().toString().contains(s) && !block.getRegistryName().toString().contains("ender")) {
            return true;
         }
      }

      return false;
   }

   /**
    * This method is used to get a list of all the tools in the player's hotbar, which is used to determine which items to swap when the player clicks on a block. We iterate through the player's hotbar 
    * (the first 9 slots of their inventory) and check if each item is a tool (axe, hoe, pickaxe, or shovel). If it is a tool, we add it to the list of items to swap. This is used to determine which items 
    * to swap when the player clicks on a block, since we only want to swap tools that are in the player's hotbar, and we want to prioritize swapping tools that are actually in the hotbar over tools that 
    * are in the main inventory.
    * 
    * @param entity The player entity to get the tools from. This is used to access the player's inventory and get the items in their hotbar.
    * @return A list of all the tools in the player's hotbar. This is used to determine which items to swap when the player clicks on a block, since we only want to swap tools that are in the player's hotbar, 
    * and we want to prioritize swapping tools that are actually in the hotbar over tools that are in the main inventory.
    */
   private static ArrayList<Item> getItemTools(PlayerEntity entity) {
      PlayerInventory inventory = entity.field_71071_by;
      ArrayList<Item> items = new ArrayList();

      for(int i = 0; i < 9; ++i) {
         ItemStack stack = inventory.func_70301_a(i);
         ToolType[] types = new ToolType[]{ToolType.AXE, ToolType.HOE, ToolType.PICKAXE, ToolType.SHOVEL};

         for(int j = 0; j < types.length; ++j) {
            if (stack.func_77973_b().getToolTypes(stack).contains(types[j]) && !items.contains(stack.func_77973_b())) {
               items.add(stack.func_77973_b());
            }
         }
      }

      return items;
   }
}
