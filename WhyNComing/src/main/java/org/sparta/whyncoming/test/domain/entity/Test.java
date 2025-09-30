package org.sparta.whyncoming.test.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test")
@Getter @NoArgsConstructor
public class Test {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public Test(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
