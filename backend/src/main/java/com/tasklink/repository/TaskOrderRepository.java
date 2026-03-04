package com.tasklink.repository;

import com.tasklink.model.TaskOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskOrderRepository extends JpaRepository<TaskOrder, Long> {
    List<TaskOrder> findByClientId(Long clientId);
}
