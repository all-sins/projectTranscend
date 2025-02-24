# Project Transcend
Project Transcend is a Java Spring Boot application that interacts with a ComputerCraft turtle inside a Minecraft world which is effectively a LUA HTTP client, to save and serve embedded analytics on collected items and their metadata.

![projTranscend](https://github.com/all-sins/projectTranscend/assets/62400484/d7fae4fb-d29d-44d0-8d35-1df05d596fe9)

# How to
## Manual
1. Build and deploy.
```bash
./manualBuildAndDeploy.sh user@domain:///dir/to/project
```

2. Update credential and token values in setEnvVars.sh.
WEBHOOK-URL, DATASOURCE.PASSWORD and DATASOURCE.USERNAME.

2. Run.
./run.sh
