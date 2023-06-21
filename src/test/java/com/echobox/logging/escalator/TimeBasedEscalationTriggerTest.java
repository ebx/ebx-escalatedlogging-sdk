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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeBasedEscalationTriggerTest {
  
  private TimeBasedEscalationTrigger trigger;
  private long now;
  
  @BeforeEach
  public void setup() {
    trigger = new TimeBasedEscalationTrigger(() -> now, 3, 30, 60);
  }
  
  @Test
  public void doesNotEscalateOnFirstEvent() {
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
  }
  
  @Test
  public void doesNotEscalateIfNotEnoughTimePassed() {
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
  }
  
  @Test
  public void doesEscalateIfEnoughTimePassed() {
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertTrue(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
  }
  
  @Test
  public void doesNotEscalateIfEnoughTimePassedButFailsMinInterval() {
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 40;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
  }
  
  @Test
  public void doesNotEscalateIfNotEnoughEventsHaveHappened() {
    trigger = new TimeBasedEscalationTrigger(() -> now, 4, 30, 60);
    
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
  }
  
  @Test
  public void allowsEventKeyToBeOverwritten() {
    trigger = new TimeBasedEscalationTrigger(() -> now, 2, 30, 60) {
      @Override
      protected String createEventKey(LoggingLevel level, Object[] eventArgs) {
        return "same-key";
      }
    };
  
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertTrue(trigger.markAndTrigger(LoggingLevel.DEBUG, "other"));
  }
  
  @Test
  public void allowsFilteringOfEventArguments() {
    trigger = new TimeBasedEscalationTrigger(() -> now, 2, 30, 60)
        .withEventKeyFilter(arg -> arg instanceof Exception);
  
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event1", new Exception("error")));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event2", new Exception("error")));
    now += 30;
    assertTrue(trigger.markAndTrigger(LoggingLevel.DEBUG, "event3", new Exception("error")));
  }
  
  @Test
  public void resetOnEventEscalation() {
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertTrue(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertTrue(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
  }
  
  @Test
  public void resetWhenEventIsMissed() {
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    now += 40;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
  }
  
  @Test
  public void canKeepTrackOfMultipleEventsByLevel() {
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
    assertFalse(trigger.markAndTrigger(LoggingLevel.ERROR, "event"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.ERROR, "event"));
    now += 30;
    assertTrue(trigger.markAndTrigger(LoggingLevel.ERROR, "event"));
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event"));
  }
  
  @Test
  public void canKeepTrackOfMultipleEventsByKey() {
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event1"));
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event2"));
    now += 30;
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event1"));
    now += 30;
    assertTrue(trigger.markAndTrigger(LoggingLevel.DEBUG, "event1"));
    assertFalse(trigger.markAndTrigger(LoggingLevel.DEBUG, "event2"));
  }
}
