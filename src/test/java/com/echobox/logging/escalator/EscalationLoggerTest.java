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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

class EscalationLoggerTest {
  
  private EscalationTrigger trigger;
  private EscalationAction action;
  private EscalationLogger escalationLogger;
  
  @BeforeEach
  public void setup() {
    trigger = Mockito.mock(EscalationTrigger.class);
    action = Mockito.mock(EscalationAction.class);
    
    final Logger logger = Mockito.mock(Logger.class);
    escalationLogger = new EscalationLogger(logger, trigger, action);
  }
  
  @Test
  public void notEscalateButTriggered() {
    escalationLogger.debug("This is a test");
    
    verify(action, times(0)).escalate("This is a test");
  }
  
  @Test
  public void escalateTRACE() {
    when(trigger.markAndTrigger(LoggingLevel.TRACE, "This is a {}", "test")).thenReturn(true);
    
    escalationLogger.trace("This is a {}", "test");
    
    verify(action).escalate("This is a {}", "test");
  }
  
  @Test
  public void escalateDEBUG() {
    when(trigger.markAndTrigger(LoggingLevel.DEBUG, "This is a test")).thenReturn(true);
  
    escalationLogger.debug("This is a test");
    
    verify(action).escalate("This is a test");
  }
  
  @Test
  public void escalateINFO() {
    when(trigger.markAndTrigger(LoggingLevel.INFO, "This is a test")).thenReturn(true);
    
    escalationLogger.info("This is a test");
    
    verify(action).escalate("This is a test");
  }
  
  @Test
  public void escalateWARN() {
    when(trigger.markAndTrigger(LoggingLevel.WARN, "This is a test")).thenReturn(true);
    
    escalationLogger.warn("This is a test");
    
    verify(action).escalate("This is a test");
  }
  
  @Test
  public void escalateERROR() {
    Exception error = new Exception("Error");
    when(trigger.markAndTrigger(LoggingLevel.ERROR, "This is a test", error)).thenReturn(true);
  
    escalationLogger.error("This is a test", error);
  
    verify(action).escalate("This is a test", error);
  }
}
