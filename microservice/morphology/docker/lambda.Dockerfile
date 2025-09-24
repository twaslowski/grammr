FROM public.ecr.aws/lambda/python:3.12 AS builder

ARG SPACY_MODEL

RUN pip install poetry==1.4.2

ENV POETRY_NO_INTERACTION=1 \
    POETRY_VIRTUALENVS_IN_PROJECT=1 \
    POETRY_VIRTUALENVS_CREATE=1 \
    POETRY_CACHE_DIR=/tmp/poetry_cache

WORKDIR /app

COPY pyproject.toml poetry.lock ./

RUN poetry export -f requirements.txt -o requirements.txt && \
    pip install -r requirements.txt --target ${LAMBDA_TASK_ROOT} && \
    rm -rf $POETRY_CACHE_DIR


FROM public.ecr.aws/lambda/python:3.12

ARG SPACY_MODEL

COPY --from=builder ${LAMBDA_TASK_ROOT}/ ${LAMBDA_TASK_ROOT}/

COPY morphology/ ${LAMBDA_TASK_ROOT}/morphology/
COPY morphology/lambda_handler.py ${LAMBDA_TASK_ROOT}/

RUN python -m spacy download ${SPACY_MODEL}
ENV SPACY_MODEL=${SPACY_MODEL}

CMD [ "lambda_handler.handler" ]