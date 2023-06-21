/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.echobox.logging.escalator;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Implementation of the escalation trigger that triggers and escalation when a particular event
 * has been happening at least a minimum amount of times at a given interval for a given period. i
 * .e. has been happening consistently for a period of time and over X events in that period.
 *
 * Uses a {@link ConcurrentHashMap} to store these times in memory.
 *
 * @author eddspencer
 */
public class TimeBasedEscalationTrigger implements EscalationTrigger {
  
  private final Supplier<Long> unixTimeNow;
  private final long minEventCount;
  private final int refreshIntervalSecs;
  private final int escalationPeriodSecs;
  
  private final Map<String, EventInfo> cache = new ConcurrentHashMap<>();
  private Predicate<Object> keyFilter = key -> true;
  
  public TimeBasedEscalationTrigger(Supplier<Long> unixTimeNow, long minEventCount,
      int rereshIntervalSecs, int escalationPeriodSecs) {
    this.unixTimeNow = unixTimeNow;
    this.minEventCount = minEventCount;
    this.refreshIntervalSecs = rereshIntervalSecs;
    this.escalationPeriodSecs = escalationPeriodSecs;
  }
  
  public TimeBasedEscalationTrigger(long minEventCount, int refreshIntervalSecs,
      int escalationPeriodSecs) {
    this.unixTimeNow = () -> System.currentTimeMillis() / 1000;
    this.minEventCount = minEventCount;
    this.refreshIntervalSecs = refreshIntervalSecs;
    this.escalationPeriodSecs = escalationPeriodSecs;
  }
  
  /**
   * Sets a filter to only match specific object arguments when generating the even key
   *
   * @param keyFilter the predicate to use to filter arguments for use in event key
   * @return this
   */
  public TimeBasedEscalationTrigger withEventKeyFilter(Predicate<Object> keyFilter) {
    this.keyFilter = keyFilter;
    return this;
  }
  
  @Override
  public boolean markAndTrigger(LoggingLevel level, Object... keys) {
    final String key = createEventKey(level, keys);
    final long now = unixTimeNow.get();
    
    final AtomicBoolean escalate = new AtomicBoolean(false);
    cache.compute(key, (k, eventInfo) -> {
      // If we have no other events just cache this one
      if (eventInfo == null) {
        return new EventInfo(now, now, 1);
      }
  
      final long eventCount = eventInfo.eventCount + 1;
      final boolean escalationPeriodPassed =
          now - eventInfo.firstSeenUnixTime >= escalationPeriodSecs;
      final boolean withinInterval = now - eventInfo.lastSeenUnixTime <= refreshIntervalSecs;
      final boolean eventCountExceeded = eventCount >= minEventCount;
      
      // Trigger escalation and reset event if enough time has passed and event count hit
      if (escalationPeriodPassed && withinInterval && eventCountExceeded) {
        escalate.set(true);
        return null;
      }
      
      // Reset the info if we are outside the refresh interval
      if (!withinInterval) {
        return new EventInfo(now, now, 1);
      }
      
      // Otherwise update event info
      return new EventInfo(eventInfo.firstSeenUnixTime, now, eventCount);
    });
    
    return escalate.get();
  }
  
  /**
   * Converts the logging level and objects provided to the logger to a unique key that
   * identifies the type of event
   *
   * @param level the logging level
   * @param eventArgs the arguments sent to the logging event
   * @return the unique key for the event
   */
  protected String createEventKey(LoggingLevel level, Object[] eventArgs) {
    return String.format("%s::%s", level,
        Arrays.stream(eventArgs).filter(keyFilter).map(Object::toString)
            .collect(Collectors.joining("_")));
  }
  
  /**
   * Event information for the time based event cache
   *
   * @author eddspencer
   */
  private static class EventInfo {
    private final long firstSeenUnixTime;
    private final long lastSeenUnixTime;
    private final long eventCount;
    
    private EventInfo(long firstSeenUnixTime, long lastSeenUnixTime, long eventCount) {
      this.firstSeenUnixTime = firstSeenUnixTime;
      this.lastSeenUnixTime = lastSeenUnixTime;
      this.eventCount = eventCount;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      EventInfo eventInfo = (EventInfo) obj;
      return firstSeenUnixTime == eventInfo.firstSeenUnixTime
          && lastSeenUnixTime == eventInfo.lastSeenUnixTime;
    }
    
    @Override
    public int hashCode() {
      return Objects.hash(firstSeenUnixTime, lastSeenUnixTime);
    }
  }
}
