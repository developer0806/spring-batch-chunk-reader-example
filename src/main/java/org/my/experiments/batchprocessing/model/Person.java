package org.my.experiments.batchprocessing.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

public class Person implements Serializable {
    private String firstName;
    private String lastName;
}
