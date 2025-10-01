package org.sparta.whyncoming.test.presentaion.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadTestResponseV1 {

    private Long id;
    private String name;

    public ReadTestResponseV1(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ReadTestResponseV1 of(Long id, String name) {     // 객체를 만들어낸다는 의미로 관용적으로 쓰이는 이름
        return new ReadTestResponseV1(id, name);
    }
}
