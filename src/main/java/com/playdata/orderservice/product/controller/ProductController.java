package com.playdata.orderservice.product.controller;

import com.playdata.orderservice.common.dto.CommonResDto;
import com.playdata.orderservice.product.dto.ProductResDto;
import com.playdata.orderservice.product.dto.ProductSaveReqDto;
import com.playdata.orderservice.product.dto.ProductSearchDto;
import com.playdata.orderservice.product.entity.Product;
import com.playdata.orderservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    // 상품 등록 요청
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(ProductSaveReqDto dto) {
        /*
        상품 등록 요청은 여러 데이터와 함께 이미지가 전달될 것입니다.
        1. JS의 formData 객체를 통해 모든 데이터를 전달 (JSON 형태가 아니라, multipart/form-data 형식)
        2. JSON 형태로 전달 (이미지를 Base64 인코딩을 통해 문자열로 변환해서 전달)

        formData로 넘어오는 이미지 파일은 MultipartFile 형태로 받아주시면 됩니다.
        MultipartFile은 이미지의 정보(크기, 원본이름...), 지정된 경로로 파일 전송 기능을 제공합니다.
         */
        log.info("dto: {}", dto);
        Product product = productService.productCreate(dto);

        CommonResDto resDto
                = new CommonResDto(HttpStatus.CREATED, "상품 등록 성공", product.getId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    // 요청방식: GET, 요청 URL: /product/list
    // 따로 권한은 필요 없습니다. (누구나 요청이 가능합니다. 로그인 안해도 됩니다.)
    // 페이징이 필요합니다. -> 클라이언트 쪽에서 페이지 번호와 한 화면에 보여질 상품 개수, 정렬 방식이 넘어와요.
    // 리턴은 ProductResDto 형태로 리턴됩니다.
    // ProductResDto(id, name, category, price, stockQuantity, imagePath)
    @GetMapping("/list")
    public ResponseEntity<?> listProduct(ProductSearchDto dto, Pageable pageable) {
        // 페이지 번호를 number로 주시면 안됨! page로 전달해 주셔야 합니다!
        // 사용자가 선택한 페이지 번호 -1을 클라이언트 단에서 해서 전달해 주셔야 합니다.
        log.info("/product/list: GET, pageable: {}", pageable);
        log.info("dto: {}", dto);
        List<ProductResDto> dtoList = productService.productList(dto, pageable);

        CommonResDto resDto
                = new CommonResDto(HttpStatus.OK, "상품 리스트 정상 조회", dtoList);

        return ResponseEntity.ok().body(resDto);
    }

}









