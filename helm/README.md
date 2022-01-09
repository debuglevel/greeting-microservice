# Helm
## Install helm
Install `helm` via `snap install helm` or `brew install brew`.

## Install chart
Use the following command from the project root to install the chart (residing in the `helm` directory, hence the third argument is `helm`) with instance name `testing` in the `greeting` namespace (which is created if not existing). Override the default `values.yaml` with your values defined in `helm/values-override.yaml`. Authorization to Kubernetes is given by referencing `/var/lib/rancher/k3s/server/cred/admin.kubeconfig`.

Use `--dry-run` to render the templates.

Use `--atomic` to rollback installation on failure.

```
helm install testing helm --create-namespace --namespace greeting -f helm/values-override.yaml --kubeconfig /var/lib/rancher/k3s/server/cred/admin.kubeconfig
```