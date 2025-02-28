package com.huiruan.calcite.optimizer.operator;

public enum OperatorType {
    /**
     * Logical operator
     */
    LOGICAL,
    LOGICAL_JOIN,
    LOGICAL_AGGR,
    LOGICAL_FILTER,
    LOGICAL_LIMIT,
    LOGICAL_TOPN,
    LOGICAL_TABLE_SCAN,

    /**
     * Physical operator
     */
    PHYSICAL,
    PHYSICAL_JOIN,
    PHYSICAL_AGGR,
    PHYSICAL_FILTER,
    PHYSICAL_LIMIT,
    PHYSICAL_TOPN,
    PHYSICAL_TABLE_SCAN,

    /**
     * Scalar operator
     */
    SCALAR,
    ARRAY,
    COLLECTION_ELEMENT,
    ARRAY_SLICE,
    VARIABLE,
    CONSTANT,
    CALL,
    BETWEEN,
    BINARY,
    COMPOUND,
    EXISTS,
    IN,
    IS_NULL,
    LIKE,
    DICT_MAPPING,
    CLONE,
    LAMBDA_FUNCTION,
    LAMBDA_ARGUMENT,
    SUBQUERY,
    SUBFIELD,

    /**
     * PATTERN
     */
    PATTERN,
    PATTERN_LEAF,
    PATTERN_MULTI_LEAF,
    // for all type scan node
    PATTERN_SCAN,
    // for extracting pattern like this
    //     join
    //    /    \
    //  join   table
    //  /  \
    // table table
    PATTERN_MULTIJOIN,
}
