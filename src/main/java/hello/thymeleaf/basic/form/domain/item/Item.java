package hello.thymeleaf.basic.form.domain.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
public class Item {
    private Long id;

    @NotBlank(groups = {AddForm.class, EditForm.class})
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000, groups = {AddForm.class, EditForm.class})
    private Integer price;

    @NotNull(groups = {AddForm.class, EditForm.class})
    @Max(value = 9999, groups = AddForm.class)
    private Integer quantity;

    private Boolean open;
    private ItemType itemType;
    private String deliveryCode;
    private List<String> regions;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
