package com.example.task.car.api;

import com.example.task.car.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/")
@Validated
public class CounterController {

    @Autowired
    private CounterService counterService;

    @PostMapping("threads")
    @ResponseStatus(HttpStatus.CREATED)
    public void threads(
            @RequestBody @Valid CounterRequest request) {
        counterService.proceedRequest(request);
    }

    @PutMapping("counter")
    @ResponseStatus(HttpStatus.OK)
    public void counter(
            @RequestParam
            @Min(0) @Max(100) @Valid Integer counterValue) {
        counterService.setCounter(counterValue);
    }
}
