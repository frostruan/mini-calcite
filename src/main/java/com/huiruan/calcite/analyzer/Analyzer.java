package com.huiruan.calcite.analyzer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.huiruan.calcite.catalog.Catalog;
import com.huiruan.calcite.catalog.Database;
import com.huiruan.calcite.catalog.Field;
import com.huiruan.calcite.catalog.Table;
import com.huiruan.calcite.expression.*;
import com.huiruan.calcite.parser.SQLNode;
import com.huiruan.calcite.parser.SQLNode.SelectStmt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Analyzer {

    private Analyzer() {}

    public static void analyze(SelectStmt selectStmt, Catalog catalog) {
        Analyzer analyzer = new Analyzer();
        analyzer.transformSelectStmt(selectStmt, null);
    }

    public void transformSelectStmt(SelectStmt stmt, Scope parent) {
        Scope sourceScope = analyzeFrom(stmt, parent);
        sourceScope.setParent(parent);

        analyzeWhere(stmt, sourceScope);

        List<Expression> outputExpressions = analyzeSelect(stmt, sourceScope);

        Scope outputScope = computeAndAssignOutputScope(stmt, sourceScope);

        List<Expression> groupByExpressions = analyzeGroupBy(stmt, sourceScope, outputScope, outputExpressions);
        if (stmt.isDistinct()) {
            groupByExpressions.addAll(outputExpressions);
        }

        analyzeHaving(stmt, sourceScope, outputScope, outputExpressions);

        Scope sourceAndOutputScope = computeAndAssignOrderScope(sourceScope, outputScope);

        List<SQLNode.OrderByItem> orderByElements = analyzeOrderBy(stmt, sourceAndOutputScope, outputExpressions);
        List<Expression> orderByExpressions =
                orderByElements.stream().map(SQLNode.OrderByItem::getExpression).collect(Collectors.toList());

        List<FunctionCall> aggregates = analyzeAggregations(sourceScope, orderByExpressions);

        analyzeLimitClause(stmt);
    }

    private Scope analyzeFrom(SelectStmt stmt, Scope parent) { return null; }

    private void analyzeWhere(SelectStmt stmt, Scope scope) {}

    private List<Expression> analyzeSelect(SelectStmt selectStmt, Scope scope) {
        return Lists.newArrayList();
    }

    private Scope computeAndAssignOutputScope(SelectStmt stmt, Scope scope) {
        return null;
    }

    private List<Expression> analyzeGroupBy(SelectStmt node, Scope sourceScope, Scope outputScope,
                                            List<Expression> outputExpressions) {
        return Lists.newArrayList();
    }

    private void analyzeHaving(SelectStmt node, Scope sourceScope,
                               Scope outputScope, List<Expression> outputExpressions) {}

    private Scope computeAndAssignOrderScope(Scope sourceScope, Scope outputScope) {return null;}

    private List<SQLNode.OrderByItem> analyzeOrderBy(SelectStmt node, Scope orderByScope,
                                                     List<Expression> outputExpressions) {
        return Lists.newArrayList();
    }

    private List<FunctionCall> analyzeAggregations(Scope sourceScope, List<Expression> outputAndOrderByExpressions) {
        return Lists.newArrayList();
    }

    private void analyzeLimitClause(SelectStmt stmt) {
    }

    public static final class AnalysisException extends RuntimeException {
        public AnalysisException(String msg) {
            super(msg);
        }
    }
}
