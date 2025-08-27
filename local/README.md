If you want to use data from the production database (no personal data, so low risk),
use the following commands to import it into your local database.

This is my personal setup that assumes port-forwarding into a Kubernetes Pod,
for which `sslmode=disable` is required.

```bash
kubectl port-forward svc/grammr-pg-rw 5432:5432 -n grammr
pg_dump "postgresql://grammr:<password>@localhost:5432/grammr?sslmode=disable" -f grammr.tar --format tar
```

Then import it into your local database:

```bash
pg_restore -d "postgresql://user:password@localhost:5432/grammr?sslmode=disable" -f grammr.tar
```