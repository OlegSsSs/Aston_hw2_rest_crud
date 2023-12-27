package entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product extends BaseEntity {
    private String name;
    private Long userId;
}