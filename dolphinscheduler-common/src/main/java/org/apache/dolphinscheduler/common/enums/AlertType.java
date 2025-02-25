/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dolphinscheduler.common.enums;

import lombok.Getter;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * describe the reason why alert generates
 */
@Getter
public enum AlertType {

    /**
     * 0 workflow instance failure, 1 workflow instance success, 2 workflow instance blocked, 3 workflow instance timeout, 4 fault tolerance warning,
     * 5 task failure, 6 task success, 7 task timeout, 8 close alert
      */
    WORKFLOW_INSTANCE_FAILURE(0, "workflow instance failure"),
    WORKFLOW_INSTANCE_SUCCESS(1, "workflow instance success"),
    WORKFLOW_INSTANCE_BLOCKED(2, "workflow instance blocked"),
    WORKFLOW_INSTANCE_TIMEOUT(3, "workflow instance timeout"),
    FAULT_TOLERANCE_WARNING(4, "fault tolerance warning"),
    TASK_FAILURE(5, "task failure"),
    TASK_SUCCESS(6, "task success"),
    TASK_TIMEOUT(7, "task timeout"),

    CLOSE_ALERT(8, "the workflow instance success, can close the before alert");

    AlertType(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @EnumValue
    private final int code;
    private final String descp;

}
