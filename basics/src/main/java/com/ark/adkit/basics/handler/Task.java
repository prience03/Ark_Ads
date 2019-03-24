package com.ark.adkit.basics.handler;

import java.util.Queue;

interface Task extends Runnable, Result {
    void setPool(Queue<Task> pool);
}