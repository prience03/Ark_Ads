package com.ark.adkit.basics.tasks;

@Deprecated
public interface IPublishProgress<Progress> {

    void showProgress(Progress... values);
}
