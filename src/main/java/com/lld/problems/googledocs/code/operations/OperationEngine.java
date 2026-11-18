package com.lld.problems.googledocs.code.operations;

import java.util.List;
import java.util.Objects;

public class OperationEngine {
    public Operation transform(Operation op, List<Operation> appliedOperation) {
        Operation current = op;

        for (Operation applied : appliedOperation) {
            current = current.transformAgainst(applied);

            if (Objects.isNull(current)) {
                return null;
            }
        }
        return current;
    }
}
