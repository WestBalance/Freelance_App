package com.tasklink.repository;

import com.tasklink.model.TaskOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskOrderRepository extends JpaRepository<TaskOrder, Long> {
    @Query("select o from TaskOrder o join fetch o.client")
    List<TaskOrder> findAllWithClient();

    @Query("select o from TaskOrder o join fetch o.client where o.id = :id")
    Optional<TaskOrder> findByIdWithClient(Long id);

    @Query("select o from TaskOrder o join fetch o.client where o.client.id = :clientId")
    List<TaskOrder> findByClientId(Long clientId);
}
