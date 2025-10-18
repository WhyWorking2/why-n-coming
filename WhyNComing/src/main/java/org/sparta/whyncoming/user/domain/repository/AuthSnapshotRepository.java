package org.sparta.whyncoming.user.domain.repository;

import org.springframework.stereotype.Repository;

@Repository
public class AuthSnapshotRepository {

    private final UserRepository userRepository;

    // @Autowired 금지 → 명시적 생성자 주입
    public AuthSnapshotRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 유저의 현재 auth_version 조회
     * @param Integer JWT subject (userId)
     * @return 현재 auth_version
     */
    public int findAuthVersion(Integer userNo) {
        return userRepository.findAuthVersionByUserId(userNo)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자: " + userNo));
    }
}
