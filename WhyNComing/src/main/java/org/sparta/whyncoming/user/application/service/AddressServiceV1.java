package org.sparta.whyncoming.user.application.service;

import jakarta.transaction.Transactional;
import org.sparta.whyncoming.common.exception.BusinessException;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.user.domain.entity.Address;
import org.sparta.whyncoming.user.domain.entity.User;
import org.sparta.whyncoming.user.domain.repository.AddressRepository;
import org.sparta.whyncoming.user.domain.repository.UserRepository;
import org.sparta.whyncoming.user.presentation.dto.request.AddressCreateRequestDtoV1;
import org.sparta.whyncoming.user.presentation.dto.request.AddressUpdateRequestDtoV1;
import org.sparta.whyncoming.user.presentation.dto.response.AddressResponseDtoV1;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceV1 {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public AddressServiceV1(AddressRepository addressRepository,UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }


    /**
     * POST /member/delivery/address
     */
    /**
     * POST /member/delivery/address
     * - 사용자의 주소가 하나도 없거나, 기존 주소들의 대표여부가 모두 'N'이면
     *   새로 등록하는 주소를 대표('Y')로 저장한다.
     * - 요청이 대표('Y')인 경우, 기존 대표는 모두 'N'으로 내린다(단일 대표 보장).
     */
    @Transactional
    public AddressResponseDtoV1 addAddress(CustomUserDetailsInfo user,
                                           AddressCreateRequestDtoV1 request) {
        // 회원 조회
        User userInfo = userRepository.findById(user.getUserNo()).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND, "회원 정보를 찾을 수 없습니다.")
        );
        if (userInfo.isDeleted()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "탈퇴한 회원입니다.");
        }

        // 대표 여부 판단
        boolean hasAnyAddress = addressRepository.existsByUser(userInfo);
        boolean hasRepresentative = addressRepository.existsByUserAndRepresentativeYn(userInfo, "Y");

        String repYn = (request.getRepresentativeYn() == null || request.getRepresentativeYn().isBlank())
                ? "N"
                : request.getRepresentativeYn().trim().toUpperCase();

        // 주소가 없거나 기존 대표가 없다면 자동으로 대표 지정
        if (!hasAnyAddress || !hasRepresentative) {
            repYn = "Y";
        }

        // 대표로 지정하려는 경우, 기존 대표를 모두 N으로 내림(단일 대표 보장)
        if ("Y".equals(repYn)) {
            addressRepository.findAllByUserAndRepresentativeYn(userInfo, "Y")
                    .forEach(Address::makeNonRepresentative);
        }

        // 저장
        Address address = new Address(userInfo, request.getAddress(), repYn);
        Address saved = addressRepository.save(address);

        return new AddressResponseDtoV1(saved.getAddressId(), saved.getAddress(), saved.getRepresentativeYn());
    }

    /**
     * GET /member/delivery/addresses
     */
    @Transactional
    public List<AddressResponseDtoV1> getAddresses(CustomUserDetailsInfo userDetailsInfo) {
        // 회원 조회
        User user = userRepository.findById(userDetailsInfo.getUserNo()).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND, "회원 정보를 찾을 수 없습니다.")
        );

        // 주소 조회 (대표 주소 우선 정렬 후, 최신순)
        List<Address> addresses = addressRepository.findAllByUser(user);
        addresses.sort(
                java.util.Comparator
                        .comparing((Address a) -> !"Y".equals(a.getRepresentativeYn())) // 대표(Y) 먼저
                        .thenComparing(Address::getAddressId, java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder())) // id 역순(신규 우선)
        );

        return addresses.stream()
                .map(a -> new AddressResponseDtoV1(
                        a.getAddressId(),
                        a.getAddress(),
                        a.getRepresentativeYn()
                ))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * PATCH /member/delivery/{addressId}
     * - 부분수정(PATCH): null이 아닌 필드만 반영
     * - representativeYn = 'Y'로 변경 시, 해당 사용자의 다른 대표 주소를 모두 'N'으로 내림(단일 대표 보장)
     */
    @Transactional
    public AddressResponseDtoV1 updateAddress(
            CustomUserDetailsInfo userDetailsInfo,
            UUID addressId,
            AddressUpdateRequestDtoV1 request
    ) {
        // 회원 검증
        User user = userRepository.findById(userDetailsInfo.getUserNo()).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND, "회원 정보를 찾을 수 없습니다.")
        );
        if (user.isDeleted()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "탈퇴한 회원입니다.");
        }

        // 소유 주소 확인
        Address address = addressRepository.findByAddressIdAndUser(addressId, user).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND, "수정할 배송지를 찾을 수 없습니다.")
        );

        // === 부분 수정 적용 ===
        if (request.address() != null) {
            // 엔티티에 적절한 세터/업데이트 메서드로 반영
            address.setAddress(request.address());
        }

        // 대표 주소 변경 로직
        if (request.representativeYn() != null) {
            String repYn = request.representativeYn().trim().toUpperCase();
            if ("Y".equals(repYn)) {
                // 기존 대표 주소들 모두 내림
                addressRepository.findAllByUserAndRepresentativeYn(user, "Y")
                        .forEach(Address::makeNonRepresentative);
                // 현재 주소를 대표로 지정
                address.makeRepresentative();
            } else if ("N".equals(repYn)) {
                address.makeNonRepresentative();
            } else {
                throw new BusinessException(ErrorCode.VALIDATION_FAILED, "대표 주소 여부는 Y 또는 N 이어야 합니다.");
            }
        }

        Address saved = addressRepository.save(address);
        return new AddressResponseDtoV1(saved.getAddressId(), saved.getAddress(), saved.getRepresentativeYn());
    }

    /**
     * DELETE /member/delivery/{addressId}
     */
    @Transactional
    public void deleteAddress(CustomUserDetailsInfo userDetailsInfo, UUID addressId) {
        // 회원 검증
        User user = userRepository.findById(userDetailsInfo.getUserNo()).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND, "회원 정보를 찾을 수 없습니다.")
        );

        // 소유 주소 조회
        Address address = addressRepository.findByAddressIdAndUser(addressId, user).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND, "삭제할 배송지를 찾을 수 없습니다.")
        );

        // 대표 주소는 삭제 불가: 먼저 다른 주소를 대표로 변경해야 함
        if ("Y".equals(address.getRepresentativeYn())) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "대표 주소는 삭제할 수 없습니다. 먼저 다른 주소를 대표로 변경하세요.");
        }

        // 실제 삭제 (소프트 삭제 정책이 있다면 여기에서 플래그/날짜 업데이트로 변경)
        addressRepository.delete(address);
    }
}
