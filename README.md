# Nested test support with Spring Data

## Current status

It does not seem to work:

  - confirmed with Spring Data Neo4j 6.0.0 (in a private repo)
  - confirmed with this repo
  
## Reproduction one-liners

### Flat tests, it works 🥳

```shell
cd $(mktemp -d) && \
    git clone https://github.com/fbiville/spring-data-jpa-nested && \
    cd spring-data-jpa-nested && \
    git reset --hard HEAD^ && \
    mvn test
```

### Nested tests, it does not work 😿

```shell
cd $(mktemp -d) && \
    git clone https://github.com/fbiville/spring-data-jpa-nested && \
    cd spring-data-jpa-nested && \
    mvn test
```

