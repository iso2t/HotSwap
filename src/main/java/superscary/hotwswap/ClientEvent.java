// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package superscary.hotswap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import superscary.hotswap.util.RayTracing;

/**
 * I am laughing at how poorly this is done. I am not sure if this is just a result of the decompiler, or if I just did this really weirdly, but here we are. This is the main event handler for the client 
 * side of the mod, and it handles both mouse input and world ticks. The mouse input is used to swap items when the player clicks, and the world ticks are used to update the ray tracing for looking at blocks 
 * and entities.
 */
@EventBusSubscriber
public class ClientEvent {

   /**
    * If I remember correctly, this is used for storing the current hotbar slot when the player starts clicking, so that it can be restored when they stop clicking. This is used to prevent the hotbar from changing 
    * when the player clicks, since the mod will swap items in and out of the hotbar when the player clicks. The value of -1 is used to indicate that there is no current hotbar slot stored, which is the default state 
    * when the player is not clicking. 
    * 
    * I know at some point there was a bug where the selected index would set to -1, which is why we force it to the actual current hotbar slot when the player logs in. There was no real way of setting this static 
    * field before the player logged in.
    */
   private static int current = -1;

   public ClientEvent() {
   }

   /**
    * So on each click, we check if the player is not in a GUI, and if they are not, we check if they are left clicking. If they are left clicking, we store the current hotbar slot if it is not already stored, and then 
    * we check what the player is looking at. If they are looking at an entity, we swap to their sword. If they are looking at a block, we swap the item in their hand with the item in their hotbar slot. When they release 
    * the left click, we restore their hotbar slot to the original one and allow them to update again.
    * 
    * @param event The mouse input event. This is fired whenever the player clicks a mouse button, and is used to handle swapping items in the hotbar.
    */
   @SubscribeEvent
   public static void click(InputEvent.MouseInputEvent event) {
      // We only want to do this if the player is not in a GUI, and if they are left clicking. This is to prevent the mod from interfering with other interactions, such as opening chests or using items. We also want to 
      // prevent the mod from interfering with other interactions, such as opening chests or using items, since the mod is only meant to swap items in the hotbar when the player clicks.
      if (Minecraft.func_71410_x().field_71462_r == null) {
         // We check if the mouse button is the left button (didn't think about doing this with keybinds, but it is what it is), and if the action is either press or release.
         if (event.getButton() == 0) {
            // Get the player entity from the Minecraft instance. The input event won't expose the player entity directly.
            PlayerEntity entity = Minecraft.func_71410_x().field_71439_g;

            // I believe this is where we store the current hotbar slot when the player starts clicking, so that we can restore it when they stop clicking. This is used to prevent the hotbar from changing when the player 
            // clicks, since the mod will swap items in and out of the hotbar when the player clicks. The value of -1 is used to indicate that there is no current hotbar slot stored, which is the default state when the player 
            // is not clicking.
            if (current == -1) {
               current = entity.field_71071_by.field_70461_c;
            }

            // We get the ray trace result from the Minecraft instance, which is used to determine what the player is looking at. This is used to determine whether we should swap the sword or swap the item in the hotbar.
            RayTraceResult result = Minecraft.func_71410_x().field_71476_x;

            // Check if the action is press or release. If it is press, we check what the player is looking at and swap accordingly. If it is release, we restore the hotbar slot to the original one and allow the player to update again.
            if (event.getAction() == 1) {

               // We get the world from the player entity, since we need it to swap items. This is used to determine the context of the swap, such as whether we are swapping a sword or swapping an item in the hotbar.
               World world = entity.field_70170_p;

               // We check if the player is looking at an entity or a block. If they are looking at an entity, we swap their sword. If they are looking at a block, we swap the item in their hand with the item in their hotbar slot. 
               // This is used to determine what kind of swap we should perform, since the mod has different behavior for swapping swords and swapping items in the hotbar.
               if (result.func_216346_c() == Type.ENTITY) {
                  Swapper.swapSword(entity, world);
               } else {

                  // We get the entity that the player is looking at, which is used to determine the context of the swap, such as whether we are swapping a sword or swapping an item in the hotbar. This is used to determine what kind of 
                  // swap we should perform, since the mod has different behavior for swapping swords and swapping items in the hotbar.
                  Entity viewpoint = Minecraft.func_71410_x().func_175606_aa();

                  // no idea
                  if (viewpoint == null) {
                     return;
                  }

                  // The main swapping logic is performed in the Swapper class, which is used to swap items in the hotbar when the player clicks. This is used to perform the actual swap, since the mod is only meant to swap items in the hotbar
                  // when the player clicks.
                  Swapper.swap(entity, RayTracing.INSTANCE.getLookingAtPos(), world);
               }
            }

            // When the player releases the left click, we restore their hotbar slot to the original one and allow them to update again. This is used to restore the player's hotbar slot to the original one when they stop clicking, 
            // since the mod will swap items in and out of the hotbar when the player clicks.
            if (event.getAction() == 0) {
               entity.field_71071_by.field_70461_c = current;
               entity.canUpdate(true);
               current = -1;
            }
         }

      }
   }

   /**
    * This might be one of the worst ways to do this. Every tick, we check if the world and player are not null, and if they are not, we fire the ray tracing event. This is used to update the ray tracing for looking at blocks and entities, 
    * since the mod needs to know what the player is looking at in order to swap items correctly. This is also used to update the ray tracing for looking at blocks and entities, since the mod needs to know what the player is looking at in order 
    * to swap items correctly.
    * 
    * @param event The tick event for the world. This is fired every tick for the world, and is used to update the ray tracing for looking at blocks and entities.
    */
   @SubscribeEvent
   public static void tick(TickEvent.WorldTickEvent event) {
      if (event.world != null && Minecraft.func_71410_x().field_71439_g != null) {
         RayTracing.INSTANCE.fire();
      }

   }
}
