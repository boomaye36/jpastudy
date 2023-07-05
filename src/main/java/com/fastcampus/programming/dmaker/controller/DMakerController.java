package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.*;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.service.DMakerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {

    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("Get /developers HTTP/1.1");
        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developers/{memberId}")
    public DeveloperDetailDto getAllDeveloperDetail(
            @PathVariable final String memberId
    ) {
        log.info("Get /developers HTTP/1.1");
        return dMakerService.getDeveloperDetail(memberId);
    }

    @PutMapping("/developers/{memberId}")
    public DeveloperDetailDto editDeveloper(
            @PathVariable final String memberId,
            @Valid @RequestBody EditDeveloper.Request request

    ) {
        log.info("Get /developers HTTP/1.1");
        return dMakerService.editDeveloper(memberId, request);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(
            @Valid @RequestBody CreateDeveloper.Request request) {
        log.info("request : {}", request);
        //dMakerService.createDeveloper(request);

        return dMakerService.createDeveloper(request);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDetailDto deleteDeveloper(
            @PathVariable final String memberId
    ){
        return dMakerService.deleteDeveloper(memberId);
    }


}
