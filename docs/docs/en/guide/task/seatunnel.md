# Apache SeaTunnel

## Overview

`SeaTunnel` task type for creating and executing `SeaTunnel` tasks. When the worker executes this task, it will parse the config file through the `start-seatunnel-spark.sh` , `start-seatunnel-flink.sh` or `seatunnel.sh` command.
Click [here](https://seatunnel.apache.org/) for more information about `Apache SeaTunnel`.

## Create Task

- Click Project Management -> Project Name -> Workflow Definition, and click the "Create Workflow" button to enter the DAG editing page.
- Drag the <img src="../../../../img/tasks/icons/seatunnel.png" width="15"/> from the toolbar to the drawing board.

## Task Parameter

[//]: # (TODO: use the commented anchor below once our website template supports this syntax)
[//]: # (- Please refer to [DolphinScheduler Task Parameters Appendix]&#40;appendix.md#default-task-parameters&#41; `Default Task Parameters` section for default parameters.)

- Please refer to [DolphinScheduler Task Parameters Appendix](appendix.md) `Default Task Parameters` section for default parameters.
- Startup script: Select script name to start the task, including `seatunnel.sh`, `start-seatunnel-flink-13-connector-v2.sh`, `start-seatunnel-flink-15-connector-v2.sh`, `start-seatunnel-flink-connector-v2.sh`, `start-seatunnel-flink.sh`, `start-seatunnel-spark-2-connector-v2.sh`, `start-seatunnel-spark-3-connector-v2.sh`, `start-seatunnel-spark-connector-v2.sh`, `start-seatunnel-spark.sh`
- FLINK
- Run model: supports `run` and `run-application` modes
- Option parameters: used to add the parameters of the Flink engine, such as `-m yarn-cluster -ynm seatunnel`
- SPARK
- Deployment mode: specify the deployment mode, `cluster` `client`
- Master: Specify the `Master` model, `yarn` `local` `spark` `mesos`, where `spark` and `mesos` need to specify the `Master` service address, for example: 127.0.0.1:7077
- SEATUNNEL_ENGINE
- Deployment mode: specify the deployment mode, `cluster` `local`

  > Click [here](https://seatunnel.apache.org/docs/command/usage) for more information on the usage of Apache SeaTunnel command`

- Custom Configuration: Supports custom configuration or select configuration file from Resource Center

  > Click [here](https://seatunnel.apache.org/docs/concept/config) for more information about `Apache SeaTunnel config` file

- Script: Customize configuration information on the task node, including four parts: `env` `source` `transform` `sink`
- Custom Parameters/Global Parameters: When custom parameters/global parameters are defined, the parameters will be passed to the SeaTunnel task, and the parameter value can be dynamically replaced during task execution by referencing the parameter with `${}` in the SeaTunnel task.

  > Click [here](https://seatunnel.apache.org/docs/concept/config/#config-variable-substitution) for more information on `Apache SeaTunnel variable substitution`

## Task Example

This sample demonstrates using the Flink engine to read data from a Fake source and print to the console.

### Configuring the SeaTunnel environment in DolphinScheduler

If you want to use the SeaTunnel task type in the production environment, you need to configure the required environment first. The configuration file is as follows: `/dolphinscheduler/conf/env/dolphinscheduler_env.sh`.

![seatunnel_task01](../../../../img/tasks/demo/seatunnel_task01.png)

### Configuring SeaTunnel Task Node

According to the above parameter description, configure the required content.

![seatunnel_task02](../../../../img/tasks/demo/seatunnel_task02.png)

### Config example

```Config

env {
  execution.parallelism = 1
}

source {
  FakeSource {
    result_table_name = "fake"
    field_name = "name,age"
  }
}

transform {
  sql {
    sql = "select name,age from fake"
  }
}

sink {
  ConsoleSink {}
}

```

### Support SeaTunnel Version

- v2.3.1
- v2.3.2
- v2.3.3

