package org.sparta.whyncoming.test.domain.repository;

import org.sparta.whyncoming.test.domain.entity.Test;
import org.sparta.whyncoming.test.infrastructure.repository.TestRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long>, TestRepositoryCustom {
}
