# Vercel Terraform Configuration

Terraform is used to create and maintain a Vercel project that deploys this application.
It maintains its domains and, more importantly, the environment variables, which could otherwise get convoluted.

1. Install the [Terraform CLI](https://developer.hashicorp.com/terraform/docs/cli/install/overview).
2. Retrieve a Vercel API token from the Vercel dashboard.
3. Retrieve the Clerk Secret key from the Clerk dashboard.
4. [Optional] Create a `.env` file in the root of the project with the following content:
   ```bash
   export TF_VAR_vercel_token=<your-vercel-token>
   export TF_VAR_clerk_secret_key_production="sk_live_<your-clerk-secret-key>"
   export TF_VAR_clerk_secret_key_preview="sk_test_<your-clerk-secret-key>"
   ```
5. Run the following command to create a new Vercel project:
   ```bash
    terraform init -backend-config=config/backend.hcl
    TF_VAR_vercel_token=$VERCEL_API_TOKEN terraform apply -var-file=config/preview.tfvars -var-file=config/production.tfvars
   ```
