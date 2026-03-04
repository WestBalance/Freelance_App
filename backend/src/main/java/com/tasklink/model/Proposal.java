package com.tasklink.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TaskOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserAccount freelancer;

    private BigDecimal price;

    @Column(length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    @ElementCollection
    @CollectionTable(name = "proposal_attachment", joinColumns = @JoinColumn(name = "proposal_id"))
    @Column(name = "attachment_link")
    @Builder.Default
    private List<String> attachments = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "proposal_ability", joinColumns = @JoinColumn(name = "proposal_id"))
    @Column(name = "ability")
    @Builder.Default
    private List<String> abilities = new ArrayList<>();
}
