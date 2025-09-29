package org.sparta.whyncoming.test;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Test {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
