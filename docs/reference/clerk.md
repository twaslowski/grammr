# Clerk

Grammr uses [Clerk](https://clerk.dev/) for authentication and user management. 
Clerk provides a simple and secure way to handle user sign-up, sign-in, and profile management.
This document outlines how to set up and configure Clerk for your Grammr deployment.

## Prerequisites

- A [Clerk](https://clerk.dev/) account. You can sign up for a free account if you don't have one.

### Setting Up Clerk

1. **Create a New Application**:
   - Log in to your Clerk dashboard.
   - Navigate to the "Applications" section and create a new application for your Grammr deployment.

2. **Configure Redirect URLs**:
   - In the application settings, set the redirect URLs to point to your Grammr instance. For example:
    `https://your-domain.com/callback`
   - Add any additional URLs needed for your application.

3. **Obtain API Keys**:
   - In the application settings, locate your API keys (Publishable Key and Secret Key). You can
      find these under the "Configure" tab of your application in the "API Keys" section.
   - 