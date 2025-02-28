grammar Calcite;

@header {
package com.huiruan.calcite.parser.generated;
}

query
    : selectStatement (setOperator selectStatement)* limitClause? EOF
    ;

selectStatement
    : SELECT distinct? columnList
      relationClause
      (WHERE condition)?
      (GROUP BY groupByItem (',' groupByItem)*)?
      (HAVING havingCondition)?
      (ORDER BY orderByItem (',' orderByItem)*)?
    ;

relationClause
    : FROM tableReference (joinClause)*
    ;

tableReference
    : tableName (AS? tableAlias)?
    | subquery (AS? tableAlias)?
    ;

subquery
    : '(' query ')'
    ;

setOperator
    : UNION ALL?
    ;

joinClause
    : (INNER | LEFT | RIGHT | FULL) JOIN tableReference ON condition
    ;

distinct
    : DISTINCT
    ;

columnList
    : '*'                  # allColumns
    | columnItem (',' columnItem)*  # specificColumns
    ;

columnItem
    : expression (AS? alias)?
    | ('*' | tableAlias '.' '*')
    ;

tableAlias
    : ID
    ;

groupByItem
    : expression
    ;

orderByItem
    : expression (ASC | DESC)?
    ;

havingCondition
    : condition
    ;

condition
    : orCondition
    ;

orCondition
    : andCondition (OR andCondition)*
    ;

andCondition
    : notCondition (AND notCondition)*
    ;

notCondition
    : NOT? comparisonCondition
    ;

comparisonCondition
    : expression comparisonOperator expression
    | expression IS NOT? NULL
    | expression NOT? IN '(' expression (',' expression)* ')'
    | '(' condition ')'
    ;

comparisonOperator
    : EQ | NEQ | GT | LT | GTE | LTE
    ;

expression
    : literal
    | columnName
    | functionCall
    | castExpression
    | '(' expression ')'
    | subquery
    ;

functionCall
    : funcName=(COUNT | SUM | AVG | MIN | MAX) '(' expression ')'
    ;

castExpression
    : CAST '(' expression AS dataType ')'
    ;

dataType
    : BOOLEAN
    | LONG
    | DOUBLE
    | CHAR
    | DATE
    | TIMESTAMP
    ;

literal
    : NUMBER
    | STRING
    | TRUE
    | FALSE
    | NULL
    ;

alias
    : ID
    ;

columnName
    : ID
    ;

tableName
    : ID
    ;

limitClause
    : LIMIT NUMBER
    ;

// Keywords
SELECT : 'SELECT';
FROM : 'FROM';
WHERE : 'WHERE';
GROUP : 'GROUP';
BY : 'BY';
HAVING : 'HAVING';
ORDER : 'ORDER';
ASC : 'ASC';
DESC : 'DESC';
DISTINCT : 'DISTINCT';
UNION : 'UNION';
ALL : 'ALL';
INNER : 'INNER';
LEFT : 'LEFT';
RIGHT : 'RIGHT';
FULL : 'FULL';
JOIN : 'JOIN';
ON : 'ON';
AND : 'AND';
OR : 'OR';
NOT : 'NOT';
LIKE : 'LIKE';
IN : 'IN';
IS : 'IS';
NULL : 'NULL';
TRUE : 'TRUE';
FALSE : 'FALSE';
COUNT : 'COUNT';
SUM : 'SUM';
AVG : 'AVG';
MIN : 'MIN';
MAX : 'MAX';
IF : 'IF';
SEMI : 'SEMI';
LIMIT : 'LIMIT';
AS : 'AS';
CAST : 'CAST';

// Data types
BOOLEAN: 'BOOLEAN';
LONG: 'LONG';
DOUBLE : 'DOUBLE';
CHAR: 'CHAR';
DATE: 'DATE';
TIMESTAMP: 'TIMESTAMP';

// Operators
EQ : '=';
NEQ : '<>' | '!=';
GT : '>';
LT : '<';
GTE : '>=';
LTE : '<=';

// Literals
NUMBER : [0-9]+ ('.' [0-9]+)?;
STRING : '\'' ~('\'')* '\'';
ID : [a-zA-Z_] [a-zA-Z0-9_]*;

// Whitespace and comments
WS : [ \t\r\n]+ -> skip;
COMMENT : '--' ~[\r\n]* -> skip;