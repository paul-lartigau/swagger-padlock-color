package com.example.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ActuatorEndpoints {
    public static final String ACTUATOR = "/actuator";
    public static final String ACTUATOR_HEALTH = ACTUATOR + "/health";
    public static final String ACTUATOR_HEAP_DUMP = ACTUATOR + "/heapdump";
    public static final String ACTUATOR_INFO = ACTUATOR + "/info";
    public static final String ACTUATOR_SCHEDULED_TASKS = ACTUATOR + "/scheduledtasks";
    public static final String ACTUATOR_THREAD_DUMP = ACTUATOR + "/threaddump";
}
