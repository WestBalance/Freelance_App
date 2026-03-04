package com.tasklink.repository;

import com.tasklink.model.FreelancerProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {
    Optional<FreelancerProfile> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "skills"})
    Optional<FreelancerProfile> findWithSkillsByUserId(Long userId);

    @Query("select link from FreelancerProfile p join p.portfolioLinks link where p.user.id = :userId")
    List<String> findPortfolioLinksByUserId(@Param("userId") Long userId);

    @Query("select review from FreelancerProfile p join p.reviews review where p.user.id = :userId")
    List<String> findReviewsByUserId(@Param("userId") Long userId);
}
