package com.example.hereiam.mapping;

import java.util.List;

import com.example.hereiam.entity.Session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcelExport {
    private Session session;
    private List<Attendances> attendances;
}
