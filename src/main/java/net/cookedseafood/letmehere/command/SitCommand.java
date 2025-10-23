package net.cookedseafood.letmehere.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class SitCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
            CommandManager.literal("sit")
            .executes(context -> execute(context.getSource()))
            .then(
                CommandManager.argument("location", Vec3ArgumentType.vec3())
                .executes(context -> execute(context.getSource(), Vec3ArgumentType.getVec3(context, "location")))
                .then(
                    CommandManager.argument("align", BoolArgumentType.bool())
                    .executes(context -> execute(context.getSource(), Vec3ArgumentType.getVec3(context, "location"), BoolArgumentType.getBool(context, "align")))
                )
            )
        );
    }

    public static int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerWorld world = source.getWorld();
        Entity entity = source.getEntityOrThrow();
        if (entity.hasVehicle()) {
            return 0;
        }

        Direction direction = entity.getHorizontalFacing();
        BlockPos blockPos = entity.getBlockPos();

        BlockPos targetBlockPos = blockPos.offset(direction.getOpposite());
        BlockState targetBlockState = world.getBlockState(targetBlockPos);
        if (targetBlockState.isIn(BlockTags.STAIRS)) {
            BlockHalf stairHalf = targetBlockState.get(StairsBlock.HALF);
            Direction stairDirection = targetBlockState.get(StairsBlock.FACING);
            if (stairHalf == BlockHalf.BOTTOM && stairDirection != direction) {
                return execute(source, Vec3d.ofCenter(targetBlockPos), false);
            }
        }

        return execute(source, entity.getPos());
    }

    public static int execute(ServerCommandSource source, Vec3d location) throws CommandSyntaxException {
        return execute(source, location, false);
    }

    public static int execute(ServerCommandSource source, Vec3d location, boolean align) throws CommandSyntaxException {
        source.getEntityOrThrow().sit(align ? new Vec3d(Math.floor(location.x) + 0.5, location.y, Math.floor(location.z) + 0.5) : location);
        return 1;
    }
}
