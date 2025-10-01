package org.sparta.whyncoming.test.infrastructure.repository;

import org.sparta.whyncoming.test.domain.entity.Test;

import java.util.List;

public interface TestRepositoryCustom {

    List<Test> searchByName(String keyword);
}
