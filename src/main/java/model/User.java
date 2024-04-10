package model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User {
    private String id;
    private String username;
    private String password;
}
