package com.edu.EvaluationWeb.component;

import com.edu.EvaluationWeb.entity.StorageNode;
import com.edu.EvaluationWeb.exception.BaseException;

import java.util.Comparator;

public class NodesComparator implements Comparator<StorageNode> {

    private static final Integer EQUALS = 0;
    private static final Integer GREATER = 1;
    private static final Integer LOWER = -1;

    @Override
    public int compare(StorageNode o1, StorageNode o2) {
        if(o1.getDir() && o2.getDir()) {
            return EQUALS;
        } else if(o1.getDir()) {
            return LOWER;
        } else if(o2.getDir()) {
            return GREATER;
        }
        return compareNames(o1.getName(), o2.getName());
    }

    private int compareNames(String name1, String name2) {
        if(name1 == null || name1.isEmpty() || name2 == null || name2.isEmpty()) {
            throw new BaseException("Node is not comparable (Name is null)");
        }
        return name1.charAt(0) - name2.charAt(0);
    }
}
