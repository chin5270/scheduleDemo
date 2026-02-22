package com.web.cc.scheduleDemo.base.repos.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.cc.scheduleDemo.base.repos.entity.ScheduledTaskConfigEntity;

@Repository
public interface ScheduledTaskConfigRepository extends JpaRepository<ScheduledTaskConfigEntity, Long> {

    Optional<ScheduledTaskConfigEntity> findByTaskName(String taskName);

    List<ScheduledTaskConfigEntity> findByIsActive(Boolean isActive);
}
