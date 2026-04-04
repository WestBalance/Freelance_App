package com.tasklink.service;

import com.tasklink.dto.FreelancerProfileRequest;
import com.tasklink.model.FreelancerProfile;
import com.tasklink.model.Role;
import com.tasklink.model.Skill;
import com.tasklink.model.UserAccount;
import com.tasklink.patterns.creational.*;
import com.tasklink.patterns.structural.PortfolioProxy;
import com.tasklink.patterns.structural.PortfolioView;
import com.tasklink.patterns.structural.SkillComposite;
import com.tasklink.patterns.structural.SkillLeaf;
import com.tasklink.repository.FreelancerProfileRepository;
import com.tasklink.repository.SkillRepository;
import com.tasklink.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    private final FreelancerProfileRepository profileRepo;
    private final SkillRepository skillRepo;
    private final UserAccountRepository userRepo;
    private final TaskLinkProfileFactory profileFactoryProvider = new TaskLinkProfileFactory();

    public ProfileService(FreelancerProfileRepository profileRepo, SkillRepository skillRepo, UserAccountRepository userRepo) {
        this.profileRepo = profileRepo;
        this.skillRepo = skillRepo;
        this.userRepo = userRepo;
    }

    public FreelancerProfile upsert(FreelancerProfileRequest request) {
        FreelancerProfile profile = getOrCreateFreelancerProfile(request.userId());
        List<Long> skillIds = request.skillIds() == null ? List.of() : request.skillIds();
        List<Skill> skills = skillRepo.findAllById(skillIds);

        SkillComposite root = new SkillComposite("root");
        skills.stream().map(s -> new SkillLeaf(s.getName())).forEach(root::add);
        List<String> normalizedSkillNames = root.flatten().stream().map(String::toLowerCase).distinct().toList();
        List<Skill> normalizedSkills = skillRepo.findByNameLowerIn(normalizedSkillNames);

        ProfileBuilder builder = new FreelancerProfileBuilder(profile);
        FreelancerProfileDirector director = new FreelancerProfileDirector();
        director.construct(builder, request, normalizedSkills);

        profileRepo.save(builder.getResult());
        return getByUserIdWithSkills(request.userId());
    }

    public FreelancerProfile getByUserId(Long userId) {
        return getOrCreateFreelancerProfile(userId);
    }

    public FreelancerProfile getByUserIdWithSkills(Long userId) {
        return profileRepo.findWithSkillsByUserId(userId).orElseGet(() -> getOrCreateFreelancerProfile(userId));
    }

    public List<String> lazyPortfolio(Long userId) {
        PortfolioView portfolioView = new PortfolioProxy(() -> profileRepo.findPortfolioLinksByUserId(userId));
        return List.copyOf(portfolioView.getLinks());
    }

    public List<String> reviews(Long userId) {
        return List.copyOf(profileRepo.findReviewsByUserId(userId));
    }

    private FreelancerProfile getOrCreateFreelancerProfile(Long userId) {
        return profileRepo.findByUserId(userId).orElseGet(() -> {
            UserAccount user = userRepo.findById(userId).orElseThrow();
            if (user.getRole() != Role.FREELANCER) {
                throw new IllegalArgumentException("Profile is available only for freelancers");
            }
            ProfileAbstractFactory factory = profileFactoryProvider.getFactory(Role.FREELANCER);
            return profileRepo.save((FreelancerProfile) factory.createProductB().toProfile(user));
        });
    }
}
