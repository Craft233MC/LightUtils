package ink.neokoni.lightutils.Commands;

import com.github.retrooper.packetevents.protocol.advancements.AdvancementType;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ink.neokoni.lightutils.Commands.LightUtilsSubCommand.ReloadSubcommand;
import ink.neokoni.lightutils.LightUtils;
import ink.neokoni.lightutils.Utils.BungeeCardChannelUtils;
import ink.neokoni.lightutils.Utils.NotificationUtils;
import ink.neokoni.lightutils.Utils.TextUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class LightUtilsCommand {
    private final LightUtils plugin = LightUtils.getInstance();
    public LightUtilsCommand() {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("lightutils")
                    .then(Commands.literal("reload")
                            .requires(source -> source.getSender().isOp())
                            .executes(ctx -> {
                                new ReloadSubcommand(ctx.getSource().getSender());
                                return Command.SINGLE_SUCCESS;
                            }))
                    .then(Commands.literal("server")
                            .then(Commands.argument("server", StringArgumentType.string())
                                    .executes(ctx -> {
                                        if (ctx.getSource().getSender() instanceof Player player) {
                                            String serverName = ctx.getArgument("server", String.class);
                                            NotificationUtils.showAdvancementCentered(
                                                    TextUtils.getLang("server.teleporting", "{server}", serverName),
                                                    player,
                                                    ItemStack.of(Material.ENDER_PEARL),
                                                    AdvancementType.GOAL
                                            );
                                            Bukkit.getAsyncScheduler().runDelayed(
                                                    LightUtils.getInstance(),
                                                    tasl -> {
                                                        BungeeCardChannelUtils.sendToServer(player, serverName);
                                                    }, 4L, TimeUnit.SECONDS
                                            );
                                        }
                                        return Command.SINGLE_SUCCESS;
                                    })
                                    .then(Commands.argument("player", ArgumentTypes.player())
                                            .requires(ctx -> ctx.getSender().hasPermission("lightutils.server.others"))
                                            .executes(ctx -> {
                                                Player player = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                                                BungeeCardChannelUtils.sendToServer(player, ctx.getArgument("server", String.class));
                                                return Command.SINGLE_SUCCESS;
                                            }
                                            ))));
            commands.registrar().register(command.build());
        });
    }
}
