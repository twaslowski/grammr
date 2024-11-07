package com.grammr.common;

import io.micrometer.core.instrument.util.NamedThreadFactory;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractConsumer<T> {

  public AbstractConsumer(BlockingQueue<T> queue) {
    this.queue = queue;
  }

  protected final BlockingQueue<T> queue;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, new NamedThreadFactory(this.getClass().getSimpleName()));

  @PostConstruct
  public void init() {
    log.info("Starting queue processor: {}", this.getClass().getSimpleName());
    scheduler.scheduleAtFixedRate(this::processUpdate, 0, 50, TimeUnit.MILLISECONDS);
  }

  private void processUpdate() {
    try {
      var item = queue.take();
      handleItem(item);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Queue processing interrupted", e);
    } catch (Exception e) {
      log.error("Error processing item from queue", e);
    }
  }

  protected abstract void handleItem(T item);
}
