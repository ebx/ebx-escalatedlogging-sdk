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

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * Logger wrapper that will escalate to another action if a certain trigger function passes on a
 * logger. For example, if 10 debug messages of the same text are made within 10min an
 * escalation could be made.
 *
 * N.B. the original message is still logged even when an escalation is made.
 *
 * @author eddspencer
 */
public class EscalationLogger implements Logger {
  
  private final Logger logger;
  private final EscalationTrigger trigger;
  private final EscalationAction action;
  
  public EscalationLogger(Logger logger, EscalationTrigger trigger, EscalationAction action) {
    this.logger = logger;
    this.trigger = trigger;
    this.action = action;
  }
  
  private <T> void checkForEscalation(LoggingLevel level, Object... keys) {
    if (trigger.markAndTrigger(level, keys)) {
      action.escalate(keys);
    }
  }
  
  @Override
  public String getName() {
    return logger.getName();
  }
  
  @Override
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }
  
  @Override
  public boolean isTraceEnabled(Marker marker) {
    return logger.isTraceEnabled(marker);
  }
  
  @Override
  public void trace(String msg) {
    checkForEscalation(LoggingLevel.TRACE, msg);
    
    logger.trace(msg);
  }
  
  @Override
  public void trace(String format, Object arg) {
    checkForEscalation(LoggingLevel.TRACE, format, arg);
    
    logger.trace(format, arg);
  }
  
  @Override
  public void trace(String format, Object arg1, Object arg2) {
    checkForEscalation(LoggingLevel.TRACE, format, arg1, arg2);
    
    logger.trace(format, arg1, arg2);
  }
  
  @Override
  public void trace(String format, Object... arguments) {
    checkForEscalation(LoggingLevel.TRACE, format, arguments);
    
    logger.trace(format, arguments);
  }
  
  @Override
  public void trace(String msg, Throwable throwable) {
    checkForEscalation(LoggingLevel.TRACE, msg, throwable);
    
    logger.trace(msg, throwable);
  }
  
  @Override
  public void trace(Marker marker, String msg) {
    checkForEscalation(LoggingLevel.TRACE, marker, msg);
    
    logger.trace(marker, msg);
  }
  
  @Override
  public void trace(Marker marker, String format, Object arg) {
    checkForEscalation(LoggingLevel.TRACE, marker, format, arg);
    
    logger.trace(marker, format, arg);
  }
  
  @Override
  public void trace(Marker marker, String format, Object arg1, Object arg2) {
    checkForEscalation(LoggingLevel.TRACE, marker, format, arg1, arg2);
    
    logger.trace(marker, format, arg1, arg2);
  }
  
  @Override
  public void trace(Marker marker, String format, Object... argArray) {
    checkForEscalation(LoggingLevel.TRACE, marker, format, argArray);
    
    logger.trace(marker, format, argArray);
  }
  
  @Override
  public void trace(Marker marker, String msg, Throwable throwable) {
    checkForEscalation(LoggingLevel.TRACE, marker, msg, throwable);
    
    logger.trace(marker, msg, throwable);
  }
  
  @Override
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }
  
  @Override
  public boolean isDebugEnabled(Marker marker) {
    return logger.isDebugEnabled(marker);
  }
  
  @Override
  public void debug(String msg) {
    checkForEscalation(LoggingLevel.DEBUG, msg);
    
    logger.debug(msg);
  }
  
  @Override
  public void debug(String format, Object arg) {
    checkForEscalation(LoggingLevel.DEBUG, format, arg);
    
    logger.debug(format, arg);
  }
  
  @Override
  public void debug(String format, Object arg1, Object arg2) {
    checkForEscalation(LoggingLevel.DEBUG, format, arg1, arg2);
    
    logger.debug(format, arg1, arg2);
  }
  
  @Override
  public void debug(String format, Object... arguments) {
    checkForEscalation(LoggingLevel.DEBUG, format, arguments);
    
    logger.debug(format, arguments);
  }
  
  @Override
  public void debug(String msg, Throwable throwable) {
    checkForEscalation(LoggingLevel.DEBUG, msg, throwable);
    
    logger.debug(msg, throwable);
  }
  
  @Override
  public void debug(Marker marker, String msg) {
    checkForEscalation(LoggingLevel.DEBUG, marker, msg);
    
    logger.debug(marker, msg);
  }
  
  @Override
  public void debug(Marker marker, String format, Object arg) {
    checkForEscalation(LoggingLevel.DEBUG, marker, format, arg);
    
    logger.debug(marker, format, arg);
  }
  
  @Override
  public void debug(Marker marker, String format, Object arg1, Object arg2) {
    checkForEscalation(LoggingLevel.DEBUG, marker, arg1, arg2);
    
    logger.debug(marker, format, arg1, arg2);
  }
  
  @Override
  public void debug(Marker marker, String format, Object... arguments) {
    checkForEscalation(LoggingLevel.DEBUG, marker, format, arguments);
    
    logger.debug(marker, format, arguments);
  }
  
  @Override
  public void debug(Marker marker, String msg, Throwable throwable) {
    checkForEscalation(LoggingLevel.DEBUG, marker, msg, throwable);
    
    logger.debug(marker, msg, throwable);
  }
  
  @Override
  public boolean isInfoEnabled() {
    return logger.isInfoEnabled();
  }
  
  @Override
  public boolean isInfoEnabled(Marker marker) {
    return logger.isInfoEnabled(marker);
  }
  
  @Override
  public void info(String msg) {
    checkForEscalation(LoggingLevel.INFO, msg);
    
    logger.info(msg);
  }
  
  @Override
  public void info(String format, Object arg) {
    checkForEscalation(LoggingLevel.INFO, format, arg);
    
    logger.info(format, arg);
  }
  
  @Override
  public void info(String format, Object arg1, Object arg2) {
    checkForEscalation(LoggingLevel.INFO, arg1, arg2);
    
    logger.info(format, arg1, arg2);
  }
  
  @Override
  public void info(String format, Object... arguments) {
    checkForEscalation(LoggingLevel.INFO, format, arguments);
    
    logger.info(format, arguments);
  }
  
  @Override
  public void info(String msg, Throwable throwable) {
    checkForEscalation(LoggingLevel.INFO, msg, throwable);
    
    logger.info(msg, throwable);
  }
  
  @Override
  public void info(Marker marker, String msg) {
    checkForEscalation(LoggingLevel.INFO, marker, msg);
    
    logger.info(marker, msg);
  }
  
  @Override
  public void info(Marker marker, String format, Object arg) {
    checkForEscalation(LoggingLevel.INFO, marker, arg);
    
    logger.info(marker, format, arg);
  }
  
  @Override
  public void info(Marker marker, String format, Object arg1, Object arg2) {
    checkForEscalation(LoggingLevel.INFO, marker, arg1, arg2);
    
    logger.info(marker, format, arg1, arg2);
  }
  
  @Override
  public void info(Marker marker, String format, Object... arguments) {
    checkForEscalation(LoggingLevel.INFO, marker, format, arguments);
    
    logger.info(marker, format, arguments);
  }
  
  @Override
  public void info(Marker marker, String msg, Throwable throwable) {
    checkForEscalation(LoggingLevel.INFO, marker, msg, throwable);
    
    logger.info(marker, msg, throwable);
  }
  
  @Override
  public boolean isWarnEnabled() {
    return logger.isWarnEnabled();
  }
  
  @Override
  public boolean isWarnEnabled(Marker marker) {
    return logger.isWarnEnabled(marker);
  }
  
  @Override
  public void warn(String msg) {
    checkForEscalation(LoggingLevel.WARN, msg);
    
    logger.warn(msg);
  }
  
  @Override
  public void warn(String format, Object arg) {
    checkForEscalation(LoggingLevel.WARN, format, arg);
    
    logger.warn(format, arg);
  }
  
  @Override
  public void warn(String format, Object... arguments) {
    checkForEscalation(LoggingLevel.WARN, format, arguments);
    
    logger.warn(format, arguments);
  }
  
  @Override
  public void warn(String format, Object arg1, Object arg2) {
    checkForEscalation(LoggingLevel.WARN, format, arg1, arg2);
    
    logger.warn(format, arg1, arg2);
  }
  
  @Override
  public void warn(String msg, Throwable throwable) {
    checkForEscalation(LoggingLevel.WARN, msg, throwable);
    
    logger.warn(msg, throwable);
  }
  
  @Override
  public void warn(Marker marker, String msg) {
    checkForEscalation(LoggingLevel.WARN, marker, msg);
    
    logger.warn(marker, msg);
  }
  
  @Override
  public void warn(Marker marker, String format, Object arg) {
    checkForEscalation(LoggingLevel.WARN, marker, format, arg);
    
    logger.warn(marker, format, arg);
  }
  
  @Override
  public void warn(Marker marker, String format, Object arg1, Object arg2) {
    checkForEscalation(LoggingLevel.WARN, marker, format, arg1, arg2);
    
    logger.warn(marker, format, arg1, arg2);
  }
  
  @Override
  public void warn(Marker marker, String format, Object... arguments) {
    checkForEscalation(LoggingLevel.WARN, marker, format, arguments);
    
    logger.warn(marker, format, arguments);
  }
  
  @Override
  public void warn(Marker marker, String msg, Throwable throwable) {
    checkForEscalation(LoggingLevel.WARN, marker, msg, throwable);
    
    logger.warn(marker, msg, throwable);
  }
  
  @Override
  public boolean isErrorEnabled() {
    return logger.isErrorEnabled();
  }
  
  @Override
  public boolean isErrorEnabled(Marker marker) {
    return logger.isErrorEnabled(marker);
  }
  
  @Override
  public void error(String msg) {
    checkForEscalation(LoggingLevel.ERROR, msg);
    
    logger.error(msg);
  }
  
  @Override
  public void error(String format, Object arg) {
    checkForEscalation(LoggingLevel.ERROR, format, arg);
    
    logger.error(format, arg);
  }
  
  @Override
  public void error(String format, Object arg1, Object arg2) {
    checkForEscalation(LoggingLevel.ERROR, format, arg1, arg2);
    
    logger.error(format, arg1, arg2);
  }
  
  @Override
  public void error(String format, Object... arguments) {
    checkForEscalation(LoggingLevel.ERROR, format, arguments);
    
    logger.error(format, arguments);
  }
  
  @Override
  public void error(String msg, Throwable throwable) {
    checkForEscalation(LoggingLevel.ERROR, msg, throwable);
    
    logger.error(msg, throwable);
  }
  
  @Override
  public void error(Marker marker, String msg) {
    checkForEscalation(LoggingLevel.ERROR, marker, msg);
    
    logger.error(marker, msg);
  }
  
  @Override
  public void error(Marker marker, String format, Object arg) {
    checkForEscalation(LoggingLevel.ERROR, marker, format, arg);
    
    logger.error(marker, format, arg);
  }
  
  @Override
  public void error(Marker marker, String format, Object arg1, Object arg2) {
    checkForEscalation(LoggingLevel.ERROR, marker, format, arg1, arg2);
    
    logger.error(marker, format, arg1, arg2);
  }
  
  @Override
  public void error(Marker marker, String format, Object... arguments) {
    checkForEscalation(LoggingLevel.ERROR, marker, format, arguments);
    
    logger.error(marker, format, arguments);
  }
  
  @Override
  public void error(Marker marker, String msg, Throwable throwable) {
    checkForEscalation(LoggingLevel.ERROR, marker, msg, throwable);
    
    logger.error(marker, msg, throwable);
  }
  
}
