# Use the official Python image from the Docker Hub
FROM python:3.12-slim

RUN apt-get update && apt-get install -y gcc python3-dev

# Args setup
ARG LANGUAGE_CODE=""
ARG MODEL_NAME=""
ENV LANGUAGE_CODE=${LANGUAGE_CODE}

WORKDIR /app

COPY analysis/ /app
COPY pyproject.toml /app
COPY poetry.lock /app

RUN pip install poetry
RUN CC="clang" poetry install
RUN poetry run python -m spacy download ${MODEL_NAME}

# Expose the port FastAPI will run on
EXPOSE 8000

# Command to run the FastAPI server
CMD ["poetry", "run", "uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]