package com.huiruan.calcite.optimizer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;

public class Group {
    private final int id;

    private final List<GroupExpr> logicalExprs;
    private final List<GroupExpr> physicalExprs;

    public Group(int groupId) {
        this.id = groupId;
        logicalExprs = Lists.newArrayList();
        physicalExprs = Lists.newArrayList();
    }
}
