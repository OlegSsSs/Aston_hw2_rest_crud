package entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order extends BaseEntity{
    private Long id;
    private String name;
}
