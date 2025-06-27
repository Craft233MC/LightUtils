package ink.neokoni.lightutils.lightutils.handler;

import dev.geco.gsit.object.GSeat;
import dev.geco.gsit.object.GStopReason;
import dev.geco.gsit.object.IGCrawl;
import dev.geco.gsit.object.IGPose;
import net.william278.huskhomes.event.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import dev.geco.gsit.api.GSitAPI;
import dev.geco.gsit.GSitMain;
import dev.geco.gsit.service.TaskService;

import static org.bukkit.Bukkit.getServer;

public class onGSitTeleport implements Listener {
    private final GSitMain gSitMain = GSitMain.getInstance();

    public void register(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerBack(TeleportBackEvent e){
        Player p = getServer().getPlayer(e.getTeleport().getExecutor().getUuid());
        assert p != null;
        if (GSitAPI.isEntitySitting(p) ||
                GSitAPI.isPlayerCrawling(p) ||
                GSitAPI.isPlayerPosing(p)) {
            stopActions(p, GStopReason.TELEPORT, false);
        }
    }

    @EventHandler
    public void onReplyTeleport(ReplyTeleportRequestEvent e){
        if (!e.isAccepted()) {
            return;
        }
        Player p = getServer().getPlayer(e.getRecipient().getUuid());
        assert p != null;
        if (GSitAPI.isEntitySitting(p) ||
                GSitAPI.isPlayerCrawling(p) ||
                GSitAPI.isPlayerPosing(p)) {
            stopActions(p, GStopReason.TELEPORT, false);
        }
    }

    @EventHandler
    public void onRandomTeleport(RandomTeleportEvent e){
        Player p = getServer().getPlayer(e.getTeleport().getExecutor().getUuid());
        assert p != null;
        if (GSitAPI.isEntitySitting(p) ||
                GSitAPI.isPlayerCrawling(p) ||
                GSitAPI.isPlayerPosing(p)) {
            stopActions(p, GStopReason.TELEPORT, false);
        }
    }

    @EventHandler
    public void onTeleport(TeleportEvent e){ // spawn and warp command?
        Player p = getServer().getPlayer(e.getTeleport().getExecutor().getUuid());
        assert p != null;
        if (GSitAPI.isEntitySitting(p) ||
                GSitAPI.isPlayerCrawling(p) ||
                GSitAPI.isPlayerPosing(p)) {
            stopActions(p, GStopReason.TELEPORT, false);
        }
    }

    private void stopActions(Player player, GStopReason stopReason, boolean useSafeDismount) {
        TaskService taskser = new TaskService(gSitMain);
        taskser.run(new Runnable() {
            @Override
            public void run() {
                GSeat seat = gSitMain.getSitService().getSeatByEntity(player);
                if(seat != null) gSitMain.getSitService().removeSeat(seat, stopReason, useSafeDismount);
                IGPose pose = gSitMain.getPoseService().getPoseByPlayer(player);
                if(pose != null) gSitMain.getPoseService().removePose(pose, stopReason, useSafeDismount);
                IGCrawl crawl = gSitMain.getCrawlService().getCrawlByPlayer(player);
                if(crawl != null) gSitMain.getCrawlService().stopCrawl(crawl, stopReason);
            }
        }, false, player);
    }

}
