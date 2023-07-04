package com.fastcampus.programming.dmaker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Data
@Builder
@Slf4j
public class DevDto {
    @NonNull
    String name;
    Integer age;
    LocalDateTime startAt;

    public void printLog(){
        log.info(getName().toUpperCase());
    }
}
