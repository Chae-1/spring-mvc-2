package hello.thymeleaf.basic.form.domain.item;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {
    private static final Map<Long, Item> store = new HashMap<>();
    private static Long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(sequence, item);
        return item;
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public void update(Item updateParam, Long itemId) {
        Item item = store.get(itemId);
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());

        item.setOpen(updateParam.getOpen());
        item.setDeliveryCode(updateParam.getDeliveryCode());
        item.setRegions(updateParam.getRegions());
        item.setItemType(updateParam.getItemType());
    }
    
    public void clearStore() {
        store.clear();
    }

}
