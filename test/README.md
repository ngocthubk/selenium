********* AWI Plugin - Action Builder Plugin *********
======================================================
UI test for Action Builder plugin using `selenium`, `TestNG`

## Prerequisites
1. Install `apm` with minimum version `2.0.0`
2. Setup `selenium`
    - Download selenium-server-standalone-x.x.x.jar from [here](https://repo.jenkins-ci.org/releases/org/seleniumhq/selenium/selenium-server-standalone/)
    - Start selenium server with role `hub`:
    ```
    java -jar selenium-server-standalone-x.x.x.jar -role hub
    ```
    - Register selenium with role `node` based on *hub URL*:
    ```
    java -jar selenium-server-standalone-x.x.x.jar -role node -hub <hub-url>
    ```

## Run test
Execute a command:
  ```
  java -DcfgFile=config.properties -Dprofile=smoke -jar webui-plugin-actionbuilder-2.1.0-SNAPSHOT-test.jar
  ```
  - `profile`: Test profile (`smoke`|`push`|`full`)
  - `cfgFile`: Specify the configuration file to run test. Default: `config/config.properties`

*Note*:
  - Take a look at `config.properties` for more details
  - Using configuration key in `config.properties` as system properties to overwrite parameters. For example: `-Dawi-conn=AE12 -Dselenium-browser=chrome`

## Test Output
- Default report test output is `test-output` folder that same directory level of test jar artifact. It's full report that used in development.
- `test-output/report.xml` is report output with JUnit format that used in dev2pro.
- Support `output` parameter when executing jar to overwrite output directory folder.

## Development process:
- Install `TestNG` plugin
- Clone `config.properites` to `local.config.properites` and fill appropriate values
- In Eclipse, configure `Debug as../Run as..` on `TestMain.java` with `VM arguments` with these values `-Ddev=true -DcfgFile=local.config.properites`
Or
- Run directly on each test case in `com.automic.ecc.testcase`
