package com.example.hereiam.mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String email;
    private String fullname;
    private String identifier;
    private String avatar;
}
