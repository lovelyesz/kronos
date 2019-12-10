package com.yz.meson.execute;

import com.yz.meson.model.ExecuteJobInfo;

public interface MesonExecuteFactory {

    void setExecuteJobInfo(ExecuteJobInfo executeJobInfo);

    void setIsolatedJavaJob(IsolatedJavaJob isolatedJavaJob);

    void init() throws Exception;

}
