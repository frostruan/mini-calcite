package com.huiruan.calcite.parser;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.huiruan.calcite.catalog.Type;
import com.huiruan.calcite.expression.CastExpression;
import com.huiruan.calcite.expression.Expression;
import com.huiruan.calcite.expression.FunctionCall;
import com.huiruan.calcite.expression.Identifier;
import com.huiruan.calcite.expression.Literal;
import com.huiruan.calcite.expression.Predicate;
import com.huiruan.calcite.parser.SQLNode.ColumnItem;
import com.huiruan.calcite.parser.SQLNode.JoinClause;
import com.huiruan.calcite.parser.SQLNode.JoinType;
import com.huiruan.calcite.parser.SQLNode.OrderByItem;
import com.huiruan.calcite.parser.SQLNode.SelectStmt;
import com.huiruan.calcite.parser.SQLNode.SortOrder;
import com.huiruan.calcite.parser.SQLNode.RelationClause;
import com.huiruan.calcite.parser.SQLNode.UnionType;
import com.huiruan.calcite.parser.SQLNode.TableReference;
import com.huiruan.calcite.parser.SQLNode.QueryStmt;
import com.huiruan.calcite.utils.Pair;

import com.huiruan.calcite.parser.generated.CalciteBaseVisitor;
import com.huiruan.calcite.parser.generated.CalciteParser;

public final class AstBuilder extends CalciteBaseVisitor<SQLNode> {

    AstBuilder() {}

    @Override
    public QueryStmt visitQuery(CalciteParser.QueryContext ctx) {
        List<CalciteParser.SelectStatementContext> selectContexts = ctx.selectStatement();
        SelectStmt selectStmt = visitSelectStatement(selectContexts.get(0));
        int limit = visitLimitClauseContext(ctx.limitClause());

        List<CalciteParser.SetOperatorContext> setOperatorContexts = ctx.setOperator();
        if (setOperatorContexts == null) {
            return new QueryStmt(selectStmt, limit);
        } else {
            List<Pair<UnionType, SelectStmt>> selectStmtList = Lists.newArrayList();
            for (int i = 0; i < setOperatorContexts.size(); i++) {
                UnionType unionType = visitSetOperatorContext(setOperatorContexts.get(i));
                SelectStmt nextSelectStmt = visitSelectStatement(selectContexts.get(i + 1));
                selectStmtList.add(Pair.create(unionType, nextSelectStmt));
            }
            return new QueryStmt(selectStmt, selectStmtList, limit);
        }
    }

    @Override
    public TableReference visitTableReference(CalciteParser.TableReferenceContext ctx) {
        Identifier.TableAlias tableAlias = null;
        if (ctx.tableAlias() != null) {
            tableAlias = visitTableAlias(ctx.tableAlias());
        }
        if (ctx.tableName() != null) {
            return TableReference.of(visitTableName(ctx.tableName()), tableAlias);
        } else {
            return TableReference.of(visitSubquery(ctx.subquery()), tableAlias);
        }
    }

    @Override
    public QueryStmt visitSubquery(CalciteParser.SubqueryContext ctx) {
        return visitQuery(ctx.query());
    }

    @Override
    public SelectStmt visitSelectStatement(CalciteParser.SelectStatementContext ctx) {
        boolean distinct =
                ctx.distinct() != null && ctx.distinct().DISTINCT() != null;

        List<ColumnItem> columnItemList =
                ctx.columnList() instanceof CalciteParser.AllColumnsContext ?
                        visitAllColumnsContext((CalciteParser.AllColumnsContext) ctx.columnList()) :
                        visitSpecificColumnsContext((CalciteParser.SpecificColumnsContext) ctx.columnList());

        RelationClause relationClause =
                visitRelationClause(ctx.relationClause());

        Predicate whereExpr = null;
        if (ctx.condition() != null) {
            whereExpr = visitCondition(ctx.condition());
        }

        List<Expression> groupByExprs = null;
        if (ctx.groupByItem() != null) {
            groupByExprs = ctx.groupByItem()
                    .stream().map(this::visitGroupByItem).collect(Collectors.toList());
        }

        Predicate havingCondition = null;
        if (ctx.havingCondition() != null) {
            havingCondition = visitHavingCondition(ctx.havingCondition());
        }

        List<OrderByItem> orderByItems = null;
        if (ctx.orderByItem() != null) {
            orderByItems = ctx.orderByItem()
                    .stream().map(this::visitOrderByItem).collect(Collectors.toList());
        }

        return new SelectStmt(distinct, columnItemList, relationClause,
                whereExpr, groupByExprs, havingCondition, orderByItems);
    }

    @Override
    public RelationClause visitRelationClause(CalciteParser.RelationClauseContext ctx) {
        TableReference tableRef = visitTableReference(ctx.tableReference());
        List<CalciteParser.JoinClauseContext> joinClauseContexts = ctx.joinClause();
        if (joinClauseContexts.isEmpty()) {
            return new RelationClause(tableRef);
        } else {
            List<JoinClause> joinClauses = joinClauseContexts.stream()
                    .map(this::visitJoinClause).collect(Collectors.toList());
            return new RelationClause(tableRef, joinClauses);
        }
    }

    private UnionType visitSetOperatorContext(CalciteParser.SetOperatorContext ctx) {
        if (ctx.ALL() != null) {
            return UnionType.UNION_ALL;
        } else {
            return UnionType.UNION;
        }
    }

    public List<ColumnItem> visitAllColumnsContext(CalciteParser.AllColumnsContext ctx) {
        return Collections.singletonList(new ColumnItem(new Literal(Expression.STAR)));
    }

    public List<ColumnItem> visitSpecificColumnsContext(CalciteParser.SpecificColumnsContext ctx) {
        return ctx.columnItem().stream().map(this::visitColumnItem).collect(Collectors.toList());
    }

    @Override
    public JoinClause visitJoinClause(CalciteParser.JoinClauseContext ctx) {
        JoinType joinType = parseJoinType(ctx);
        TableReference tableReference = visitTableReference(ctx.tableReference());
        Predicate condition = visitCondition(ctx.condition());
        return new JoinClause(joinType, tableReference, condition);
    }

    private JoinType parseJoinType(CalciteParser.JoinClauseContext ctx) {
        if (ctx.LEFT() != null) {
            return JoinType.LEFT;
        }
        if (ctx.RIGHT() != null) {
            return JoinType.RIGHT;
        }
        if (ctx.FULL() != null) {
            return JoinType.FULL;
        }
        return JoinType.INNER;
    }

    @Override
    public ColumnItem visitColumnItem(CalciteParser.ColumnItemContext ctx) {
        if (ctx.expression() != null) {
            return new ColumnItem(visitExpression(ctx.expression()), visitAlias(ctx.alias()));
        }
        if (ctx.getChildCount() == 1 && Expression.STAR.equals(ctx.getChild(0).getText())) {
            return new ColumnItem(new Identifier.ColumnName(Expression.STAR));
        }
        if (ctx.tableAlias() != null && ctx.getChildCount() == 3 && Expression.STAR.equals(ctx.getChild(0).getText())) {
            Identifier.TableAlias tableAlias = visitTableAlias(ctx.tableAlias());
            return new ColumnItem(new Identifier.ColumnName(tableAlias + ".*"), tableAlias);
        }
        return null;
    }

    @Override
    public Expression visitGroupByItem(CalciteParser.GroupByItemContext ctx) {
        if (ctx.expression() != null) {
            return visitExpression(ctx.expression());
        }
        return null;
    }

    @Override
    public OrderByItem visitOrderByItem(CalciteParser.OrderByItemContext ctx) {
        Expression expr = visitExpression(ctx.expression());
        if (ctx.ASC() != null || ctx.DESC() != null) {
            if (ctx.ASC() != null) {
                return new OrderByItem(expr, SortOrder.ASC);
            } else {
                return new OrderByItem(expr, SortOrder.DESC);
            }
        } else {
            return new OrderByItem(expr);
        }
    }

    @Override
    public Predicate visitHavingCondition(CalciteParser.HavingConditionContext ctx) {
        if (ctx.condition() != null) {
            return visitCondition(ctx.condition());
        }
        return null;
    }

    @Override
    public Predicate visitCondition(CalciteParser.ConditionContext ctx) {
        if (ctx.orCondition() != null) {
            return visitOrCondition(ctx.orCondition());
        }
        return null;
    }

    @Override
    public Predicate visitOrCondition(CalciteParser.OrConditionContext ctx) {
        List<CalciteParser.AndConditionContext> andConditionContexts = ctx.andCondition();
        if (andConditionContexts.size() == 1) {
            return visitAndCondition(andConditionContexts.get(0));
        }
        if (ctx.OR() != null) {
            List<Expression> inputs = andConditionContexts.stream()
                    .map(this::visitAndCondition)
                    .map(pn -> (Expression) pn)
                    .collect(Collectors.toList());
            return new Predicate.CompoundPredicate(Predicate.CompoundType.OR, inputs);
        }
        return null;
    }

    @Override
    public Predicate visitAndCondition(CalciteParser.AndConditionContext ctx) {
        List<CalciteParser.NotConditionContext> notConditionContexts = ctx.notCondition();
        if (notConditionContexts.size() == 1) {
            return visitNotCondition(notConditionContexts.get(0));
        }
        if (ctx.AND() != null) {
            List<Expression> inputs = notConditionContexts.stream()
                    .map(this::visitNotCondition)
                    .map(pn -> (Expression) pn)
                    .collect(Collectors.toList());
            return new Predicate.CompoundPredicate(Predicate.CompoundType.AND, inputs);
        }
        return null;
    }

    @Override
    public Predicate visitNotCondition(CalciteParser.NotConditionContext ctx) {
        Predicate comparisonCondition = visitComparisonCondition(ctx.comparisonCondition());
        if (ctx.NOT() != null) {
            return new Predicate.CompoundPredicate(Predicate.CompoundType.NOT, comparisonCondition);
        } else {
            return comparisonCondition;
        }
    }

    @Override
    public Predicate visitComparisonCondition(CalciteParser.ComparisonConditionContext ctx) {
        if (ctx.comparisonOperator() != null) {
            Predicate.BinaryType binaryType = visitComparisonOperatorContext(ctx.comparisonOperator());
            List<Expression> inputs = ctx.expression()
                    .stream().map(this::visitExpression).collect(Collectors.toList());
            return new Predicate.BinaryPredicate(binaryType, inputs);
        }
        if (ctx.IS() != null && ctx.NULL() != null) {
            Expression expr = visitExpression(ctx.expression(0));
            if (ctx.NOT() != null) {
                return new Predicate.NullPredicate(Predicate.NullType.IS_NOT_NULL, expr);
            } else {
                return new Predicate.NullPredicate(Predicate.NullType.IS_NULL, expr);
            }
        }
        if (ctx.IN() != null) {
            List<Expression> inputs = ctx.expression()
                    .stream().map(this::visitExpression).collect(Collectors.toList());
            if (ctx.NOT() != null) {
                return new Predicate.InPredicate(Predicate.InType.NOT_IN, inputs);
            } else {
                return new Predicate.InPredicate(Predicate.InType.IN, inputs);
            }
        }
        if (ctx.condition() != null) {
            return visitCondition(ctx.condition());
        }
        return null;
    }

    private Predicate.BinaryType visitComparisonOperatorContext(CalciteParser.ComparisonOperatorContext ctx) {
        if (ctx.EQ() != null) {
            return Predicate.BinaryType.EQ;
        }
        if (ctx.GT() != null) {
            return Predicate.BinaryType.GT;
        }
        if (ctx.LT() != null) {
            return Predicate.BinaryType.LT;
        }
        if (ctx.GTE() != null) {
            return Predicate.BinaryType.GE;
        }
        if (ctx.LTE() != null) {
            return Predicate.BinaryType.LE;
        }
        if (ctx.NEQ() != null) {
            return Predicate.BinaryType.NE;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression visitExpression(CalciteParser.ExpressionContext ctx) {
        if (ctx.literal() != null) {
            return visitLiteral(ctx.literal());
        }
        if (ctx.columnName() != null) {
            return visitColumnName(ctx.columnName());
        }
        if (ctx.functionCall() != null) {
            return visitFunctionCall(ctx.functionCall());
        }
        return null;
    }

    @Override
    public FunctionCall visitFunctionCall(CalciteParser.FunctionCallContext ctx) {
        Expression expr = visitExpression(ctx.expression());
        if (ctx.COUNT() != null) {
            return FunctionCall.count(expr);
        }
        if (ctx.MAX() != null) {
            return FunctionCall.max(expr);
        }
        if (ctx.MIN() != null) {
            return FunctionCall.min(expr);
        }
        if (ctx.AVG() != null) {
            return FunctionCall.avg(expr);
        }
        if (ctx.SUM() != null) {
            return FunctionCall.sum(expr);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public SQLNode visitCastExpression(CalciteParser.CastExpressionContext ctx) {
        Type targetType = visitDataTypeContext(ctx.dataType());
        return new CastExpression(targetType, visitExpression(ctx.expression()));
    }

    public Type visitDataTypeContext(CalciteParser.DataTypeContext ctx) {
        if (ctx.BOOLEAN() != null) {
            return Type.BOOLEAN;
        }
        if (ctx.LONG() != null) {
            return Type.LONG;
        }
        if (ctx.DOUBLE() != null) {
            return Type.DOUBLE;
        }
        if (ctx.CHAR() != null) {
            return Type.CHAR;
        }
        if (ctx.DATE() != null) {
            return Type.DATE;
        }
        if (ctx.TIMESTAMP() != null) {
            return Type.TIMESTAMP;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Literal visitLiteral(CalciteParser.LiteralContext ctx) {
        if (ctx.NULL() != null) {
            return new Literal();
        }
        if (ctx.NUMBER() != null) {
            String text = ctx.NUMBER().getText();
            if (text.contains(".")) {
                return new Literal(Double.parseDouble(text));
            } else {
                return new Literal(Long.parseLong(text));
            }
        }
        if (ctx.TRUE() != null) {
            return new Literal(true);
        }
        if (ctx.FALSE() != null) {
            return new Literal(false);
        }
        if (ctx.STRING() != null) {
            return new Literal(ctx.STRING().getText());
        }
        return new Literal();
    }

    @Override
    public Identifier.Alias visitAlias(CalciteParser.AliasContext ctx) {
        return new Identifier.Alias(ctx.ID().getText());
    }

    @Override
    public Identifier.TableAlias visitTableAlias(CalciteParser.TableAliasContext ctx) {
        return new Identifier.TableAlias(ctx.ID().getText());
    }

    @Override
    public Identifier.ColumnName visitColumnName(CalciteParser.ColumnNameContext ctx) {
        return new Identifier.ColumnName(ctx.ID().getText());
    }

    @Override
    public Identifier.TableName visitTableName(CalciteParser.TableNameContext ctx) {
        return new Identifier.TableName(ctx.ID().getText());
    }

    private int visitLimitClauseContext(CalciteParser.LimitClauseContext ctx) {
        if (ctx == null) {
            return QueryStmt.NO_LIMIT;
        } else {
            return Integer.parseInt(ctx.NUMBER().getText());
        }
    }
}
