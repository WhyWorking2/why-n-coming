package org.sparta.whyncoming.user.domain.enums;

public enum UserRoleEnum {
    CUSTOMER(Authority.CUSTOMER),  // 사용자 권한
    OWNER(Authority.OWNER),  // 관리자 권한
    MANAGER(Authority.MANAGER),
    MASTER(Authority.MASTER);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String CUSTOMER = "ROLE_CUSTOMER";
        public static final String OWNER = "ROLE_OWNER";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String MASTER = "ROLE_MASTER";
    }
}
