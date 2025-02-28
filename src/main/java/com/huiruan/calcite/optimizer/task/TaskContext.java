package com.huiruan.calcite.optimizer.task;

import com.huiruan.calcite.optimizer.OptimizerContext;
import com.huiruan.calcite.optimizer.base.ColumnRefSet;
import com.huiruan.calcite.optimizer.base.PhysicalPropertySet;
import com.huiruan.calcite.optimizer.operator.logical.LogicalScanOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;


public class TaskContext {
    @Getter
    private final OptimizerContext optimizerContext;
    @Getter
    private final PhysicalPropertySet requiredProperty;
    @Setter
    @Getter
    private ColumnRefSet requiredColumns;
    @Setter
    @Getter
    private double upperBoundCost;
    @Getter
    @Setter
    private List<LogicalScanOperator> allScanOperators;
    @Setter
    private boolean isHybridScanIncluded = false;

    public TaskContext(OptimizerContext context,
                       PhysicalPropertySet physicalPropertySet,
                       ColumnRefSet requiredColumns,
                       double cost) {
        this.optimizerContext = context;
        this.requiredProperty = physicalPropertySet;
        this.requiredColumns = requiredColumns;
        this.upperBoundCost = cost;
        this.allScanOperators = Collections.emptyList();
    }

    public boolean isHybridScanIncluded() {
        return isHybridScanIncluded;
    }
}
