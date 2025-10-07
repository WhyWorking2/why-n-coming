package org.sparta.whyncoming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //시간 체크를 위해 추가
@SpringBootApplication
public class WhyNComingApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhyNComingApplication.class, args);
    }

}
