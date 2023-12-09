package dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class UserDto {
    private Long id;
    private String name;
    private String email;
}
