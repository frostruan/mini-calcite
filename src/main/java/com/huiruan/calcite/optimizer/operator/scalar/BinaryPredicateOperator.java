package com.huiruan.calcite.optimizer.operator.scalar;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.huiruan.calcite.catalog.Type;
import com.huiruan.calcite.optimizer.operator.OperatorType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BinaryPredicateOperator extends PredicateOperator {
    private static final Map<BinaryType, BinaryType> BINARY_COMMUTATIVE_MAP =
            ImmutableMap.<BinaryType, BinaryType>builder()
                    .put(BinaryType.EQ, BinaryType.EQ)
                    .put(BinaryType.NE, BinaryType.NE)
                    .put(BinaryType.LE, BinaryType.GE)
                    .put(BinaryType.LT, BinaryType.GT)
                    .put(BinaryType.GE, BinaryType.LE)
                    .put(BinaryType.GT, BinaryType.LT)
                    .put(BinaryType.EQ_FOR_NULL, BinaryType.EQ_FOR_NULL)
                    .build();

    private static final Map<BinaryType, BinaryType> BINARY_NEGATIVE_MAP =
            ImmutableMap.<BinaryType, BinaryType>builder()
                    .put(BinaryType.EQ, BinaryType.NE)
                    .put(BinaryType.NE, BinaryType.EQ)
                    .put(BinaryType.LE, BinaryType.GT)
                    .put(BinaryType.LT, BinaryType.GE)
                    .put(BinaryType.GE, BinaryType.LT)
                    .put(BinaryType.GT, BinaryType.LE)
                    .build();

    private final BinaryType type;

    public BinaryPredicateOperator(BinaryType type, ScalarOperator... arguments) {
        super(OperatorType.BINARY, arguments);
        this.type = type;
        Preconditions.checkState(arguments.length == 2);
    }

    public BinaryPredicateOperator(BinaryType type, List<ScalarOperator> arguments) {
        super(OperatorType.BINARY, arguments);
        this.type = type;
        Preconditions.checkState(arguments.size() == 2);
    }

    public BinaryType getBinaryType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BinaryPredicateOperator that = (BinaryPredicateOperator) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }

    @Override
    public boolean isNullable() {
        return type != BinaryType.EQ_FOR_NULL && super.isNullable();
    }

    public static BinaryPredicateOperator eq(ScalarOperator lhs, ScalarOperator rhs) {
        return new BinaryPredicateOperator(BinaryType.EQ, lhs, rhs);
    }

    public static BinaryPredicateOperator ge(ScalarOperator lhs, ScalarOperator rhs) {
        return new BinaryPredicateOperator(BinaryType.GE, lhs, rhs);
    }

    public static BinaryPredicateOperator gt(ScalarOperator lhs, ScalarOperator rhs) {
        return new BinaryPredicateOperator(BinaryType.GT, lhs, rhs);
    }

    public static BinaryPredicateOperator ne(ScalarOperator lhs, ScalarOperator rhs) {
        return new BinaryPredicateOperator(BinaryType.NE, lhs, rhs);
    }

    public static BinaryPredicateOperator le(ScalarOperator lhs, ScalarOperator rhs) {
        return new BinaryPredicateOperator(BinaryType.LE, lhs, rhs);
    }

    public static BinaryPredicateOperator lt(ScalarOperator lhs, ScalarOperator rhs) {
        return new BinaryPredicateOperator(BinaryType.LT, lhs, rhs);
    }

    public enum BinaryType {
        EQ("="),
        NE("!="),
        LE("<="),
        GE(">="),
        LT("<"),
        GT(">"),
        EQ_FOR_NULL("<=>");

        private final String type;

        BinaryType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }

        public boolean isEqual() {
            return this == EQ;
        }

        public boolean isNotEqual() {
            return this == NE;
        }

        public boolean isEquivalence() {
            return this == EQ || this == EQ_FOR_NULL;
        }

        public boolean isNotEquivalence() {
            return this == NE;
        }

        public boolean isNotRangeComparison() {
            return isEquivalence() || isNotEquivalence();
        }

        public boolean isRange() {
            return this == LE
                    || this == GE
                    || this == LT
                    || this == GT;
        }

        public boolean isRangeOrNe() {
            return isRange() || this == NE;
        }
    }
}
