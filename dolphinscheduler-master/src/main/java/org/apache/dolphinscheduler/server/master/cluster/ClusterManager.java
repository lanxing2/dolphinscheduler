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

package org.apache.dolphinscheduler.server.master.cluster;

import org.apache.dolphinscheduler.registry.api.RegistryClient;
import org.apache.dolphinscheduler.registry.api.enums.RegistryNodeType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClusterManager {

    @Getter
    private final MasterClusters masterClusters;

    @Getter
    private final WorkerClusters workerClusters;

    @Autowired
    private WorkerGroupChangeNotifier workerGroupChangeNotifier;

    @Autowired
    private RegistryClient registryClient;

    public ClusterManager() {
        this.masterClusters = new MasterClusters();
        this.workerClusters = new WorkerClusters();
    }

    public void start() {
        this.registryClient.subscribe(RegistryNodeType.MASTER.getRegistryPath(), masterClusters);
        this.registryClient.subscribe(RegistryNodeType.WORKER.getRegistryPath(), workerClusters);
        this.workerGroupChangeNotifier.subscribeWorkerGroupsChange(workerClusters);
        this.workerGroupChangeNotifier.start();
        log.info("ClusterManager started...");
    }

}
