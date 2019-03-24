package com.ark.adkit.basics.tasks;

@Deprecated
public interface IProgressUpdate<Progress> {

    void onProgressUpdate(Progress... values);
}