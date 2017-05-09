[![CircleCI](https://circleci.com/gh/topicquests/backside-servlet-ks/tree/master.svg?style=shield)](https://circleci.com/gh/topicquests/backside-servlet-ks/tree/master)
[![VersionEye](https://www.versioneye.com/user/projects/589912711e07ae0046f590d2/badge.svg?style=shield)](https://www.versioneye.com/user/projects/589912711e07ae0046f590d2/)
# backside-servlet-ks
BacksideServlet for the new TQElasticKnowledgeSystem, replaces BacksideServlet

# Try it out
Use docker to build and boot.

```
docker-compose up --build
```

The BacksideServletKS api will be available on port 8080 at your docker host's ip.

# Tests
Use docker to run tests in an automatically-provisioned environment.

```
docker-compose build --no-cache && docker-compose run web mvn clean test -Dgpg.skip=true
```

# Building without Docker
Automatically download dependencies and build this project.
```
mvn clean install -DskipTests
```
