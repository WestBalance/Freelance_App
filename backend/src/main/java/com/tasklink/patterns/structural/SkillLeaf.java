package com.tasklink.patterns.structural;

import java.util.List;

public record SkillLeaf(String name) implements SkillNode {
    @Override
    public List<String> flatten() {
        return List.of(name);
    }
}
