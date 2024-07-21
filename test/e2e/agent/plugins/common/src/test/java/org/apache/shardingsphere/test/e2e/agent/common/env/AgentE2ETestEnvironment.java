/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.test.e2e.agent.common.env;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.spi.type.typed.TypedSPILoader;
import org.apache.shardingsphere.test.e2e.agent.common.container.ITContainers;
import org.apache.shardingsphere.test.e2e.agent.common.container.MySQLContainer;
import org.apache.shardingsphere.test.e2e.agent.common.container.ShardingSphereJdbcContainer;
import org.apache.shardingsphere.test.e2e.agent.common.container.ShardingSphereProxyContainer;
import org.apache.shardingsphere.test.e2e.agent.common.container.plugin.AgentPluginContainerFactory;
import org.apache.shardingsphere.test.e2e.agent.common.container.plugin.AgentPluginHTTPEndpointProvider;
import org.apache.shardingsphere.test.e2e.agent.common.fixture.executor.ProxyRequestExecutor;
import org.apache.shardingsphere.test.e2e.env.container.atomic.DockerITContainer;
import org.apache.shardingsphere.test.e2e.env.container.atomic.enums.AdapterType;
import org.apache.shardingsphere.test.e2e.env.container.atomic.governance.GovernanceContainer;
import org.apache.shardingsphere.test.e2e.env.container.atomic.governance.GovernanceContainerFactory;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Agent E2E test environment.
 */
@Slf4j
public final class AgentE2ETestEnvironment {
    
    private static final AgentE2ETestEnvironment INSTANCE = new AgentE2ETestEnvironment();
    
    private final AgentE2ETestConfiguration testConfig;
    
    @Getter
    private final Collection<String> actualLogs = new LinkedList<>();
    
    private DockerITContainer agentPluginContainer;
    
    private ITContainers containers;
    
    @Getter
    private String agentPluginURL;
    
    private ProxyRequestExecutor proxyRequestExecutor;
    
    private boolean initialized;
    
    private String mysqlImage;
    
    private String proxyImage;
    
    private String jdbcProjectImage;
    
    private AgentE2ETestEnvironment() {
        testConfig = AgentE2ETestConfiguration.getInstance();
        initContainerImage();
    }
    
    private void initContainerImage() {
        Properties imageProps = EnvironmentProperties.loadProperties("env/image.properties");
        proxyImage = imageProps.getProperty("proxy.image", "apache/shardingsphere-proxy-agent-test:latest");
        jdbcProjectImage = imageProps.getProperty("jdbc.project.image", "apache/shardingsphere-jdbc-agent-test:latest");
        mysqlImage = imageProps.getProperty("mysql.image", "mysql:8.0");
    }
    
    /**
     * Get instance.
     *
     * @return singleton instance
     */
    public static AgentE2ETestEnvironment getInstance() {
        return INSTANCE;
    }
    
    /**
     * Init environment.
     */
    public void init() {
        if (!AgentE2ETestConfiguration.getInstance().containsTestParameter()) {
            return;
        }
        if (AdapterType.PROXY.getValue().equalsIgnoreCase(testConfig.getAdapter())) {
            createProxyEnvironment();
        } else if (AdapterType.JDBC.getValue().equalsIgnoreCase(testConfig.getAdapter())) {
            createJDBCEnvironment();
        }
        log.info("Waiting to collect data ...");
        long collectDataWaitSeconds = testConfig.getCollectDataWaitSeconds();
        if (collectDataWaitSeconds > 0L) {
            Awaitility.await().ignoreExceptions().atMost(Duration.ofSeconds(collectDataWaitSeconds + 1L)).pollDelay(collectDataWaitSeconds, TimeUnit.SECONDS).until(() -> true);
        }
        agentPluginURL = null == agentPluginContainer ? null : new AgentPluginHTTPEndpointProvider().getHURL(agentPluginContainer, testConfig.getDefaultExposePort());
        initialized = true;
    }
    
    private void createProxyEnvironment() {
        containers = new ITContainers();
        MySQLContainer storageContainer = new MySQLContainer(mysqlImage);
        GovernanceContainer governanceContainer = GovernanceContainerFactory.newInstance("ZooKeeper");
        ShardingSphereProxyContainer proxyContainer = new ShardingSphereProxyContainer(proxyImage, testConfig.getPluginType(), testConfig.isLogEnabled() ? this::collectLogs : null);
        proxyContainer.dependsOn(storageContainer);
        proxyContainer.dependsOn(governanceContainer);
        Optional<DockerITContainer> pluginContainer = getAgentPluginContainer();
        pluginContainer.ifPresent(proxyContainer::dependsOn);
        pluginContainer.ifPresent(optional -> containers.registerContainer(optional));
        containers.registerContainer(storageContainer);
        containers.registerContainer(governanceContainer);
        containers.registerContainer(proxyContainer);
        containers.start();
        try {
            proxyRequestExecutor = new ProxyRequestExecutor(proxyContainer.getConnection());
            proxyRequestExecutor.start();
        } catch (final SQLException ignored) {
        }
    }
    
    private void createJDBCEnvironment() {
        containers = new ITContainers();
        Optional<DockerITContainer> pluginContainer = getAgentPluginContainer();
        MySQLContainer storageContainer = new MySQLContainer(mysqlImage);
        ShardingSphereJdbcContainer jdbcContainer = new ShardingSphereJdbcContainer(jdbcProjectImage, testConfig.getPluginType(), testConfig.isLogEnabled() ? this::collectLogs : null);
        jdbcContainer.dependsOn(storageContainer);
        pluginContainer.ifPresent(jdbcContainer::dependsOn);
        pluginContainer.ifPresent(optional -> containers.registerContainer(optional));
        containers.registerContainer(storageContainer);
        containers.registerContainer(jdbcContainer);
        containers.start();
    }
    
    private Optional<DockerITContainer> getAgentPluginContainer() {
        Optional<AgentPluginContainerFactory> agentPluginContainerFactory = TypedSPILoader.findService(AgentPluginContainerFactory.class, testConfig.getPluginType());
        if (agentPluginContainerFactory.isPresent()) {
            agentPluginContainer = agentPluginContainerFactory.get().create();
            return Optional.of(agentPluginContainer);
        }
        return Optional.empty();
    }
    
    private void collectLogs(final OutputFrame outputFrame) {
        if (!initialized) {
            actualLogs.add(outputFrame.getUtf8StringWithoutLineEnding());
        }
    }
    
    /**
     * Destroy environment.
     */
    public void destroy() {
        if (!AgentE2ETestConfiguration.getInstance().containsTestParameter()) {
            return;
        }
        if (null != proxyRequestExecutor) {
            proxyRequestExecutor.stop();
        }
        if (null != containers) {
            containers.stop();
        }
    }
}
