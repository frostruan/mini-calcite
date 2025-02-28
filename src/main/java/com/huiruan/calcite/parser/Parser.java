package com.huiruan.calcite.parser;

import com.huiruan.calcite.parser.SQLNode.QueryStmt;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.huiruan.calcite.parser.generated.CalciteBaseVisitor;
import com.huiruan.calcite.parser.generated.CalciteLexer;
import com.huiruan.calcite.parser.generated.CalciteParser;

public final class Parser extends CalciteBaseVisitor<SQLNode> {
    private Parser() {}

    public static QueryStmt parse(String sqlText) {
        CalciteLexer lexer = new CalciteLexer(CharStreams.fromString(sqlText));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        CalciteParser parser = new CalciteParser(tokenStream);
        CalciteParser.QueryContext queryContext = parser.query();
        AstBuilder astBuilder = new AstBuilder();
        return astBuilder.visitQuery(queryContext);
    }

    public static final class ParseException extends RuntimeException {
        public ParseException(String msg) {
            super(msg);
        }
    }
}
