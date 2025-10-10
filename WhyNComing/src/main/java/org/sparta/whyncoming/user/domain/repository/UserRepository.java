package org.sparta.whyncoming.user.domain.repository;

import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
