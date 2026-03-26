// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package superscary.hotswap.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;

/**
 * The RayTracing class is a utility class that contains methods for performing ray tracing to determine what the player is looking at. This is used to determine what block or entity the player 
 * is looking at when they click, which is used to determine what item to swap in the hotbar. The main method in this class is the fire method, which performs the ray tracing and stores the 
 * result in the target field. The rayTrace method is used to perform the actual ray tracing, and the getLookingAtPos method is used to get the position of the block that the player is looking at. 
 * Overall, the RayTracing class is a crucial part of the mod, as it contains the main logic for determining what the player is looking at when they click, which is used to determine what item to swap 
 * in the hotbar.
 * 
 * Probably the only thing worth while in this old mod.
 */
public class RayTracing {

   // Ran as a singleton since there is no reason to have multiple instances of this class, and it is used in multiple places throughout the mod, so it is easier to just have a single instance 
   // that can be accessed from anywhere.
   public static final RayTracing INSTANCE = new RayTracing();

   // The target field is used to store the result of the ray tracing, which is used to determine what block or entity the player is looking at when they click. This is used to determine what item 
   // to swap in the hotbar, since the mod is meant to swap items in the hotbar when the player clicks, and we want to determine what block or entity the player is looking at when they click in 
   // order to determine what item to swap in the hotbar.
   private RayTraceResult target = null;

   // The minecraft instance.
   private Minecraft mc = Minecraft.func_71410_x();

   // The lookingAt field is used to store the position of the block that the player is looking at when they click. This is used to determine what item to swap in the hotbar, since the mod is meant 
   // to swap items in the hotbar when the player clicks, and we want to determine what block the player is looking at when they click in order to determine what item to swap in the hotbar.
   private BlockPos lookingAt;

   public RayTracing() {
   }

   /**
    * This method is used to perform the ray tracing and store the result in the target field. We first check if the player is looking at an entity, and if they are, we store that in the target field. 
    * If they are not looking at an entity, we perform a ray trace from the player's viewpoint to determine what block they are looking at, and we store that in the target field. This is used to determine 
    * what block or entity the player is looking at when they click, which is used to determine what item to swap in the hotbar, since the mod is meant to swap items in the hotbar when the player clicks, 
    * and we want to determine what block or entity the player is looking at when they click in order to determine what item to swap in the hotbar.
    */
   public void fire() {
      if (this.mc.field_71476_x != null && this.mc.field_71476_x.func_216346_c() == Type.ENTITY) {
         this.target = this.mc.field_71476_x;
      } else {
         Entity viewpoint = this.mc.func_175606_aa();
         if (viewpoint != null) {
            this.target = this.rayTrace(viewpoint, (double)this.mc.field_71442_b.func_78757_d(), 0.0F);
         }
      }
   }

   /**
    * This method is used to perform the actual ray tracing, and is called from the fire method. We create a RayTraceContext with the player's eye position, the end position of the ray trace (which is 
    * calculated based on the player's look vector and reach distance), and the appropriate block and fluid modes. We then perform the ray trace using the player's world and store the result in the target 
    * field. This is used to determine what block or entity the player is looking at when they click, which is used to determine what item to swap in the hotbar, since the mod is meant to swap items in the 
    * hotbar when the player clicks, and we want to determine what block or entity the player is looking at when they click in order to determine what item to swap in the hotbar.
    * 
    * @param entity The entity from whose perspective the ray trace is performed
    * @param playerReach The reach distance of the player.
    * @param partialTicks The partial ticks for interpolation.
    * @return The result of the ray trace.
    */
   public RayTraceResult rayTrace(Entity entity, double playerReach, float partialTicks) {
      Vector3d eyePosition = entity.func_174824_e(partialTicks);
      Vector3d lookVector = entity.func_70676_i(partialTicks);
      Vector3d traceEnd = eyePosition.func_72441_c(lookVector.field_72450_a * playerReach, lookVector.field_72448_b * playerReach, lookVector.field_72449_c * playerReach);
      RayTraceContext context = new RayTraceContext(eyePosition, traceEnd, BlockMode.OUTLINE, FluidMode.NONE, entity);
      this.lookingAt = entity.func_130014_f_().func_217299_a(context).func_216350_a();
      return entity.func_130014_f_().func_217299_a(context);
   }

   public BlockPos getLookingAtPos() {
      return this.lookingAt;
   }
}
