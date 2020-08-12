package com.example.task.car.repository;

import com.example.task.car.domain.CounterResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterResultRepo extends JpaRepository<CounterResult, Long> {
}
