package com.lld.problems.googledocs.code.models;

import com.lld.problems.googledocs.code.operations.Operation;

public interface DocumentListener {
    public void onChange(String docId, Operation op);
}
