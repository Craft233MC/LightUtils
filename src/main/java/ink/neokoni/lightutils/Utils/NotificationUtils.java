package ink.neokoni.lightutils.Utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.advancements.*;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAdvancements;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotificationUtils {
    public static void showAdvancementAsDesc(Component component, Player player, ItemStack icon, AdvancementType type) {
        AdvancementCore(component, player, type, icon);
    }

    public static void showAdvancementCentered(Component component, Player player, ItemStack icon, AdvancementType type) {
        Component centeredComponent = Component.empty().appendNewline()
                .append(component).appendNewline();
        AdvancementCore(centeredComponent, player, type, icon);
    }

    public static void showAdvancementDown(Component component, Player player, ItemStack icon, AdvancementType type) {
        Component downComponent = Component.empty().appendNewline()
                .append(component);
        AdvancementCore(downComponent, player, type, icon);
    }

    public static void showAdvancementUp(Component component, Player player, ItemStack icon, AdvancementType type) {
        Component upComponent = component.appendNewline();
        AdvancementCore(upComponent, player, type, icon);
    }

    private static void AdvancementCore(Component component, Player player, AdvancementType type, ItemStack icon) {
        AdvancementDisplay advancementDisplay = new AdvancementDisplay(
                component,
                Component.empty(),
                SpigotConversionUtil.fromBukkitItemStack(icon.asOne()),
                type,
                null,
                true,
                false,
                0,
                0
        );
        ResourceLocation resourceLocation = new ResourceLocation("lightutils", "notification/advancement");
        List<AdvancementHolder> advancementHolders = Collections.singletonList(
                new AdvancementHolder(
                        resourceLocation,
                        new Advancement(
                                null,
                                advancementDisplay,
                                List.of(List.of("")),
                                true
                        )
                ));
        Map<ResourceLocation, AdvancementProgress> advancementProgressMap = Map.of(
                resourceLocation,
                new AdvancementProgress(Map.of("",
                        new AdvancementProgress.CriterionProgress(System.currentTimeMillis()))));
        WrapperPlayServerUpdateAdvancements show = new WrapperPlayServerUpdateAdvancements(
                false,
                advancementHolders,
                Set.of(),
                advancementProgressMap,
                true
        );
        WrapperPlayServerUpdateAdvancements remove = new WrapperPlayServerUpdateAdvancements(
                false,
                Collections.emptyList(),
                Set.of(resourceLocation),
                advancementProgressMap,
                true
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, show);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, remove);
    }
}
