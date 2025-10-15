package com.example.h2_practice.controller;

import com.example.h2_practice.dto.UserDto;
import com.example.h2_practice.entity.User;
import com.example.h2_practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자 생성
    @PostMapping
    public User createUser(@RequestBody UserDto userDto){
        // return userService.createUser(userDto);
        try {
            return userService.createUserWithRollback(userDto);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("사용자 생성 중 오류 발생", e);
        }

    }

    // 사용자 조회
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    // 사용자 정보 업데이트
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UserDto userDto){
        return userService.updateUser(id, userDto);
    }
}
