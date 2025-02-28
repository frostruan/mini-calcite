package com.huiruan.calcite.analyzer;

import com.huiruan.calcite.catalog.Catalog;
import com.huiruan.calcite.catalog.Database;
import com.huiruan.calcite.expression.*;

public class ExpressionAnalyzer {

    public static void analyzeExpression(Expression expr, Scope scope, Catalog catalog) {

    }

    private void analyze(Expression expr, Scope scope, Catalog catalog) {
        ExpressionVisitor visitor = new ExpressionVisitor(catalog);
        bottomUpAnalyze(visitor, expr, scope);
    }

    private void bottomUpAnalyze(ExpressionVisitor visitor, Expression expr, Scope scope) {
        for (Expression child : expr.getInputs()) {
            bottomUpAnalyze(visitor, child, scope);
        }
        visitor.visit(expr, scope);
    }


    private static final class ExpressionVisitor extends AstVisitor<Void, Scope> {

        private final Catalog catalog;

        private ExpressionVisitor(Catalog catalog) {
            this.catalog = catalog;
        }

        public Void visit(Expression node, Scope context) {
            return node.accept(this, context);
        }


        public Void visitColumnName(Identifier.ColumnName columnName, Scope scope) {

            return null;
        }
    }
}
