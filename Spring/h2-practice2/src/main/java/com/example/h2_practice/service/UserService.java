package com.example.h2_practice.service;

import com.example.h2_practice.dto.UserDto;
import com.example.h2_practice.entity.User;
import com.example.h2_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 사용자 생성 매서드에 트랜잭션 적용
    @Transactional
    public User createUser(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return userRepository.save(user);
    }

    // 특정 조건에서 트랜잭션을 롤백하는 예제 : 예외 발생하면 롤백
    @Transactional(rollbackFor = Exception.class)
    public User createUserWithRollback(UserDto userDto) throws Exception{
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);

        // 강제로 예외 발생
        if(userDto.getEmail().contains("error")){
            throw new Exception("강제 예외 발생");
        }

        return user;
    }

    // 사용자 정보 조회 (id로 정보 조회)
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션 선언
    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    // 사용자 정보 수정 : 전파 및 격리 수준 설정
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public User  updateUser(Long id, UserDto userDto){
        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return userRepository.save(user);
    }

}
