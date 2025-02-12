#!/bin/sh

python3 -m uvicorn anki.main:app --host=0.0.0.0 --port=8000