# Helm
## Install helm
Install `helm` via `snap install helm` or `brew install brew`.

## Install chart
Use the following command from the project root to install the chart (residing in the `helm` directory, hence the third argument is `helm`; you could also use `.` inside this directory) with instance name `testing` in the `greeting` namespace (which is created if not existing). Override the default `values.yaml` with your values defined in `helm/values-override.yaml`. Authorization to Kubernetes is given by referencing `/var/lib/rancher/k3s/server/cred/admin.kubeconfig`.

Use `--dry-run` to just render the templates which would be installed.

`--atomic` ensures everything works (e.g. readniess probes); it rolls back the whole thing on a failure. Remove it if you are okay with installing e.g. a faulty pod (which e.g. does not yet manage readiness probes during chart development).

```
helm install testing helm --create-namespace --namespace greeting -f helm/values-override.yaml --kubeconfig /var/lib/rancher/k3s/server/cred/admin.kubeconfig --atomic
```

## Upgrade chart

To upgrade an already installed helm chart, use this:

```
helm upgrade testing helm --create-namespace --namespace greeting -f helm/values-override.yaml --kubeconfig /var/lib/rancher/k3s/server/cred/admin.kubeconfig --atomic
```

## Upgrade or install chart

Use `helm upgrade --install` to upgrade an existing release or install it if it does not yet exist.

```
helm upgrade testing helm --install --create-namespace --namespace greeting -f helm/values-override.yaml --kubeconfig /var/lib/rancher/k3s/server/cred/admin.kubeconfig --atomic
```