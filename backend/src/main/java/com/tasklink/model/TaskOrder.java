package com.tasklink.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
@Table(name = "task_order")
public class TaskOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    private OrderCategory category;

    private BigDecimal budget;

    private String currency;

    private LocalDate deadline;

    private Double minRating;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private boolean urgent;

    private boolean featured;

    @Enumerated(EnumType.STRING)
    private PricingMode pricingMode;

    private Integer estimatedHours;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserAccount client;

    private Integer priorityScore;
}
