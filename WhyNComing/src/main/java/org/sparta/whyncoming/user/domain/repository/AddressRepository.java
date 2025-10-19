package org.sparta.whyncoming.user.domain.repository;

import org.sparta.whyncoming.user.domain.entity.Address;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByUser(User user);

    Optional<Address> findByUserAndAddress(User user, String address);

    boolean existsByUserAndRepresentativeYn(User user, String representativeYn);

    List<Address> findAllByUserAndRepresentativeYn(User user, String representativeYn);

    // 단순 조회 (정렬은 서비스에서 처리)
    List<Address> findAllByUser(User user);

    // 대표 주소 우선 + 최신순 정렬을 DB에서 처리하고 싶을 때 사용할 수 있는 쿼리 메서드
    @Query("""
        select a
          from Address a
         where a.user = :user
         order by case when a.representativeYn = 'Y' then 0 else 1 end,
                  a.addressId desc
    """)
    List<Address> findAllByUserOrderByRepFirstThenNewest(User user);

    // 특정 주소 단건 조회 (추후 수정/삭제 시 유용)
    Optional<Address> findByAddressIdAndUser(UUID addressId, User user);

    // 대표 주소 존재 여부 확인 등 확장용 (필요 시)
    Optional<Address> findFirstByUserAndRepresentativeYn(User user, String representativeYn);
}
