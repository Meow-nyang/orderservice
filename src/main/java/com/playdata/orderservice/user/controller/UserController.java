package com.playdata.orderservice.user.controller;

import com.playdata.orderservice.common.auth.JwtTokenProvider;
import com.playdata.orderservice.common.dto.CommonResDto;
import com.playdata.orderservice.user.dto.UserLoginReqDto;
import com.playdata.orderservice.user.dto.UserResDto;
import com.playdata.orderservice.user.dto.UserSaveReqDto;
import com.playdata.orderservice.user.entity.User;
import com.playdata.orderservice.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user") // user 관련 요청은 /user로 시작한다고 가정.
@RequiredArgsConstructor
public class UserController {

    // 컨트롤러는 서비스에 의존하고 있다. (요청과 함께 전달받은 데이터를 서비스에게 넘겨야 함!)
    // 빈 등록된 서비스 객체를 자동으로 주입 받자!
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

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
    public ResponseEntity<?> userCreate(@Valid @RequestBody UserSaveReqDto dto) {
        // 화면단에서 전달된 데이터를 DB에 넣자.
        // 혹시 이메일이 중복되었는가? -> 이미 이전에 회원가입을 한 회원이라면 거절.
        // dto를 DB에 바로 때려? -> dto를 entity로 바꾸는 로직 추가.


        User saved = userService.userCreate(dto);
        // ResponseEntity는 응답을 줄 때 다양한 정보를 한번에 포장해서 넘길 수 있습니다.
        // 요청에 따른 응답 상태 코드, 응답 헤더에 정보를 추가, 일관된 응답 처리를 제공합니다.

        CommonResDto resDto
                = new CommonResDto(HttpStatus.CREATED,
                "User Created", saved.getName());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody UserLoginReqDto dto) {
        User user = userService.login(dto);

        // 회원 정보가 일치한다면 -> 로그인 성공.
        // 로그인 유지를 해 주고 싶다.
        // 백엔드는 요청이 들어왔을 때 이 사람이 이전에 로그인 성공 한 사람인지 알 수가 없다.
        // 징표를 하나 만들어 주겠다. -> JWT를 발급해서 클라이언트에게 전달해 주겠다!
        String token
                = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());
        CommonResDto resDto
                = new CommonResDto(HttpStatus.OK,
                "Login Success", token);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 회원 정보 조회 (관리자 전용) -> ADMIN만 회원 전체 목록을 조회할 수 있다.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    // 컨트롤러 파라미터 Pageable 선언하면 페이징 파라미터 처리를 쉽게 할 수 있음.
    // /list?number=1&size=10&sort=name,desc 요런 식으로.
    // 요청 시 쿼리스트링이 전달되지 않으면 기본값 0, 20, unsorted
    public ResponseEntity<?> getUserList(Pageable pageable) {
        List<UserResDto> dtoList = userService.userList(pageable);
        CommonResDto resDto
                = new CommonResDto(HttpStatus.OK, "userList 조회 성공", dtoList);

        return ResponseEntity.ok().body(resDto);
    }

    // 회원 정보 조회 (마이페이지) -> 로그인 한 회원만이 요청할 수 있습니다.
    // 일반 회원용 정보 조회
    @GetMapping("/myInfo")
    public ResponseEntity<?> getMyInfo() {
        UserResDto dto = userService.myInfo();
        CommonResDto resDto
                = new CommonResDto(HttpStatus.OK, "myInfo 조회 성공", dto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }


}







