package com.huiruan.calcite.optimizer.base;

import com.huiruan.calcite.optimizer.operator.scalar.ColumnRefOperator;

public abstract class PhysicalProperty {

    public abstract boolean satisfy(PhysicalProperty property);
}
