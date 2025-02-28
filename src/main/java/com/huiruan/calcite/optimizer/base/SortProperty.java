package com.huiruan.calcite.optimizer.base;

import com.huiruan.calcite.optimizer.operator.scalar.ColumnRefOperator;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public final class SortProperty extends PhysicalProperty {
    private final List<OrderSpec> orderSpecs;

    public SortProperty(List<OrderSpec> orderSpecs) {
        this.orderSpecs = orderSpecs;
    }

    @Override
    public boolean satisfy(PhysicalProperty otherProperty) {
        if (! (otherProperty instanceof SortProperty)) {
            return false;
        }
        SortProperty property = (SortProperty) otherProperty;
        List<OrderSpec> otherOrderSpecs = property.getOrderSpecs();
        if (orderSpecs.size() < otherOrderSpecs.size()) {
            return false;
        }

        for (int i = 0; i < otherOrderSpecs.size(); i++) {
            if (!orderSpecs.get(i).matches(otherOrderSpecs.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderSpecs);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SortProperty)) {
            return false;
        }

        SortProperty rhs = (SortProperty) obj;
        if (orderSpecs.size() != rhs.orderSpecs.size()) {
            return false;
        }
        for (int i = 0; i < orderSpecs.size(); i++) {
            if (!orderSpecs.get(i).equals(rhs.orderSpecs.get(i))) {
                return false;
            }
        }
        return true;
    }

    public enum OrderType {
        ASCENDING, DESCENDING
    }

    public enum NullOrderType {
        NULLS_FIRST, NULLS_LAST
    }

    @Getter
    public static final class OrderSpec {
        private final ColumnRefOperator columnRef;
        private final OrderType orderType;
        private final NullOrderType nullOrderType;

        public OrderSpec(ColumnRefOperator columnRef, OrderType orderType, NullOrderType nullOrderType) {
            this.columnRef = columnRef;
            this.orderType = orderType;
            this.nullOrderType = nullOrderType;
        }

        public boolean isAscending() {
            return orderType == OrderType.ASCENDING;
        }

        public boolean isNullsFirst() {
            return nullOrderType == NullOrderType.NULLS_FIRST;
        }

        public boolean matches(OrderSpec rhs) {
            return columnRef.equals(rhs.columnRef) && orderType == rhs.orderType && nullOrderType == rhs.nullOrderType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(columnRef, orderType, nullOrderType);
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }

            if (!(object instanceof OrderSpec)) {
                return false;
            }

            OrderSpec other = (OrderSpec) object;
            return matches(other);
        }

        @Override
        public String toString() {
            return columnRef.toString() + " " + orderType.name() + " " + nullOrderType.name();
        }
    }
}
