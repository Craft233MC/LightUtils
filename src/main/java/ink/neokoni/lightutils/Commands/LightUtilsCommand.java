package ink.neokoni.lightutils.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ink.neokoni.lightutils.Commands.LightUtilsSubCommand.ReloadSubcommand;
import ink.neokoni.lightutils.LightUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

public class LightUtilsCommand {
    private final LightUtils plugin = LightUtils.getInstance();
    public LightUtilsCommand() {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("lightutils")
                    .requires(source -> source.getSender().isOp())
                    .then(Commands.literal("reload")
                            .executes(ctx -> {
                                new ReloadSubcommand(ctx.getSource().getSender());
                                return Command.SINGLE_SUCCESS;
                            }));
            commands.registrar().register(command.build());
        });
    }
}
