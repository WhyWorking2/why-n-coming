package org.sparta.whyncoming.test.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sparta.whyncoming.test.domain.entity.Test;
import org.sparta.whyncoming.test.domain.repository.TestRepository;
import org.sparta.whyncoming.test.presentaion.dto.request.CreateTestRequestV1;
import org.sparta.whyncoming.test.presentaion.dto.request.UpdateTestRequestV1;
import org.sparta.whyncoming.test.presentaion.dto.response.ReadTestResponseV1;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceV1 {

    private final TestRepository testRepository;

    public TestServiceV1(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public String health() {
        return "ok";
    }

    @Transactional
    public ReadTestResponseV1 createTest(CreateTestRequestV1 req) {
        Test test = testRepository.save(new Test(null, req.getName())); // 엔티티 생성자 사용
        return toResponse(test);
    }

    public List<ReadTestResponseV1> findAllTest() {
        return testRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ReadTestResponseV1 findByIdTest(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test not found: " + id));
        return toResponse(test);
    }

    @Transactional
    public ReadTestResponseV1 updateTest(Long id, UpdateTestRequestV1 req) {
        Test e = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test not found: " + id));
        e.updateName(req.getName()); // Dirty Checking
        return toResponse(e);
    }

    @Transactional
    public void deleteTest(Long id) {
        if (!testRepository.existsById(id)) {
            throw new IllegalArgumentException("Test not found: " + id);
        }
        testRepository.deleteById(id);
    }

    public List<ReadTestResponseV1> searchByName(String keyword) {
        return testRepository.searchByName(keyword).stream()
                .map(this::toResponse)
                .toList();
    }

    private ReadTestResponseV1 toResponse(Test e) {
        return new ReadTestResponseV1(e.getId(), e.getName());
    }
}
