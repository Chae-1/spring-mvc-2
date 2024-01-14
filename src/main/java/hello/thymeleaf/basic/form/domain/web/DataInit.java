package hello.thymeleaf.basic.form.domain.web;


import hello.thymeleaf.basic.form.domain.item.Item;
import hello.thymeleaf.basic.form.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final ItemRepository repository;

    @PostConstruct
    public void init() {
        repository.save(new Item("itemA", 10000, 10));
        repository.save(new Item("itemB", 20000, 20));
    }

}
