package com.huiruan.calcite.expression;

import com.huiruan.calcite.catalog.Type;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class Predicate extends Expression {
    public Predicate(List<Expression> inputs) {
        super(Type.BOOLEAN, inputs);
    }

    public enum CompoundType { AND, OR, NOT }

    public enum BinaryType { EQ, NE, LE, GE, LT, GT }

    public enum InType { IN, NOT_IN }

    public enum NullType { IS_NULL, IS_NOT_NULL }

    @Getter
    public static final class CompoundPredicate extends Predicate {
        private final CompoundType compoundType;

        public CompoundPredicate(CompoundType compoundType, Expression input) {
            this(compoundType, Collections.singletonList(input));
        }

        public CompoundPredicate(CompoundType compoundType, List<Expression> predicates) {
            super(predicates);
            this.compoundType = compoundType;
        }
    }

    @Getter
    public static final class BinaryPredicate extends Predicate {
        private final BinaryType binaryType;

        public BinaryPredicate(BinaryType binaryType, List<Expression> inputs) {
            super(inputs);
            this.binaryType = binaryType;
        }
    }

    @Getter
    public static final class InPredicate extends Predicate {
        private final InType inType;
        public InPredicate(InType inType, List<Expression> inputs) {
            super(inputs);
            this.inType = inType;
        }
    }

    @Getter
    public static final class NullPredicate extends Predicate {
        private final NullType nullType;
        public NullPredicate(NullType nullType, Expression input) {
            super(Collections.singletonList(input));
            this.nullType = nullType;
        }
    }
}
