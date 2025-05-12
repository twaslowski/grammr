#!/bin/sh

#!/bin/bash

if [ -z "$SYNC_USER1" ]; then
  echo "Error: SYNC_USER1 environment variable is not set (expected format user:pass)"
  exit 1
fi

# Start the Anki sync server with the provided user credentials
exec python -m anki.syncserver
