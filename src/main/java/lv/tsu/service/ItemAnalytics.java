package lv.tsu.service;

import lv.tsu.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ItemAnalytics {

    private final DiscordPoster discordPoster;
    private final H2Service h2Service;
    private boolean isRunning;

    @Autowired
    public ItemAnalytics(DiscordPoster discordPoster, H2Service h2Service) {
        this.discordPoster = discordPoster;
        this.h2Service = h2Service;
    }

    public boolean runAnalyticsThread() {
        if (!isRunning) {
            // Create a ScheduledExecutorService with a pool of 1 thread
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            Runnable task = () -> {
                System.out.println("Running task at: " + System.currentTimeMillis());
                List<Item> itemList = h2Service.getItems();
                String payload = discordPoster.buildEmbedPayload(itemList);
                System.out.println(payload);
                discordPoster.sendEmbed(payload);
            };
            scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.DAYS);
        }
        return isRunning;
    }
}
