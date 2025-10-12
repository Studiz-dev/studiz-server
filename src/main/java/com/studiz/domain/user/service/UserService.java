package com.studiz.domain.user.service;

import com.studiz.domain.auth.dto.RegisterRequest;
import com.studiz.domain.auth.dto.RegisterResponse;
import com.studiz.domain.user.entity.User;
import com.studiz.domain.user.repository.UserRepository;
import com.studiz.global.exception.DuplicateLoginIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        log.info("회원가입 시도: loginId={}", request.getLoginId());

        // 아이디 중복 확인
        if (userRepository.existsByLoginId(request.getLoginId())) {
            log.warn("중복된 아이디로 회원가입 시도: {}", request.getLoginId());
            throw new DuplicateLoginIdException();
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // User 엔티티 생성 및 저장
        User user = User.builder()
                .loginId(request.getLoginId())
                .password(encodedPassword)
                .name(request.getName())
                .email(request.getEmail())
                .build();

        User savedUser = userRepository.save(user);
        log.info("회원가입 완료: userId={}, loginId={}", savedUser.getId(), savedUser.getLoginId());

        return RegisterResponse.from(savedUser);
    }
}

