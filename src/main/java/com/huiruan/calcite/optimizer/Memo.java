package com.huiruan.calcite.optimizer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class Memo {
    private int nextGroupId = 0;

    @Getter
    private final List<Group> groups;

    @Getter
    private Group rootGroup;

    @Getter
    private final Map<GroupExpr, GroupExpr> groupExprs;

    public Memo() {
        groups = Lists.newLinkedList();
        groupExprs = Maps.newHashMap();
    }


}
