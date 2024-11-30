import os
import subprocess
import sys


def main():
    spacy_models = os.getenv("SPACY_MODELS")
    if not spacy_models:
        print("SPACY_MODELS is not set. Exiting.")
        sys.exit(1)

    models = spacy_models.split(",")

    processes = []
    for model in models:
        process = subprocess.Popen(["python3", "-m", "spacy", "download", model])
        processes.append(process)

    for process in processes:
        process.wait()


if __name__ == "__main__":
    main()
