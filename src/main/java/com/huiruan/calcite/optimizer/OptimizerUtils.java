package com.huiruan.calcite.optimizer;

import com.google.common.collect.Sets;
import com.huiruan.calcite.optimizer.operator.OperatorType;
import com.huiruan.calcite.optimizer.operator.scalar.ColumnRefOperator;
import com.huiruan.calcite.optimizer.operator.scalar.CompoundPredicateOperator;
import com.huiruan.calcite.optimizer.operator.scalar.ScalarOperator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class OptimizerUtils {
    private OptimizerUtils() {
    }

    /*
    public static List<ScalarOperator> extractConjuncts(ScalarOperator root) {
        LinkedList<ScalarOperator> list = new LinkedList<>();
        if (root == null) {
            return list;
        }
        extractConjunctsImpl(root, list);
        return list;
    }

    public static Set<ScalarOperator> extractConjunctSet(ScalarOperator root) {
        Set<ScalarOperator> list = Sets.newHashSet();
        if (root == null) {
            return list;
        }
        extractConjunctsImpl(root, list);
        return list;
    }

    private static void extractConjunctsImpl(ScalarOperator root, Collection<ScalarOperator> result) {
        if (root.getOpType() != OperatorType.COMPOUND) {
            result.add(root);
            return;
        }

        CompoundPredicateOperator cpo = (CompoundPredicateOperator) root;
        if (!cpo.isAnd()) {
            result.add(root);
            return;
        }
        extractConjunctsImpl(cpo.getChild(0), result);
        extractConjunctsImpl(cpo.getChild(1), result);
    }

    public static List<ScalarOperator> extractDisjunctive(ScalarOperator root) {
        LinkedList<ScalarOperator> list = new LinkedList<>();
        if (root == null) {
            return list;
        }
        extractDisjunctiveImpl(root, list);
        return list;
    }

    private static void extractDisjunctiveImpl(ScalarOperator root, List<ScalarOperator> result) {
        if (!OperatorType.COMPOUND.equals(root.getOpType())) {
            result.add(root);
            return;
        }

        CompoundPredicateOperator cpo = (CompoundPredicateOperator) root;
        if (!cpo.isOr()) {
            result.add(root);
            return;
        }
        extractDisjunctiveImpl(cpo.getChild(0), result);
        extractDisjunctiveImpl(cpo.getChild(1), result);
    } */
}
