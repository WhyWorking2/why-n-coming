package org.sparta.whyncoming.user.domain.repository;


import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserId(String userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String username);
    Optional<User> findByUserNo(Integer userNo);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
       update User u 
          set u.deletedDate = :deleteDate 
        where u.userNo = :userNo 
          and u.deletedDate is null
       """)
    int softDeleteByUserNo(@Param("userNo") Integer userNo,
                           @Param("deleteDate") Instant deleteDate);


}
