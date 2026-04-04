package com.tasklink.repository;

import com.tasklink.model.Proposal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByOrderId(Long orderId);

    @EntityGraph(attributePaths = {"order", "freelancer"})
    List<Proposal> findWithOrderAndFreelancerByOrderId(Long orderId);

    @EntityGraph(attributePaths = {"order", "freelancer"})
    Optional<Proposal> findWithOrderAndFreelancerById(Long id);

    @Query("select p.id, attachment from Proposal p join p.attachments attachment where p.id in :proposalIds")
    List<Object[]> findAttachmentRowsByProposalIds(@Param("proposalIds") List<Long> proposalIds);

    @Query("select p.id, ability from Proposal p join p.abilities ability where p.id in :proposalIds")
    List<Object[]> findAbilityRowsByProposalIds(@Param("proposalIds") List<Long> proposalIds);

    @EntityGraph(attributePaths = {"order", "freelancer"})
    List<Proposal> findByOrderIdIn(List<Long> orderIds);
}
