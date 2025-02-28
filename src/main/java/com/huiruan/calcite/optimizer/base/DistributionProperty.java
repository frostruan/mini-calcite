package com.huiruan.calcite.optimizer.base;

import com.huiruan.calcite.optimizer.operator.scalar.ColumnRefOperator;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class DistributionProperty extends PhysicalProperty {
    private final List<DistributionSpec> distributionSpecs;

    public DistributionProperty(List<DistributionSpec> distributionSpecs) {
        this.distributionSpecs = distributionSpecs;
    }

    @Override
    public boolean satisfy(PhysicalProperty otherProperty) {
        if (! (otherProperty instanceof DistributionProperty)) {
            return false;
        }
        DistributionProperty property = (DistributionProperty) otherProperty;
        List<DistributionSpec> otherDistributionSpecs = property.getDistributionSpecs();
        if (distributionSpecs.size() < otherDistributionSpecs.size()) {
            return false;
        }

        for (int i = 0; i < otherDistributionSpecs.size(); i++) {
            if (!distributionSpecs.get(i).matches(otherDistributionSpecs.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distributionSpecs);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }


        if (!(obj instanceof DistributionProperty)) {
            return false;
        }

        DistributionProperty rhs = (DistributionProperty) obj;
        if (distributionSpecs.size() != rhs.distributionSpecs.size()) {
            return false;
        }
        for (int i = 0; i < distributionSpecs.size(); i++) {
            if (!distributionSpecs.get(i).equals(rhs.distributionSpecs.get(i))) {
                return false;
            }
        }
        return true;
    }

    public enum DistributionType {
        ANY,
        BROADCAST,
        HASH,
        RANGE
    }

    @Getter
    public static final class DistributionSpec {
        private final ColumnRefOperator columnRef;
        private final DistributionType type;

        DistributionSpec(ColumnRefOperator columnRef, DistributionType type) {
            this.columnRef = columnRef;
            this.type = type;
        }

        public boolean matches(DistributionSpec spec) {
            return columnRef.equals(spec.columnRef) && type == spec.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(columnRef, type);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof DistributionSpec)) {
                return false;
            }
            return matches((DistributionSpec) object);
        }

        @Override
        public String toString() {
            return columnRef + " " + type;
        }
    }
}
