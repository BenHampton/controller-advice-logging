# controller-advice-logging

### Create Jar
- clean install 
```bash
mvn clean install -B
```

### Create Docker image
- build image:
```bash
docker build -t controller-advice-logging .
```

- run docker:
```bash
docker run -p 8080:8080 -t controller-advice-logging
```

### Minikube
- start minikube
```bash
minikube start  
```

- list pods
```bash
kubectl get pods --all-namespaces
```