package com.tasklink.service;

import com.tasklink.dto.CreateProposalRequest;
import com.tasklink.model.*;
import com.tasklink.patterns.behavioral.OrderEvent;
import com.tasklink.patterns.behavioral.OrderSubject;
import com.tasklink.patterns.behavioral.RuntimeOrderObserver;
import com.tasklink.patterns.creational.singleton.PlatformRuntimeManager;
import com.tasklink.patterns.structural.SkillComposite;
import com.tasklink.patterns.structural.SkillLeaf;
import com.tasklink.repository.FreelancerProfileRepository;
import com.tasklink.repository.ProposalRepository;
import com.tasklink.repository.TaskOrderRepository;
import com.tasklink.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProposalService {
    private static final Logger log = LoggerFactory.getLogger(ProposalService.class);
    private final ProposalRepository proposalRepo;
    private final TaskOrderRepository orderRepo;
    private final UserAccountRepository userRepo;
    private final FreelancerProfileRepository profileRepo;
    private final PlatformRuntimeManager runtimeManager = PlatformRuntimeManager.getInstance();
    private final OrderSubject orderSubject = new OrderSubject();

    public ProposalService(ProposalRepository proposalRepo, TaskOrderRepository orderRepo, UserAccountRepository userRepo, FreelancerProfileRepository profileRepo) {
        this.proposalRepo = proposalRepo;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
        orderSubject.attach(new RuntimeOrderObserver(runtimeManager));
    }

    public Proposal create(CreateProposalRequest request) {
        TaskOrder order = orderRepo.findById(request.orderId()).orElseThrow();
        if (order.getStatus() != OrderStatus.OPEN) {
            log.warn("Proposal rejected: order {} has status {}", order.getId(), order.getStatus());
            throw new IllegalStateException("Cannot apply to non-open order");
        }
        UserAccount freelancer = userRepo.findById(request.freelancerId()).orElseThrow();
        FreelancerProfile profile = profileRepo.findWithSkillsByUserId(freelancer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Freelancer profile not found"));

        SkillComposite abilityTree = new SkillComposite("abilities");
        profile.getSkills().stream()
                .map(Skill::getName)
                .distinct()
                .sorted()
                .map(SkillLeaf::new)
                .forEach(abilityTree::add);

        Proposal p = Proposal.builder()
                .order(order)
                .freelancer(freelancer)
                .price(request.price())
                .message(request.message())
                .status(ProposalStatus.PENDING)
                .attachments(request.attachments() == null ? List.of() : request.attachments())
                .abilities(abilityTree.flatten())
                .build();
        Proposal saved = proposalRepo.save(p);
        Proposal detailed = proposalRepo.findWithOrderAndFreelancerById(saved.getId()).orElse(saved);
        hydrateCollections(List.of(detailed));
        log.info("Proposal created: proposalId={}, orderId={}, freelancerId={}", detailed.getId(), order.getId(), freelancer.getId());
        return detailed;
    }

    public List<Proposal> listByOrder(Long orderId) {
        List<Proposal> proposals = proposalRepo.findWithOrderAndFreelancerByOrderId(orderId);
        hydrateCollections(proposals);
        return proposals;
    }

    public void accept(Long orderId, Long proposalId) {
        TaskOrder order = orderRepo.findById(orderId).orElseThrow();
        accept(orderId, proposalId, order.getClient().getId());
    }

    @Transactional
    public void accept(Long orderId, Long proposalId, Long clientId) {
        TaskOrder order = orderRepo.findById(orderId).orElseThrow();
        if (!order.getClient().getId().equals(clientId)) {
            throw new IllegalArgumentException("Only order owner can accept proposal");
        }
        Proposal proposal = proposalRepo.findById(proposalId).orElseThrow();
        if (!proposal.getOrder().getId().equals(orderId)) {
            throw new IllegalArgumentException("Proposal does not belong to order");
        }

        List<Proposal> orderProposals = proposalRepo.findByOrderId(orderId);
        for (Proposal current : orderProposals) {
            current.setStatus(current.getId().equals(proposalId) ? ProposalStatus.ACCEPTED : ProposalStatus.REJECTED);
        }
        proposalRepo.saveAll(orderProposals);

        OrderStatus previous = order.getStatus();
        order.setBudget(proposal.getPrice());
        order.setStatus(OrderStatus.IN_PROGRESS);
        orderRepo.save(order);
        orderSubject.notifyObservers(new OrderEvent(order.getId(), "PROPOSAL_ACCEPTED", previous, order.getStatus(), Instant.now()));
    }

    private void hydrateCollections(List<Proposal> proposals) {
        if (proposals.isEmpty()) {
            return;
        }

        List<Long> ids = proposals.stream().map(Proposal::getId).toList();
        Map<Long, List<String>> attachmentsByProposal = new HashMap<>();
        for (Object[] row : proposalRepo.findAttachmentRowsByProposalIds(ids)) {
            Long proposalId = (Long) row[0];
            String attachment = (String) row[1];
            attachmentsByProposal.computeIfAbsent(proposalId, key -> new ArrayList<>()).add(attachment);
        }

        Map<Long, List<String>> abilitiesByProposal = new HashMap<>();
        for (Object[] row : proposalRepo.findAbilityRowsByProposalIds(ids)) {
            Long proposalId = (Long) row[0];
            String ability = (String) row[1];
            abilitiesByProposal.computeIfAbsent(proposalId, key -> new ArrayList<>()).add(ability);
        }

        for (Proposal proposal : proposals) {
            proposal.setAttachments(attachmentsByProposal.getOrDefault(proposal.getId(), List.of()));
            proposal.setAbilities(abilitiesByProposal.getOrDefault(proposal.getId(), List.of()));
        }
    }
}
