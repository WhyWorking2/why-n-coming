package org.sparta.whyncoming.test;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Test {

    @Id
    @SequenceGenerator(
            name = "test_seq",
            sequenceName = "test_seq",
            allocationSize = 1        // ★ DB 시퀀스 증가폭(1)과 맞춤
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_seq")
    private Long id;

    private String name;
}
