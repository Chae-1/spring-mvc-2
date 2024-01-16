package hello.thymeleaf.basic.validation;

import hello.thymeleaf.basic.form.domain.item.DeliveryCode;
import hello.thymeleaf.basic.form.domain.item.Item;
import hello.thymeleaf.basic.form.domain.item.ItemRepository;
import hello.thymeleaf.basic.form.domain.item.ItemType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/validation/items/v1")
@RequiredArgsConstructor
public class ValidationItemControllerv1 {
    private final ItemRepository itemRepository;

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "보통 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");

        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }


//    @PostMapping("/add")
    public String saveV1(@ModelAttribute Item item,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        // 1. 검증 결과를 보관, 결과를 모델을 통해, 전달.
        Map<String, String> errors = new HashMap<>();

        // 2. 검증 시작.
        if (item.getItemName() == null || item.getItemName().isEmpty()) {
            errors.put("itemName", "입력 값은 필수 입니다.");
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >= 10000) {
            errors.put("price", "가격은 1,000원 ~ 1,000,000원 사이 입니다.");
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            errors.put("quantity", "수량은 최대 9,999개 이하이어야 합니다.");
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            if (item.getPrice() * item.getQuantity() < 10000) {
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다.");
            }
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:validation/items/v1/{itemId}";
    }



    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(item, itemId);
        return "redirect:/validation/items/v1/{itemId}";
    }

}
