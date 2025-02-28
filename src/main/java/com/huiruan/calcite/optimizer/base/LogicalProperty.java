package com.huiruan.calcite.optimizer.base;

import java.util.BitSet;

public class LogicalProperty {
    private BitSet outputColumns;

    public LogicalProperty() {
        this(new BitSet());
    }

    public LogicalProperty(BitSet outputColumns) {
        this.outputColumns = outputColumns;
    }
}
