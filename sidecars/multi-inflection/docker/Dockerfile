# The builder image, used to build the virtual environment
FROM python:3.12-slim AS builder

RUN pip install poetry==1.4.2

ENV POETRY_NO_INTERACTION=1 \
    POETRY_VIRTUALENVS_IN_PROJECT=1 \
    POETRY_VIRTUALENVS_CREATE=1 \
    POETRY_CACHE_DIR=/tmp/poetry_cache

WORKDIR /app

COPY pyproject.toml poetry.lock ./

RUN poetry install --without dev --no-root && rm -rf $POETRY_CACHE_DIR

# Use the official Python image from the Docker Hub
FROM python:3.12-slim

RUN apt-get update && apt-get install -y curl

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl --fail --silent --show-error http://localhost:8000/health

# Set up dependencies
ENV VIRTUAL_ENV=/app/.venv \
    PATH="/app/.venv/bin:$PATH"

COPY --from=builder ${VIRTUAL_ENV} ${VIRTUAL_ENV}

ENV APP_DIR=/app

COPY inflection/ ${APP_DIR}/inflection
COPY docker/init.sh ${APP_DIR}/init.sh

ENV PYTHONPATH=${APP_DIR}
WORKDIR ${APP_DIR}

EXPOSE 8000

CMD /bin/sh init.sh