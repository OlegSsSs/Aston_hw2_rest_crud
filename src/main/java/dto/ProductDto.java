package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProductDto {
    private Long id;
    private String name;
    private Long userId;

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass()!= obj.getClass()) return false;
        return Objects.equals(id, ((ProductDto) obj).id) && Objects.equals(name, ((ProductDto) obj).name) && Objects.equals(userId, ((ProductDto ) obj).userId);
    }
}