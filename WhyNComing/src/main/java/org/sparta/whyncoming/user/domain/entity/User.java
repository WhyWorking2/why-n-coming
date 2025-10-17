package org.sparta.whyncoming.user.domain.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.common.entity.BaseTimeEntity;
import org.sparta.whyncoming.order.domain.entity.Order;
import org.sparta.whyncoming.order.domain.entity.OwnerReview;
import org.sparta.whyncoming.order.domain.entity.Review;
import org.sparta.whyncoming.order.domain.entity.Cart;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.user.domain.enums.UserRoleEnum;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userNo;

    @Column(nullable = false, length = 36, unique = true)
    private String userId;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String userName;

    @Column(nullable = false, length = 11, unique = true)
    private String userPhone;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum role = UserRoleEnum.CUSTOMER;

    @Column(nullable = false)
    private Integer authVersion = 1;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Store> stores = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnerReview> ownerReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

    public User(String userId, String password, String userName, String userPhone,
                String email, UserRoleEnum role, List<Address> addresses, List<Store> stores,
                List<Order> orders, List<Review> reviews, List<OwnerReview> ownerReviews, List<Cart> carts) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.userPhone = userPhone;
        this.email = email;
        this.role = role;
        this.addresses = addresses;
        this.stores = stores;
        this.orders = orders;
        this.reviews = reviews;
        this.ownerReviews = ownerReviews;
        this.carts = carts;
    }

    public User(String userId, String password, String userName, String userPhone,
                String email, UserRoleEnum role) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.userPhone = userPhone;
        this.email = email;
        this.role = role;
    }

    // 명시적 변경 메서드
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
    public void updateName(String name) {
        this.userName = name;
    }
    public void updatePhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }
    public void updateRole(UserRoleEnum newRole) {this.role = newRole;}

    public void increaseAuthVersion() {
        this.authVersion += 1;
    }
}
