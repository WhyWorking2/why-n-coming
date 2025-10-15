package org.sparta.whyncoming.user.domain.repository;

import org.sparta.whyncoming.user.domain.entity.Address;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByUserAndAddress(User user, String address);
}
