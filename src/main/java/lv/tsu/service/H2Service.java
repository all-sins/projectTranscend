package lv.tsu.service;

import lv.tsu.entity.Item;
import lv.tsu.repository.H2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class H2Service {

    private final H2Repository h2Repository;

    @Autowired
    public H2Service(H2Repository h2Repository) {
        this.h2Repository = h2Repository;
    }

    public void saveKeyValue(String key, int value, int damage) {
        Optional<Item> exItem = h2Repository.findByItemName(key);
        if (exItem.isPresent()) {
            Item item = exItem.get();
            item.setItemAmount(item.getItemAmount() + value);
            h2Repository.save(item);
            System.out.println(item.toString()+" already exists, adding "+value+" to "+item.getItemAmount());
        } else {
            System.out.println(key+" is new, saving...");
            Item kv = new Item();
            kv.setItemName(key);
            kv.setItemAmount(value);
            kv.setItemDamage(damage);
            h2Repository.save(kv);
        }
    }

    public List<Item> getItems() {
        return h2Repository.findAll();
    }

    public Optional<Item> getKeyValue(String key) {
        return h2Repository.findById(key);
    }

    public void deleteKeyValue(String key) {
        h2Repository.deleteById(key);
    }
}
