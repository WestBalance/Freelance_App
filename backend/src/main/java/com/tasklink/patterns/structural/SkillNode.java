package com.tasklink.patterns.structural;

import java.util.List;

public interface SkillNode {
    String name();
    default SkillNode add(SkillNode node) {
        throw new UnsupportedOperationException("add is not supported for leaf node");
    }
    default void remove(SkillNode node) {
        throw new UnsupportedOperationException("remove is not supported for leaf node");
    }
    default SkillNode getChild(int index) {
        throw new UnsupportedOperationException("getChild is not supported for leaf node");
    }
    List<String> flatten();
}
