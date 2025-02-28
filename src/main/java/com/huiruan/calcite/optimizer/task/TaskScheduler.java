package com.huiruan.calcite.optimizer.task;

import java.util.Stack;

public class TaskScheduler {
    private final Stack<OptimizerTask> tasks = new Stack<>();

    private TaskScheduler() {
    }

    public void pushTask(OptimizerTask task) {
        tasks.push(task);
    }

    public void executeTasks(TaskContext context) {
        while (!tasks.isEmpty()) {
            OptimizerTask task = tasks.pop();
            task.execute();
        }
    }
}
