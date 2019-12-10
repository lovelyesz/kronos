package com.yz.meson.schedule;

import io.fabric8.kubernetes.api.model.batch.Job;

import java.util.List;

public interface ScheduleFactory {

    void execute();

    List<Job> shutdown();

}
