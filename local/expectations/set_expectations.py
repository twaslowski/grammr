import os
import json
import urllib.request
import random
import math

chunk_size = 4

responses_template = 'responses.json'
expectation_template = 'expectation.json'
poems = ['kubla_khan.txt', 'earendil.txt']

def load_template(path: str) -> dict:
  with open(path) as f:
    return json.loads(f.read())


def create_expectation(body: dict) -> None:
    body = json.dumps(body).encode('utf-8')
    req = urllib.request.Request('http://localhost:1080/mockserver/expectation', data=body,
                                 headers={'Content-Type': 'application/json'}, method='PUT')
    response = urllib.request.urlopen(req)


if __name__ == '__main__':
    responses_template = load_template(responses_template)
    expectation_template = load_template(expectation_template)

    lines = []
    for poem in poems:
      with open(poem) as f:
        lines.extend(f.readlines())

    chunks = []
    for i in range(math.floor(len(lines) / chunk_size)):
      chunk_start = i*chunk_size
      chunk_end = chunk_start + chunk_size
      chunk = lines[chunk_start:chunk_end]
      chunks.append(chunk)

    for chunk in chunks:
      responses_template['output'][0]['content'][0]['text'] = ' '.join(chunk).strip()
      expectation_template[0]['httpResponse']['body'] = responses_template
      expectation_template[0]['httpResponse']['delay']['value'] = random.randint(1, 15) * 100
      create_expectation(expectation_template)

    print(f"Created {len(chunks)} mockserver expectations")


