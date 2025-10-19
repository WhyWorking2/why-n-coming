package org.sparta.whyncoming.common.security.auth;

import com.github.benmanes.caffeine.cache.Cache;
import org.sparta.whyncoming.user.domain.repository.AuthSnapshotRepository;
import org.springframework.stereotype.Component;

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
