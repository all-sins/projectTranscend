package lv.tsu.controller;

// Spring.
import lv.tsu.service.DiscordPoster;
import lv.tsu.service.ItemAnalytics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

// Tsu.
import lv.tsu.entity.Item;
import lv.tsu.service.H2Service;

// Standard Java.
import java.util.List;

@RestController
public class WebController {

    private final H2Service h2Service;
    private final DiscordPoster discordPoster;
    private final ItemAnalytics itemAnalytics;

    @Autowired
    public WebController(H2Service h2Service, DiscordPoster discordPoster, ItemAnalytics itemAnalytics) {
        this.h2Service = h2Service;
        this.discordPoster = discordPoster;
        this.itemAnalytics = itemAnalytics;
    }

    @PostMapping("/postBlockData")
    boolean postBlockData(@RequestBody Item item) {
        String name = item.getItemName();
        int amount = item.getItemAmount();
        int damage = item.getItemDamage();
        // discordPoster.sendMessage(name+" x "+amount);
        System.out.print("Saving -> "+name+" "+amount+" "+damage+"   ");
        h2Service.saveKeyValue(name, amount, damage);
        return true;
    }

    @GetMapping("/enableAnalytics")
    boolean enableAnalytics() {
        return itemAnalytics.runAnalyticsThread();
    }

}
