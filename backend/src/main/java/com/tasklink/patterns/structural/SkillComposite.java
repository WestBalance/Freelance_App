package com.tasklink.patterns.structural;

import java.util.ArrayList;
import java.util.List;

public class SkillComposite implements SkillNode {
    private final String name;
    private final List<SkillNode> children = new ArrayList<>();

    public SkillComposite(String name) { this.name = name; }

    public SkillComposite add(SkillNode node) {
        children.add(node);
        return this;
    }

    @Override
    public String name() { return name; }

    @Override
    public List<String> flatten() {
        return children.stream().flatMap(c -> c.flatten().stream()).toList();
    }
}
