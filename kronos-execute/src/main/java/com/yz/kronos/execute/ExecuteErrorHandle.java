package com.yz.kronos.execute;

public interface ExecuteErrorHandle {

    void errorHandle(Integer index,Integer shareTotal,String execId,Exception e);

}
