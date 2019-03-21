package com.ark.adkit.basics.tasks;

public interface IPublishProgress<Progress> {
    void showProgress(Progress... values);
}
