package com.studiz.domain.schedule.service;

import com.studiz.domain.schedule.dto.ScheduleAvailabilityRequest;
import com.studiz.domain.schedule.dto.ScheduleAvailabilitySummaryResponse;
import com.studiz.domain.schedule.entity.Schedule;
import com.studiz.domain.schedule.entity.ScheduleAvailability;
import com.studiz.domain.schedule.entity.ScheduleSlot;
import com.studiz.domain.schedule.exception.ScheduleNotFoundException;
import com.studiz.domain.schedule.repository.ScheduleAvailabilityRepository;
import com.studiz.domain.schedule.repository.ScheduleRepository;
import com.studiz.domain.schedule.repository.ScheduleSlotRepository;
import com.studiz.domain.study.entity.Study;
import com.studiz.domain.study.service.StudyService;
import com.studiz.domain.studymember.service.StudyMemberService;
import com.studiz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleAvailabilityService {

    private final ScheduleAvailabilityRepository availabilityRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleSlotRepository slotRepository;
    private final StudyService studyService;
    private final StudyMemberService studyMemberService;

    public void updateAvailability(UUID studyId, UUID scheduleId, ScheduleAvailabilityRequest request, User user) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureMember(study, user);

        Schedule schedule = scheduleRepository.findByIdAndStudy(scheduleId, study)
                .orElseThrow(ScheduleNotFoundException::new);

        List<ScheduleSlot> allSlots = slotRepository.findBySchedule(schedule);
        Map<UUID, ScheduleSlot> slotMap = allSlots.stream()
                .collect(Collectors.toMap(ScheduleSlot::getId, Function.identity()));

        Set<UUID> availableSlotIds = request.getAvailableSlotIds().stream()
                .collect(Collectors.toSet());

        // 모든 슬롯에 대해 availability 업데이트
        for (ScheduleSlot slot : allSlots) {
            boolean isAvailable = availableSlotIds.contains(slot.getId());
            
            availabilityRepository.findBySlotAndUser(slot, user)
                    .ifPresentOrElse(
                            availability -> availability.updateAvailability(isAvailable),
                            () -> {
                                if (isAvailable) {
                                    ScheduleAvailability availability = ScheduleAvailability.create(slot, user, true);
                                    availabilityRepository.save(availability);
                                }
                            }
                    );
        }
    }

    @Transactional(readOnly = true)
    public ScheduleAvailabilitySummaryResponse getAvailabilitySummary(UUID studyId, UUID scheduleId, User user) {
        Study study = studyService.getStudy(studyId);
        studyMemberService.ensureMember(study, user);

        Schedule schedule = scheduleRepository.findByIdAndStudy(scheduleId, study)
                .orElseThrow(ScheduleNotFoundException::new);

        List<Object[]> results = availabilityRepository.countAvailableUsersBySlot(scheduleId);

        List<ScheduleAvailabilitySummaryResponse.SlotAvailability> slotAvailabilities = results.stream()
                .map(result -> ScheduleAvailabilitySummaryResponse.SlotAvailability.builder()
                        .slotId((UUID) result[0])
                        .availableCount(((Number) result[1]).intValue())
                        .build())
                .toList();

        return ScheduleAvailabilitySummaryResponse.builder()
                .slots(slotAvailabilities)
                .build();
    }
}

