package com.playdata.orderservice.user.controller;

import com.playdata.orderservice.user.dto.UserSaveReqDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user") // user 관련 요청은 /user로 시작한다고 가정.
public class UserController {

    /*
     프론트 단에서 회원 가입 요청 보낼때 함께 보내는 데이터 (JSON) -> dto로 받자.
     {
        name: String,
        email: String,
        password: String,
        address: {
            city: String,
            street: String,
            zipCode: String
        }
     }
     */
    @PostMapping("/create")
    public void userCreate(@Valid @RequestBody UserSaveReqDto dto) {
        // 화면단에서 전달된 데이터를 DB에 넣자.
        // 혹시 이메일이 중복되었는가? -> 이미 이전에 회원가입을 한 회원이라면 거절.
        // dto를 DB에 바로 때려? -> dto를 entity로 바꾸는 로직 추가.
    }

}







