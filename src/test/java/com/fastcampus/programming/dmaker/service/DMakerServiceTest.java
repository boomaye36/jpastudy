package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.code.StatusCode;
import com.fastcampus.programming.dmaker.constant.DMakerConstant;
import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.fastcampus.programming.dmaker.type.DeveloperLevel.SENIOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {
    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @InjectMocks
    private DMakerService dMakerService;

    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(SENIOR)
            .developerSkillType(DeveloperSkillType.FRONT_END)
            .experienceYears(12)
            .statusCode(StatusCode.EMPLOYED)
            .name("name")
            .age(12)
            .build();

    private CreateDeveloper.Request getCreateRequest(
            DeveloperLevel developerLevel,
            DeveloperSkillType developerSkillType,
            Integer experienceYears
    ) {
        return CreateDeveloper.Request.builder()
                .developerLevel(developerLevel)
                .developerSkillType(developerSkillType)
                .experienceYears(experienceYears)
                .name("name")
                .age(32)
                .memberId("memberId")
                .build();
    }
    @Test
    public void testSomething(){
//        dMakerService.createDeveloper(CreateDeveloper.Request.builder()
//                .developerLevel(DeveloperLevel.SENIOR)
//                .developerSkillType(DeveloperSkillType.FRONT_END)
//                .experienceYears(12)
//                .memberId("memberId")
//                .name("name")
//                .age(32)
//                .build());
//
//        List<DeveloperDto> allEmployedDevelopers = dMakerService.getAllEmployedDevelopers();
//        System.out.println(allEmployedDevelopers);

        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));
        DeveloperDetailDto developerDetailDto = dMakerService.getDeveloperDetail("memberId");

        assertEquals(SENIOR, developerDetailDto.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, developerDetailDto.getDeveloperSkillType());
        assertEquals(12, developerDetailDto.getExperienceYears());
    }

    @Test
    void createDevelopertest_success() {
        //given

        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        given(developerRepository.save(any()))
                .willReturn(defaultDeveloper);
        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        CreateDeveloper.Response developer =
                dMakerService.createDeveloper(
                        getCreateRequest(
                                SENIOR, DeveloperSkillType.FRONT_END, 12));

        verify(developerRepository, times(1))
                .save(captor.capture());
        Developer savedDeveloper = captor.getValue();
        assertEquals(SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(32, savedDeveloper.getAge());
    }
    @Test
    void createDevelopertest_fail_low_senior() {
        //given


        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(getCreateRequest(SENIOR, DeveloperSkillType.FRONT_END, DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS-1)));

        assertEquals(DMakerErrorCode.LEVEL_EXPERIIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());
    }
    @Test
    void createDevelopertest_failed_with_duplicated() {
        //given

        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));
        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(getCreateRequest(SENIOR, DeveloperSkillType.FRONT_END, 13)));

        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }
}