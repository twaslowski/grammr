package com.grammr.common;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractConsumer<T> {

  public AbstractConsumer(BlockingQueue<T> queue) {
    this.queue = queue;
  }

  protected final BlockingQueue<T> queue;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @PostConstruct
  public void init() {
    log.info("Starting incoming queue processor ...");
    scheduler.execute(this::processUpdate);
  }

  private void processUpdate() {
    try {
      var item = queue.take();
      handleItem(item);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Queue processing interrupted", e);
    }
  }

  protected abstract void handleItem(T item);
}
