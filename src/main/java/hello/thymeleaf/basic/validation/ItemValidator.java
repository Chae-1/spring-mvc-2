package hello.thymeleaf.basic.validation;

import hello.thymeleaf.basic.form.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        if (item.getItemName() == null || item.getItemName().isEmpty()) {
            errors.rejectValue("itemName", "required",  null, null);
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >= 10000) {
            errors.rejectValue("price", "range",  null, null);
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            errors.rejectValue("quantity", "max",  null, null);
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.reject("totalPriceMin",  new Object[]{10000, resultPrice}, null);
            }
        }
    }
}
