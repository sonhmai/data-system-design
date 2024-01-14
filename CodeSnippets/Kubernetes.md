
## Docker


## Kubernetes

```bash
# check if pods have right label
kubectl get pods --show-labels

# check if pods belong to several apps
kubectl get pods --selector any-name=my-app --show-labels

# test if a service works -> port-forward
# 3000 local port, 80 exposed service port
kubectl port-forward service/<service-name> 3000:80

# test if an ingress works -> port-forward
kgp -n ingress-nginx
kubectl describe pod nginx-ingress-controller-6fc5bcc \
 --namespace ingress-nginx \
 | grep Ports
# Ports:         80/TCP, 443/TCP, 18080/TCP
# connect to the pod
kubectl port-forward nginx-ingress-controller-6fc5bcc 3000:80 --n ingress-nginx
```

Debugging application issues, go bottom up
- Pod and Deployment
- Service
- Ingress

Debugging Pod Issues
```bash
kubectl logs <pod name>
# retrieve a list of events associated with the Pod
kubectl describe pod <pod name> 
#execute command in pod
kubectl exec -ti <pod name> -- bash
# inspect logs of cluster
kubectl get events --sort-by=.metadata.creationTimestamp
```

Ingress Nginx
```bash
# add namespace with -n if necessary
kubectl ingress-nginx lint # which checks the nginx.conf
kubectl ingress-nginx backend
kubectl ingress-nginx logs  # check logs
```

References
- [Visual guide on troubleshooting K8s deployments](https://learnk8s.io/troubleshooting-deployments)