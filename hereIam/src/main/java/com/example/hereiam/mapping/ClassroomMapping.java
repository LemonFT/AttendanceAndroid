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
public class ClassroomMapping {
    private Long id;

    private String code;

    private String name;

    private String desc;

    private int numberMember;
}
