# Terraform

Terraform manages configuration and infrastructure for different parts of this project.
There are three main directories, each serving a specific purpose:

### 1. `shared/`
**Purpose:**

- Contains resources that are shared across different environments. Currently just
includes the ECR repository for all application images.

### 2. `application/`
**Purpose:** 
  - Provisions AWS Lambda functions for services like Morphology and Text-to-Speech (TTS).
  - Handles IAM roles, permissions, and any supporting infrastructure required by the application services.

### 3. `vercel/`
**Purpose:** Manages deployment and configuration of the Vercel project found at `frontend/`:
  - Configures the Vercel project itself, along with its DNS records
  - Configures environment variables for the Vercel project

---

## How to Use

1. **Initialize Terraform:**  
   Run `terraform init` in the desired directory to initialize the working directory.
   Most directories include different backends, so add `-backend-config=config/(dev|prod).hcl`. 

2. **Plan Changes:**  
   Run `terraform plan -var-file=config/(dev|prod).tfvar` to preview infrastructure changes.

3. **Apply Changes:**  
   Run `terraform apply` to provision or update resources.

---

## Notes

- Each directory is self-contained and can be managed independently.
- Shared resources should be applied before application or frontend resources to ensure dependencies (like ECR) are available.
