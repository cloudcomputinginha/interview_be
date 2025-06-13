package cloudcomputinginha.demo.domain;

import cloudcomputinginha.demo.apiPayload.exception.GeneralException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class InterviewTest {
    private Interview interview;

    @Test
    void 쓰레드_100개가_면접을_신청하면_인터뷰의_현재참가인원은_100이다() throws InterruptedException {
        final int REQUEST_COUNT = 100;
        interview = Interview.builder()
                .currentParticipants(new AtomicInteger(0))
                .maxParticipants(REQUEST_COUNT)
                .build();

        ExecutorService execustorService = Executors.newFixedThreadPool(30);
        CountDownLatch countDownLatch = new CountDownLatch(REQUEST_COUNT);

        for (int i = 0; i < REQUEST_COUNT; i++) {
            execustorService.submit(() -> {
                try {
                    interview.increaseCurrentParticipants();
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        countDownLatch.await(); //100개의 쓰레드가 작업이 완료될 때까지 기다리기

        Assertions.assertThat(interview.getCurrentParticipants().intValue()).isEqualTo(REQUEST_COUNT);
    }

    @Test
    void 최대참가인원을_넘기면_현재참가인원은_증가하지_않는다() throws InterruptedException {
        final int MAX_PARTICIPANTS = 15;
        final int REQUEST_COUNT = MAX_PARTICIPANTS * 2;

        interview = Interview.builder()
                .currentParticipants(new AtomicInteger(0))
                .maxParticipants(MAX_PARTICIPANTS)
                .build();

        ExecutorService execustorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(REQUEST_COUNT);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 0; i < REQUEST_COUNT; i++) {
            execustorService.submit(() -> {
                try {
                    interview.increaseCurrentParticipants();
                    success.incrementAndGet();
                } catch (GeneralException e) {
                    fail.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Assertions.assertThat(interview.getCurrentParticipants().intValue()).isEqualTo(MAX_PARTICIPANTS);
        Assertions.assertThat(success.get()).isEqualTo(MAX_PARTICIPANTS);
        Assertions.assertThat(fail.get()).isEqualTo(MAX_PARTICIPANTS);
    }
}
