package com.playdata.orderservice.product.service;

import com.playdata.orderservice.product.dto.ProductResDto;
import com.playdata.orderservice.product.dto.ProductSaveReqDto;
import com.playdata.orderservice.product.dto.ProductSearchDto;
import com.playdata.orderservice.product.entity.Product;
import com.playdata.orderservice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Product productCreate(ProductSaveReqDto dto) {
        // 원본 이미지를 어딘가에 저장하고, 그 저장된 위치를 Entity에 세팅하자.
        MultipartFile productImage = dto.getProductImage();

        // 상품을 등록하는 과정에서, 이미지 이름의 충돌이 발생할 수 있기 때문에
        // 랜덤한 문자열을 섞어서 파일 중복을 막아주자.
        String uniqueFileName
                = UUID.randomUUID() + "_" + productImage.getOriginalFilename();

        // 특정 로컬 경로에 이미지를 전송하고, 그 경로를 Entity에 세팅하자.
        File file
                = new File("/Users/stephen/Desktop/playdata_8th_develop/upload/" + uniqueFileName);

        try {
            productImage.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패!");
        }

        Product product = dto.toEntity();
        product.setImagePath(uniqueFileName);

        return productRepository.save(product);

    }

    public List<ProductResDto> productList(ProductSearchDto dto, Pageable pageable) {
        Page<Product> products;
        if (dto.getCategory() == null) {
            products = productRepository.findAll(pageable);
        } else if (dto.getCategory().equals("name")) {
            products = productRepository.findByNameValue(dto.getSearchName(), pageable);
        } else {
            products = productRepository.findByCategoryValue(dto.getSearchName(), pageable);
        }

        List<Product> productList = products.getContent();

        return productList.stream()
                .map(Product::fromEntity)
                .collect(Collectors.toList());
    }
}








