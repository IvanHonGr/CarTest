package com.example.task.car.repository;

import com.example.task.car.domain.CounterRequestInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRequestRepo extends JpaRepository<CounterRequestInfo, Long> {
}
