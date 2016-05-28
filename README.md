# BacksideServletKS
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
docker-compose build && docker-compose run web ./test.sh
```
