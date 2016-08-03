# backside-servlet-ks [![CircleCI](https://circleci.com/gh/wenzowski/backside-servlet-ks/tree/master.svg?style=svg)](https://circleci.com/gh/wenzowski/backside-servlet-ks/tree/master) [![](https://imagelayers.io/badge/wenzowski/backside-servlet-ks:0.6.1.svg)](https://imagelayers.io/?images=wenzowski/backside-servlet-ks:0.6.1 'Get your own badge on imagelayers.io')
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
docker-compose build --no-cache && docker-compose run web ./test.sh
```
