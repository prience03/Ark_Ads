package com.ark.adkit.basics.tasks;

@Deprecated
public interface IDoInBackground<Params, Progress, Result> {

    Result doInBackground(IPublishProgress<Progress> publishProgress, Params... params);
}
