package org.sparta.whyncoming.common.security.auth;

import com.github.benmanes.caffeine.cache.Cache;
import org.sparta.whyncoming.user.domain.repository.AuthSnapshotRepository;
import org.springframework.stereotype.Component;

/**
 * AuthVersionProvider는 유저의 권한 변경 버전을 관리하는 컴포넌트입니다.
 * - 요청 시 캐시에 저장된 auth_version을 우선 조회하고, 없을 경우 DB에서 가져와 캐시에 저장합니다.
 * - 유저의 권한 또는 상태가 변경되면 해당 유저의 캐시를 무효화하여 최신 정보를 반영합니다.
 * 이를 통해 매 요청마다 DB 조회를 최소화하면서도 최신 권한 상태를 보장합니다.
 */
@Component
public class AuthVersionProvider {

    private final Cache<Integer, Integer> userAuthVersionCache;
    private final AuthSnapshotRepository authSnapshotRepository;

    // @Autowired 금지 → 명시적 생성자 주입
    public AuthVersionProvider(Cache<Integer, Integer> userAuthVersionCache,
                               AuthSnapshotRepository authSnapshotRepository) {
        this.userAuthVersionCache = userAuthVersionCache;
        this.authSnapshotRepository = authSnapshotRepository;
    }

    /** 캐시 조회 + DB 폴백 */
    public int currentVersion(Integer userNo) {
        Integer v = userAuthVersionCache.getIfPresent(userNo);
        if (v != null) return v;

        int dbv = authSnapshotRepository.findAuthVersion(userNo);
        userAuthVersionCache.put(userNo, dbv);
        return dbv;
    }

    /** 권한/상태 변경 직후 캐시 무효화 */
    public void evict(Integer userNo) {
        userAuthVersionCache.invalidate(userNo);
    }
}
