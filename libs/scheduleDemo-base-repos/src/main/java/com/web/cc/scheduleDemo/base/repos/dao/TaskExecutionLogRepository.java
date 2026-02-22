package com.web.cc.scheduleDemo.base.repos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.cc.scheduleDemo.base.repos.entity.TaskExecutionLogEntity;

@Repository
public interface TaskExecutionLogRepository extends JpaRepository<TaskExecutionLogEntity, Long> {

    List<TaskExecutionLogEntity> findByTaskNameOrderByCreatedAtDesc(String taskName);

    List<TaskExecutionLogEntity> findTop10ByOrderByCreatedAtDesc();
}
