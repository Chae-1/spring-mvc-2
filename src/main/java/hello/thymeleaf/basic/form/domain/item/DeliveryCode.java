package hello.thymeleaf.basic.form.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class DeliveryCode {
    private String code;
    private String displayName;

}
