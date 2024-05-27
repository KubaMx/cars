package com.app.cars.controller;

import com.app.cars.service.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppController {
    @GetMapping("/user")
    public ResponseDto<String> user() {
        return new ResponseDto<>("USER", null);
    }

    @GetMapping("/admin")
    public ResponseDto<String> admin() {
        return new ResponseDto<>("ADMIN", null);
    }



}
