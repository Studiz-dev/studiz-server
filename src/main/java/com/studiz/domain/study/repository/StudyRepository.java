package com.studiz.domain.study.repository;

import com.studiz.domain.study.entity.Study;
import com.studiz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudyRepository extends JpaRepository<Study, UUID> {

    Optional<Study> findByInviteCode(String inviteCode);

    List<Study> findByOwner(User owner);
}
