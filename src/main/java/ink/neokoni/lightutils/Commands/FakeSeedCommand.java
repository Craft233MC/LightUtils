package ink.neokoni.lightutils.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ink.neokoni.lightutils.DataStorage.Languages;
import ink.neokoni.lightutils.LightUtils;
import ink.neokoni.lightutils.Utils.SeedUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

public class FakeSeedCommand {
    private final LightUtils plugin = LightUtils.getInstance();

    private String template = Languages.getLanguages().getString("fake-seed.template");
    private String hover = Languages.getLanguages().getString("fake-seed.hover");

    public FakeSeedCommand() {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("seed")
                            .executes(ctx -> {
                                return sendFakeSeed(ctx.getSource().getSender());
                            });
            commands.registrar().register(command.build());
        });
    }

    private int sendFakeSeed(CommandSender sender){
        boolean isReturnRealSeed = SeedUtils.isReturnFakeSeed(sender);

        if (isReturnRealSeed) {
            sender.sendMessage(getSeedMsg(SeedUtils.getFakeSeed()));
        } else {
            sender.sendMessage(getSeedMsg(SeedUtils.getRealSeed()));
        }

        return Command.SINGLE_SUCCESS;
    }

    private Component getSeedMsg(String seed){
        ComponentLike seedComponent = Component.text(seed)
                .color(NamedTextColor.GREEN)
                .hoverEvent(Component.text(hover));
        TextReplacementConfig replacement = TextReplacementConfig.builder()
                .matchLiteral("{seed}")
                .replacement(seedComponent)
                .build();
        return Component.text(template).replaceText(replacement);
    }
}
