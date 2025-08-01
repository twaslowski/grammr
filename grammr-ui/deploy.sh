#!/bin/bash

# Vercel deploys every commit to the repository to the Preview branch.
# In its free tier, you cannot create a Custom Environment to track particular branches.
# You can, however, Ignore the Build Step by several mechanisms:
# https://vercel.com/docs/project-configuration/git-settings#ignored-build-step

echo "VERCEL_GIT_COMMIT_REF: $VERCEL_GIT_COMMIT_REF"

if [[ "$VERCEL_GIT_COMMIT_REF" == "develop" || "$VERCEL_GIT_COMMIT_REF" == "main"  ]] ; then
  # Proceed with the build
    echo "✅ - Build can proceed"
  exit 1;

else
  # Don't build
  echo "🛑 - Build cancelled"
  exit 0;
fi