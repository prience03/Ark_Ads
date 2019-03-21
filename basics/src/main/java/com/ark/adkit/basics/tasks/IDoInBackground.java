package com.ark.adkit.basics.tasks;

public interface IDoInBackground<Params, Progress, Result> {
    Result doInBackground(IPublishProgress<Progress> publishProgress, Params... params);
}
