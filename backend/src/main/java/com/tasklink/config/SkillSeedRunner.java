package com.tasklink.config;

import com.tasklink.model.Skill;
import com.tasklink.repository.SkillRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class SkillSeedRunner {

    @Bean
    CommandLineRunner seedSkills(SkillRepository skillRepository) {
        return args -> {
            List<Skill> defaults = List.of(
                    new Skill(null, "Java", "Development"),
                    new Skill(null, "Spring", "Development"),
                    new Skill(null, "React", "Development"),
                    new Skill(null, "TypeScript", "Development"),
                    new Skill(null, "SQL", "Development"),
                    new Skill(null, "Docker", "DevOps"),
                    new Skill(null, "AWS", "DevOps"),
                    new Skill(null, "UI/UX", "Design"),
                    new Skill(null, "Figma", "Design"),
                    new Skill(null, "SEO", "Marketing"),
                    new Skill(null, "Copywriting", "Writing"),
                    new Skill(null, "Video Editing", "Video"),
                    new Skill(null, "Python", "Development"),
                    new Skill(null, "QA Testing", "Development"),
                    new Skill(null, "Prompt Engineering", "Development")
            );

            Set<String> existingNames = skillRepository.findAll().stream()
                    .map(Skill::getName)
                    .collect(Collectors.toSet());

            List<Skill> missing = defaults.stream()
                    .filter(skill -> !existingNames.contains(skill.getName()))
                    .toList();

            if (!missing.isEmpty()) {
                skillRepository.saveAll(missing);
            }
        };
    }
}
