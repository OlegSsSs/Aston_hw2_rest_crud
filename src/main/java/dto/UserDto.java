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

public class UserDto {
    private Long id;
    private String name;
    private String email;

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass()!= obj.getClass()) return false;
        return Objects.equals(id, ((UserDto) obj).id) &&
                Objects.equals(name, ((UserDto) obj).name) &&
                Objects.equals(email, ((UserDto) obj).email);
    }
}