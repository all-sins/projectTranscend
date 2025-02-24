package lv.tsu.repository;

import lv.tsu.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface H2Repository extends JpaRepository<Item, String> {
    Optional<Item> findByItemName(String itemName);
}
