package com.studiz.domain.user.service;

import com.studiz.domain.auth.dto.LoginRequest;
import com.studiz.domain.auth.dto.LoginResponse;
import com.studiz.domain.auth.dto.RegisterRequest;
import com.studiz.domain.auth.dto.RegisterResponse;
import com.studiz.domain.user.dto.UserProfileResponse;
import com.studiz.domain.user.entity.User;
import com.studiz.domain.user.repository.UserRepository;
import com.studiz.global.exception.DuplicateLoginIdException;
import com.studiz.global.exception.InvalidPasswordException;
import com.studiz.global.exception.UserNotFoundException;
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
                .profileImageUrl(request.getProfileImageUrl())
                .build();

        User savedUser = userRepository.save(user);
        log.info("회원가입 완료: userId={}, loginId={}", savedUser.getId(), savedUser.getLoginId());

        return RegisterResponse.from(savedUser);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("로그인 시도: loginId={}", request.getLoginId());

        // 사용자 조회
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 아이디로 로그인 시도: {}", request.getLoginId());
                    return new UserNotFoundException();
                });

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("잘못된 비밀번호로 로그인 시도: loginId={}", request.getLoginId());
            throw new InvalidPasswordException();
        }

        log.info("로그인 성공: userId={}, loginId={}", user.getId(), user.getLoginId());
        return LoginResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(User user) {
        return UserProfileResponse.from(user);
    }

    @Transactional
    public UserProfileResponse updateProfile(User user, String name) {
        user.updateName(name);
        log.info("프로필 수정: userId={}, newName={}", user.getId(), name);
        return UserProfileResponse.from(user);
    }

    @Transactional
    public UserProfileResponse updateProfile(User user, String name, String profileImageUrl) {
        user.updateName(name);
        // profileImageUrl이 null이 아니고 빈 문자열이 아닐 때만 업데이트
        if (profileImageUrl != null && !profileImageUrl.isBlank()) {
            user.updateProfileImageUrl(profileImageUrl);
        } else if (profileImageUrl != null && profileImageUrl.isBlank()) {
            // 빈 문자열이면 null로 설정 (프로필 이미지 제거)
            user.updateProfileImageUrl(null);
        }
        log.info("프로필 수정: userId={}, newName={}, profileImageUrl={}", user.getId(), name, profileImageUrl);
        return UserProfileResponse.from(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 사용자 ID: {}", id);
                    return new UserNotFoundException();
                });
    }
}

