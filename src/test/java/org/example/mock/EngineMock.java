package org.example.mock;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mock
 *
 * @author liu.xiaojian
 * @date 2023-01-10 10:55
 */
@Slf4j
public class EngineMock {

    private ProcessEngine engine;

    @Before
    public void config() {
        this.engine = new StandaloneProcessEngineConfiguration()
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&rewriteBatchedStatements=true")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .buildProcessEngine();
    }

    @Test
    public void deployment() {
        Deployment deployment = engine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .name("请假流程")
                .deploy();

        log.info("deployment id: {}, name: {}", deployment.getId(), deployment.getName());
    }

    @Test
    public void queryDeployment() {
        Deployment deployment = engine.getRepositoryService()
                .createDeploymentQuery()
                .deploymentId("1")
                .singleResult();
        log.info("deployment info: {}", deployment.toString());
    }

    @Test
    public void deleteDeployment() {
        engine.getRepositoryService()
                .deleteDeployment("1");
    }

    @Test
    public void startupProcess() {
        Map<String, Object> variables = new HashMap<>();

        variables.put("name", "张三");
        variables.put("count", 3);

        ProcessInstance instance = engine.getRuntimeService()
                .startProcessInstanceByKey("holidayRequest", variables);

        log.info("instance info: {}", instance.toString());
    }

    @Test
    public void queryTasks() {
        this.mockQueryTasks();
    }

    @Test
    public void completeTask() {
        List<Task> tasks = this.mockQueryTasks();

        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", false);

        tasks.forEach(task -> engine.getTaskService().complete(task.getId(), variables));
    }

    @Test
    public void queryHistory() {
        List<HistoricActivityInstance> historicActivityInstances = engine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();
        log.info("historic activity instances info: {}", historicActivityInstances);
    }

    public List<Task> mockQueryTasks() {
        List<Task> tasks = engine.getTaskService()
                .createTaskQuery()
                .processDefinitionKey("holidayRequest")
                .taskAssignee("leader")
                .list();

        log.info("tasks info: {}", tasks);

        return tasks;
    }

}
