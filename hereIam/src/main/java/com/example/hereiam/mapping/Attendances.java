package com.example.hereiam.mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Attendances {
    private String email;
    private String name;
    private String identifier;
    private boolean checked;
}
