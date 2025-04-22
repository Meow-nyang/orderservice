package com.playdata.orderservice.user.entity;

import com.playdata.orderservice.common.entity.Address;
import jakarta.persistence.*;
import lombok.*;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Embedded // @Embeddable로 선언된 값 대입 (기본 생성자 필수)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Builder.Default // builder 패턴 사용해서 객체 초기화 시 초기값으로 세팅
    private Role role = Role.USER;


}
