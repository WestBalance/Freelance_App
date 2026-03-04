package com.tasklink.controller;

import com.tasklink.model.Skill;
import com.tasklink.repository.SkillRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {
    private final SkillRepository repository;

    public SkillController(SkillRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Skill> all() { return repository.findAll(); }
}
