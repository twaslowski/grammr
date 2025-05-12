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

FROM python:3.12-slim

ENV VIRTUAL_ENV=/app/.venv \
    PATH="/app/.venv/bin:$PATH"

COPY --from=builder ${VIRTUAL_ENV} ${VIRTUAL_ENV}

ENV APP_DIR=/app

COPY docker/init.sh ${APP_DIR}/init.sh

EXPOSE 8080
WORKDIR ${APP_DIR}

# Command to run the FastAPI server
CMD /bin/sh init.sh