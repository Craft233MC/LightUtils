package ink.neokoni.lightutils.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ink.neokoni.lightutils.DataStorage.Configs;
import ink.neokoni.lightutils.LightUtils;
import ink.neokoni.lightutils.Utils.TextUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Location;
import org.bukkit.World;

import java.math.RoundingMode;
import java.text.NumberFormat;

public class SetWorldSpawnCommand {
    public SetWorldSpawnCommand() {
        LightUtils plugin = LightUtils.getInstance();
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("setworldspawn")
                    .requires(ctx ->
                            ctx.getSender().hasPermission("minecraft.command.setworldspawn") &&
                            Configs.getConfigs().getBoolean("utils.setworldspawn.enable"))
                    .then(Commands.argument("Postions", ArgumentTypes.finePosition())
                            .then(Commands.argument("World", ArgumentTypes.world())
                                    .executes(ctx -> {
                                        FinePositionResolver resolver = ctx.getArgument("Postions", FinePositionResolver.class);
                                        FinePosition position = resolver.resolve(ctx.getSource());
                                        World world = ctx.getArgument("World", World.class);
                                        NumberFormat nf = NumberFormat.getNumberInstance();
                                        nf.setMaximumFractionDigits(2);
                                        nf.setRoundingMode(RoundingMode.HALF_UP);
                                        Location location = new Location(world, f(position.x()), f(position.y()), f(position.z()));
                                        String locationStr = world.getName()+","+f(position.x())+","+f(position.y())+","+f(position.z());
                                        world.setSpawnLocation(location);
                                        Configs.getConfigs().set("utils.setworldspawn.location", locationStr);
                                        Configs.saveConfigs();
                                        ctx.getSource().getSender().sendMessage(TextUtils.getLang("setworldspawn", "{location}", locationStr));
                                        return Command.SINGLE_SUCCESS;
                                    })));
            commands.registrar().register(command.build());
        });
    }

    private double f(double input) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setMaximumFractionDigits(2);
        return Double.parseDouble(nf.format(input));
    }
}
