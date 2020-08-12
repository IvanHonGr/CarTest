package com.example.task.car.service;

import com.example.task.car.api.CounterRequest;
import com.example.task.car.repository.CounterResultRepo;
import com.example.task.car.domain.CounterRequestInfo;
import com.example.task.car.repository.CounterRequestRepo;
import com.example.task.car.domain.CounterResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Slf4j
@Service
public class CounterServiceImpl implements CounterService {

    private static final int MAX_COUNTER = 100;
    private static final int MIN_COUNTER = 0;

    private AtomicInteger counter = new AtomicInteger(DEFAULT_START_VALUE);

    @Autowired
    private CounterRequestRepo counterRequestRepo;
    @Autowired
    private CounterResultRepo counterResultRepo;

    @Override
    public void proceedRequest(CounterRequest request) {
        save(request.getProducerAmount(), request.getConsumerAmount());
        addThreads(request);
    }

    @Override
    public void setCounter(Integer counterValue) {
        counter = new AtomicInteger(counterValue);
    }

    private void save(int producerAmount, int consumerAmount) {
        CounterRequestInfo counterRequestInfo = CounterRequestInfo.builder()
                .producerCount(producerAmount)
                .consumerCount(consumerAmount)
                .build();

        counterRequestRepo.save(counterRequestInfo);
    }

    private void addThreads(CounterRequest request) {
        IntStream.range(0, request.getConsumerAmount())
                .forEach(i -> {
                    CounterThread thread = new CounterThread(AtomicInteger::decrementAndGet);
                    thread.start();
                });
        IntStream.range(0, request.getProducerAmount())
                .forEach(i -> {
                    CounterThread thread = new CounterThread(AtomicInteger::incrementAndGet);
                    thread.start();
                });
    }

    private class CounterThread extends Thread {
        private Consumer<AtomicInteger> consumer;

        private CounterThread(Consumer<AtomicInteger> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            while (!counterReachCeiling()) {
                synchronized (CounterThread.class) {
                    updateCounter();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
        }

        private void updateCounter() {
            if (!counterReachCeiling()) {
                consumer.accept(counter);
                log.info(String.format("Value changed by %s, current value: %s", Thread.currentThread().getName(), counter));
                if (counterReachCeiling()) {
                    resultReachedPersist();
                }
            }
        }

        private boolean counterReachCeiling() {
            return counter.get() == MIN_COUNTER || counter.get() == MAX_COUNTER;
        }

        private void resultReachedPersist() {
            CounterResult counterResult = new CounterResult();
            counterResultRepo.save(counterResult);
        }
    }
}
