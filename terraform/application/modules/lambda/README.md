# lambda module

This module creates dockerized Lambdas with a keep-warm functionality as to ensure
that no cold-start time interferes with execution. This is particularly important because
the Lambdas would have to download language models (for most spaCy and inflection containers)
upon cold start which could take upwards of a minute.