package com.tasklink.repository;

import com.tasklink.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    @Query("select s from Skill s where lower(s.name) in :names")
    List<Skill> findByNameLowerIn(@Param("names") Collection<String> names);
}
