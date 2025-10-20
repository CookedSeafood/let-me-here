package net.cookedseafood.letmehere.command;

import com.mojang.brigadier.CommandDispatcher;
import net.cookedseafood.letmehere.LetMeHere;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class LetMeHereCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
            CommandManager.literal("letmehere")
            .then(
                CommandManager.literal("version")
                .executes(context -> executeVersion((ServerCommandSource)context.getSource()))
            )
        );
    }

    public static int executeVersion(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Let Me Here " + LetMeHere.VERSION_MAJOR + "." + LetMeHere.VERSION_MINOR + "." + LetMeHere.VERSION_PATCH), false);
        return 0;
    }
}
