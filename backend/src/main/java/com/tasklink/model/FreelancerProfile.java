package com.tasklink.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreelancerProfile implements UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private UserAccount user;

    @Column(length = 1000)
    private String about;

    private Double rating;

    @ManyToMany
    @JoinTable(name = "freelancer_skill",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @Builder.Default
    private List<Skill> skills = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "freelancer_portfolio", joinColumns = @JoinColumn(name = "freelancer_id"))
    @Column(name = "portfolio_link")
    @Builder.Default
    private List<String> portfolioLinks = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "freelancer_review", joinColumns = @JoinColumn(name = "freelancer_id"))
    @Column(name = "review_text")
    @Builder.Default
    private List<String> reviews = new ArrayList<>();
}
