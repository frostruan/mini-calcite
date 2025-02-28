package com.huiruan.calcite.optimizer.operator.pattern;

import com.huiruan.calcite.optimizer.operator.OperatorType;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class Pattern {
    private final OperatorType opType;
    private final List<Pattern> children;

    protected Pattern(OperatorType opType) {
        this.opType = opType;
        this.children = Lists.newArrayList();
    }

    public OperatorType getOpType() {
        return opType;
    }

    public static Pattern create(OperatorType type, OperatorType... children) {
        Pattern p = new Pattern(type);
        for (OperatorType child : children) {
            p.addChildren(new Pattern(child));
        }
        return p;
    }

    public List<Pattern> children() {
        return children;
    }

    public Pattern childAt(int i) {
        return children.get(i);
    }

    public Pattern addChildren(Pattern... children) {
        this.children.addAll(Arrays.asList(children));
        return this;
    }
}
