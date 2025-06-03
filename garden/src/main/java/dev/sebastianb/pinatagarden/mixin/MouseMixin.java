package dev.sebastianb.pinatagarden.mixin;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {


    @Shadow private boolean ignoreFirstMove;

    @Shadow @Final private Minecraft minecraft;

    @Shadow private double xpos;

    @Shadow private double ypos;


    @Shadow private boolean isRightPressed;


    @Shadow private double accumulatedDX;

    @Shadow private double accumulatedDY;


    double previousHeldMouseX = xpos;
    double previousHeldMouseY = ypos;


    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    public void injectTurnPlayer(double timeDelta, CallbackInfo ci) {
        if (!isRightPressed) {
            ci.cancel();
        } else {
            InputConstants.grabOrReleaseMouse(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR_CAPTURED, previousHeldMouseX, previousHeldMouseY);
        }
    }

    private double scrollOffset = 0.0;

    @Inject(method = "onScroll", at = @At("HEAD"))
    public void injectOnScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (minecraft.player == null || minecraft.level == null) return;
        HitResult hitResult = minecraft.player.pick(100, 10, true);
        if (hitResult instanceof BlockHitResult blockHitResult) {
            double distance = blockHitResult.distanceTo(minecraft.player);

            // Use logarithmic scaling to reduce speed as player nears the ground
            double scale = Math.log1p(distance) / 5; // log1p(x) = log(1 + x), safe for small distances
            scrollOffset += vertical * scale;
        }
    }

    // TODO: this works for now but we should probably make the mouse behave more like how city skylines does it
    @Inject(method = "turnPlayer", at = @At("TAIL"))
    public void smoothScrollCamera(double timeDelta, CallbackInfo ci) {
        if (minecraft.player == null || minecraft.level == null) return;

        if (Math.abs(scrollOffset) > 0.01) {
            Vec3 currentPos = minecraft.player.position();
            Vec3 viewVec = minecraft.player.getLookAngle();

            double moveAmount = scrollOffset * 0.1;
            Vec3 intendedOffset = viewVec.scale(moveAmount);
            Vec3 targetPos = currentPos.add(intendedOffset);

            // Find ground level below player
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(currentPos.x, currentPos.y, currentPos.z);
            int groundY = getLowestGroundPoint(currentPos, mutablePos);

            if (groundY == -1) {
                scrollOffset = 0.0; // no ground found
                return;
            }


            // === Cap scroll distance ===
            if (groundY > 60.0) {
                double cappedY = groundY + 60.0;
                if (targetPos.y > cappedY) {
                    targetPos = new Vec3(targetPos.x, cappedY, targetPos.z);
                    scrollOffset = 0.0;
                }
            } else {
                // Optional: block forward scrolling into obstacles
                if (scrollOffset > 0) {
                    boolean isNearBlock = false;
                    double maxDistance = 3.0;
                    double stepSize = 0.3;
                    for (double d = 0.0; d <= maxDistance; d += stepSize) {
                        Vec3 checkPos = currentPos.add(viewVec.scale(d));
                        BlockPos blockPos = BlockPos.containing(checkPos);
                        if (!minecraft.level.getBlockState(blockPos).isAir()) {
                            isNearBlock = true;
                            break;
                        }
                    }

                    if (isNearBlock) {
                        scrollOffset = 0.0;
                        return;
                    }
                }
            }

            // Apply the move
            minecraft.player.setPos(targetPos.x, targetPos.y, targetPos.z);
            scrollOffset *= 0.9; // damping
        }
    }

    private int getLowestGroundPoint(Vec3 currentPos, BlockPos.MutableBlockPos mutablePos) {
        int groundY = -1;
        for (int y = (int) currentPos.y; y >= minecraft.level.getMinY(); y--) {
            mutablePos.setY(y);
            if (!minecraft.level.getBlockState(mutablePos).isAir()) {
                groundY = y;
                break;
            }
        }
        return groundY;
    }


    @Inject(method = "grabMouse", at = @At("TAIL"), cancellable = true)
    public void injectMouseGrab(CallbackInfo ci) {
        if (this.minecraft.isWindowActive()) {
            if (isRightPressed) {
                this.accumulatedDX = 0;
                this.accumulatedDY = 0;
                InputConstants.grabOrReleaseMouse(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR_NORMAL, previousHeldMouseX, previousHeldMouseY);
                this.minecraft.setScreen(null);

                this.previousHeldMouseX = xpos;
                this.previousHeldMouseY = ypos;

            }
        }
    }

}
