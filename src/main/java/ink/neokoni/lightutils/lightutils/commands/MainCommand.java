package ink.neokoni.lightutils.lightutils.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ink.neokoni.lightutils.lightutils.LightUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

public class MainCommand {
    private final LightUtils plugin = LightUtils.getInstance();
    public MainCommand() {
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
