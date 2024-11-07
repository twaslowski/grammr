# The builder image, used to build the virtual environment
FROM python:3.11-buster as builder

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

# Set up dependencies
ENV VIRTUAL_ENV=/app/.venv \
    PATH="/app/.venv/bin:$PATH"

COPY --from=builder ${VIRTUAL_ENV} ${VIRTUAL_ENV}

COPY analysis/ ./analysis
COPY init.sh .

# Args setup
ENV LANGUAGE_CODE=""
ENV SPACY_MODEL=""

# Command to run the FastAPI server
CMD ["sh", "-c", "init.sh"]