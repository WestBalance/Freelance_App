package com.tasklink.controller;

import com.tasklink.dto.ClientProfileDto;
import com.tasklink.dto.FreelancerProfileDto;
import com.tasklink.dto.FreelancerProfileRequest;
import com.tasklink.service.ClientProfileService;
import com.tasklink.service.Mapper;
import com.tasklink.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService service;
    private final ClientProfileService clientProfileService;

    public ProfileController(ProfileService service, ClientProfileService clientProfileService) {
        this.service = service;
        this.clientProfileService = clientProfileService;
    }

    @PutMapping("/freelancer")
    public FreelancerProfileDto upsert(@Valid @RequestBody FreelancerProfileRequest request) {
        var profile = service.upsert(request);
        return Mapper.toDto(profile, service.lazyPortfolio(request.userId()), service.reviews(request.userId()));
    }

    @GetMapping("/freelancer/{userId}")
    public FreelancerProfileDto get(@PathVariable Long userId) {
        var profile = service.getByUserIdWithSkills(userId);
        return Mapper.toDto(profile, service.lazyPortfolio(userId), service.reviews(userId));
    }

    @GetMapping("/freelancer/{userId}/portfolio")
    public List<String> portfolio(@PathVariable Long userId) {
        return service.lazyPortfolio(userId);
    }

    @GetMapping("/client/{userId}")
    public ClientProfileDto clientProfile(@PathVariable Long userId) {
        return clientProfileService.get(userId);
    }
}
