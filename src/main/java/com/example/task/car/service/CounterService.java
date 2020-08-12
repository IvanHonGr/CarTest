package com.example.task.car.service;

import com.example.task.car.api.CounterRequest;

public interface CounterService {
    int DEFAULT_START_VALUE = 50;

    void proceedRequest(CounterRequest request);

    void setCounter(Integer counterValue);
}
