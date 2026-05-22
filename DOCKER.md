# Docker Commands Reference

## Starting Services

### Start all services
```bash
docker compose up --build
```

### Start a single service (with its dependencies)
```bash
# Start only the API (also starts postgres since it depends on it)
docker compose up --build api

# Start only a specific micro-frontend (also starts api + postgres)
docker compose up --build mfe-spring
docker compose up --build mfe-qute
docker compose up --build mfe-htmlflow
docker compose up --build shell
```

### Start a single service without dependencies
```bash
docker compose up --build --no-deps api
```

### Start in detached mode (background)
```bash
docker compose up --build -d api
```

## Stopping Services

### Stop all services
```bash
docker compose down
```

### Stop and remove volumes (wipes database)
```bash
docker compose down -v
```

### Stop a single service
```bash
docker compose stop api
```

## Rebuilding

### Rebuild a single service (no cache)
```bash
docker compose build --no-cache api
```

### Rebuild all services (no cache)
```bash
docker compose build --no-cache
```

## Logs

### Follow logs for a service
```bash
docker compose logs -f api
```

### Follow logs for all services
```bash
docker compose logs -f
```

### Show last N lines
```bash
docker compose logs --tail=50 api
```

## Status & Debugging

### List running containers
```bash
docker compose ps
```

### Open a shell inside a running container
```bash
docker exec -it api /bin/sh
```

### Inspect a container
```bash
docker inspect api
```

## Cleanup

### Remove all stopped containers, unused networks, and dangling images
```bash
docker system prune
```

### Remove everything including unused images
```bash
docker system prune -a
```

## Services Overview

| Service       | Port | Depends On                          |
|---------------|------|-------------------------------------|
| postgres      | 5432 | —                                   |
| api           | 8080 | postgres                            |
| mfe-spring    | 8081 | api                                 |
| mfe-qute      | 8083 | api                                 |
| mfe-htmlflow  | 8084 | api                                 |
| shell         | 8082 | api, mfe-spring, mfe-qute, mfe-htmlflow |

## Quick Test: Stream Endpoint

After starting the API:
```bash
docker compose up --build api
```

Test the streaming order history:
```bash
curl http://localhost:8080/html-chunked/stream
```
