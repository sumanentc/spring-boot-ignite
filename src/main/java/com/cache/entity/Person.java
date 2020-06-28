package com.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person  implements Serializable {
    @QuerySqlField(index = true)
    private Long id;
    @QuerySqlField
    private String firstName;
    @QuerySqlField
    private String lastName;
    @QuerySqlField
    private int age;

}
