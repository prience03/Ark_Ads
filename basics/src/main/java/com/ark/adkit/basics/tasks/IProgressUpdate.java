package com.ark.adkit.basics.tasks;

public interface IProgressUpdate<Progress> {
    void onProgressUpdate(Progress... values);
}