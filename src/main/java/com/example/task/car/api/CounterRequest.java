package com.example.task.car.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounterRequest {
    @Min(value = 0)
    @Max(value = 200)
    private int producerAmount;

    @Min(value = 0)
    @Max(value = 200)
    private int consumerAmount;
}
