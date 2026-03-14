package net.hederamc.letmehere.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.hederamc.fishbonetrehalose.api.Text;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class SitCommand {
    private static final SimpleCommandExceptionType DISTANCE_TOO_FAR = new SimpleCommandExceptionType(
            Text.literal("The distance to the target location is too far"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(
                Commands.literal("sit")
                        .executes(context -> sit(context.getSource()))
                        .then(
                                Commands.argument("location", Vec3Argument.vec3())
                                        .executes(context -> sit(
                                                context.getSource(),
                                                Vec3Argument.getVec3(context, "location")))
                                        .then(
                                                Commands.argument("align", BoolArgumentType.bool())
                                                        .executes(context -> sit(
                                                                context.getSource(),
                                                                Vec3Argument.getVec3(context, "location"),
                                                                BoolArgumentType.getBool(context, "align"))))));
    }

    public static int sit(CommandSourceStack source) throws CommandSyntaxException {
        source.getEntityOrException().sit();
        return Command.SINGLE_SUCCESS;
    }

    public static int sit(CommandSourceStack source, Vec3 pos) throws CommandSyntaxException {
        return sit(source, pos, false);
    }

    public static int sit(CommandSourceStack source, Vec3 pos, boolean align) throws CommandSyntaxException {
        Entity entity = source.getEntityOrException();

        if (align) {
            pos = new Vec3(Math.floor(pos.x) + 0.5, pos.y, Math.floor(pos.z) + 0.5);
        }

        if (entity.getPos().distanceToSqr(pos) > 1.0 && !source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
            throw DISTANCE_TOO_FAR.create();
        }

        entity.sit(pos);
        return Command.SINGLE_SUCCESS;
    }
}
