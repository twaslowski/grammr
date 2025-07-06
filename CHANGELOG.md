# Changelog

## [3.4.0](https://github.com/twaslowski/grammr/compare/v3.3.0...v3.4.0) (2025-07-06)


### Features

* **ops:** alert if morphology lambdas go down ([da06334](https://github.com/twaslowski/grammr/commit/da0633494b0fc23521ffc534c790eee1da7a818c))
* **ops:** enable health checks in ingress ([bdead57](https://github.com/twaslowski/grammr/commit/bdead57833874b4059c4bec593b619f5b6c3a431))


### Bug Fixes

* **flashcards:** deck and flashcard fetching from backend ([aeb0761](https://github.com/twaslowski/grammr/commit/aeb0761f6bf76b3de986745ebaa3358a375c5966))
* **flashcards:** deck and flashcard fetching from backend ([5e08770](https://github.com/twaslowski/grammr/commit/5e08770556808bd1d792b2febee7856c4cf3bd69))
* **ops:** prod ingress spec ([ea3067b](https://github.com/twaslowski/grammr/commit/ea3067b313d8ca64bbffb3e70a06d8e69beb2bd0))
* **serverless:** lambda permissions for ECR pull access ([48b39f7](https://github.com/twaslowski/grammr/commit/48b39f71b069d5757503d5fa10acee2c194bc2b3))
* **serverless:** provide SetRepositoryPolicy to morphology lambdas ([010996e](https://github.com/twaslowski/grammr/commit/010996e9e44d9fdce19aad7f65eab598f3ecfbe0))

## [3.3.0](https://github.com/twaslowski/grammr/compare/v3.2.0...v3.3.0) (2025-07-04)


### Features

* **core:** create rudimentary database monitoring ([79e549c](https://github.com/twaslowski/grammr/commit/79e549ce0e7f09f729234e8949657a68a0de368a))


### Bug Fixes

* **core:** environment variables ([1b2d9ab](https://github.com/twaslowski/grammr/commit/1b2d9ab889da5f5d94a35dc2a982417d20777416))
* **core:** provider configuration ([b405c1f](https://github.com/twaslowski/grammr/commit/b405c1f04e0df3c28e9b28d60eb4334693b60c18))
* ensure anonymous users are only created for chat requests ([5e054f1](https://github.com/twaslowski/grammr/commit/5e054f1108e2f33df830221fa7317e6a268175df))

## [3.2.0](https://github.com/twaslowski/grammr/compare/v3.1.0...v3.2.0) (2025-07-01)


### Features

* **observability:** create serverless dashboard ([80f1165](https://github.com/twaslowski/grammr/commit/80f1165dfc0567e069df1fb7d74b16185e416eb2))
* **ui:** fix spacing between tokens in analysis ([64b71e9](https://github.com/twaslowski/grammr/commit/64b71e9f8f9533c291f26149b69e12d667cd1af8))


### Bug Fixes

* **ui:** formatting issues ([4b10a56](https://github.com/twaslowski/grammr/commit/4b10a561bc1a126cdf96d7c1986e977a66149fb7))

## [3.1.0](https://github.com/twaslowski/grammr/compare/v3.0.0...v3.1.0) (2025-06-30)


### Features

* **anki:** improve flashcard api structure, enrich flashcard entity to enable stable sync process ([3c00c20](https://github.com/twaslowski/grammr/commit/3c00c2067e350fc7c27f69bfd4435118736e7be4))
* **auth:** enable anonymous user sessions ([a698fef](https://github.com/twaslowski/grammr/commit/a698fef3976491c9b597ec03cf027277c7dcaec9))
* **auth:** enable migration of anonymous users ([7cdd004](https://github.com/twaslowski/grammr/commit/7cdd0044aa09f1c13e503c09cc9ef6a1f778607c))
* **ops:** automatically deploy grammr via terraform ([f956f3d](https://github.com/twaslowski/grammr/commit/f956f3d689a82057dd66eb774fc90409f9e60f6f))
* **ops:** begin implementation of lambda handler for serverless multi-inflection image ([3be02d7](https://github.com/twaslowski/grammr/commit/3be02d7b1558496d3e83a5fbd3afea2da471c544))
* **ops:** create helm chart to allow performing backups ([2588cd7](https://github.com/twaslowski/grammr/commit/2588cd784cf5baa1aec2c2a1bc53a54b3fd3b377))
* **ops:** create s3 buckets for database backups ([6c32e30](https://github.com/twaslowski/grammr/commit/6c32e3008e25c8014963e554dff98ac170e8e9bb))
* **ops:** deploy anki-exporter ([40ff3fb](https://github.com/twaslowski/grammr/commit/40ff3fba89fd7ca98fb22970db3a9d0b43080cc7))
* **ops:** provide serverless implementations of multi-inflection images due to memory constraints ([590a71c](https://github.com/twaslowski/grammr/commit/590a71c7e3997da11b6eb9c9f6992bd2510b584c))
* **ops:** use terraform to manage application ([d1d2a20](https://github.com/twaslowski/grammr/commit/d1d2a20245139fb15b883997adb02eda13da2323))
* **quality:** separate functionalities around decks and flashcards more cleanly ([6e88402](https://github.com/twaslowski/grammr/commit/6e88402c7a35720417919b69dd3f0d40974c1dc8))
* solve deck ownership check via custom query to improve performance ([0db94ac](https://github.com/twaslowski/grammr/commit/0db94acab065d4f15f956194c78eb850f7597517))


### Bug Fixes

* **auth:** fix existing mockMvc tests ([87f1c80](https://github.com/twaslowski/grammr/commit/87f1c800c2e0f2ac22dd988cc57bf73515254832))
* backend secret reference ([7abeee6](https://github.com/twaslowski/grammr/commit/7abeee66946959eefe360ad4c3b67c4ad0d03769))
* helm provider config ([5747358](https://github.com/twaslowski/grammr/commit/574735859ff69ab4d22ab72b2cd7ef64815f171b))
* image sha values ([2c93e9e](https://github.com/twaslowski/grammr/commit/2c93e9ef5e392ce8eb9eb641869d0eac29ab2a99))
* **ops:** add missing tailscale auth secret ([214d3ab](https://github.com/twaslowski/grammr/commit/214d3ab10b7b3b27c688fcefcf475a968cd2f449))
* **ops:** db backup chart ([31e4341](https://github.com/twaslowski/grammr/commit/31e434148c340ac4451817f20b75d7dde3060c19))
* **ops:** db backup chart (2) ([ec7b394](https://github.com/twaslowski/grammr/commit/ec7b394add45238adc5c38884b66e7ea9b79c5b9))
* **ops:** install task for integration test step ([fe28867](https://github.com/twaslowski/grammr/commit/fe28867300d793dcc446159132c0caa8f0692e2d))
* **ops:** job dependencies ([644c0bd](https://github.com/twaslowski/grammr/commit/644c0bdeb10f08d0dbdbf6af5bc6973a024bc473))
* **ops:** pr pipeline yaml structure ([b8a7cc4](https://github.com/twaslowski/grammr/commit/b8a7cc4a698898311627f43359e72b372fe58618))
* **ops:** serverless workflow dependency structure ([66fed7c](https://github.com/twaslowski/grammr/commit/66fed7c2a16407e515253d280c9eb17cef79d55b))
* **ops:** serverless workflow triggers ([2091497](https://github.com/twaslowski/grammr/commit/20914974baa1baf102fa40af47b11afa71cda051))
* **ops:** serverless workflow version outputs ([2a86a3c](https://github.com/twaslowski/grammr/commit/2a86a3c1b4ea5a6cf1384bbe2ff8d4844889eb40))
* **ops:** set up nodejs before terraform in deployment ([13c6c0d](https://github.com/twaslowski/grammr/commit/13c6c0d1ba681f90bf4370f4fc79bcb6f47c9bb8))
* use in-cluster configuration to deploy ([2c2f9c5](https://github.com/twaslowski/grammr/commit/2c2f9c592b6795491cf48627a3d22f97e4a775f6))

## [3.0.0](https://github.com/twaslowski/grammr/compare/v2.3.1...v3.0.0) (2025-06-14)


### ⚠ BREAKING CHANGES

- Removes all previously deprecated `/v1/translate` and `/v1/analysis` endpoints and related services.
- Removes all usages of the now deprecated `theokanning/openai-java` library.

### Features

* **analysis:** update analyses lazily fetched translations ([d8256c5](https://github.com/twaslowski/grammr/commit/d8256c50217aa7aa106ee823e0e7750950a734eb))

## [2.3.1](https://github.com/twaslowski/grammr/compare/v2.3.0...v2.3.1) (2025-06-14)


### Bug Fixes

* **ops:** production deployment workflow ([368e19f](https://github.com/twaslowski/grammr/commit/368e19fc778562a8224b5e86a7fd9383bdb50c53))

## [2.3.0](https://github.com/twaslowski/grammr/compare/v2.2.2...v2.3.0) (2025-06-14)


### Features

* **analysis:** enable frontend-side analysis persistence ([3cf9f25](https://github.com/twaslowski/grammr/commit/3cf9f254a3b76eb924c2cca7e5a03d65daa8badb))
* **analysis:** enable persistence of message-&gt;analysis relation ([bf5f279](https://github.com/twaslowski/grammr/commit/bf5f2798e2f9b9e580f8f4d0dd044ed39e2f4574))
* **analysis:** lazily retrieve analyses when loading chat ([a04fe80](https://github.com/twaslowski/grammr/commit/a04fe80ceae9f6edcfff937970ca02018f1cb514))
* **analysis:** migrate /translate to v2 translation and analysis ([c183d67](https://github.com/twaslowski/grammr/commit/c183d67a6b3967e25db76eda69b8dd729842f43a))
* publish OpenAPI docs to GitHub pages ([7fc28cd](https://github.com/twaslowski/grammr/commit/7fc28cded975d3e44b99c04f89edb5ec278482c4))
* start implementing translation and analysis controllers to v2 ([1245a90](https://github.com/twaslowski/grammr/commit/1245a90922071d3176bcd601d8bc8328b399e205))
* **translations:** create v2 word translation service ([06ffff5](https://github.com/twaslowski/grammr/commit/06ffff5ccf15155c0c940fbb6fcc193648c9d1f5))
* **translations:** enable easy analysis of translated sentence ([c1b1c95](https://github.com/twaslowski/grammr/commit/c1b1c95e5d5b6677c0ba6321f91d120988d03e0c))


### Bug Fixes

* Github Token secret reference ([21e87d6](https://github.com/twaslowski/grammr/commit/21e87d698623aa5b6fc60d168ca13529388b85c2))
* perform replacement in swagger-initializer.js ([1ee2405](https://github.com/twaslowski/grammr/commit/1ee24050ccf29ea7e27b56764da2f23e80b53347))
* start postgresql so Spring can run properly ([37545d8](https://github.com/twaslowski/grammr/commit/37545d8b2cbb52bdf0029de617ff00f7578e2c29))
* supply placeholder openai api key ([d506c24](https://github.com/twaslowski/grammr/commit/d506c24db5562a6bdf367d1702c010e53bdb1735))

## [2.2.2](https://github.com/twaslowski/grammr/compare/v2.2.1...v2.2.2) (2025-06-11)


### Bug Fixes

* favicon site.manifest format ([611e7a6](https://github.com/twaslowski/grammr/commit/611e7a69dbf56a3123ea1b1fe67fe63319ace02c))

## [2.2.1](https://github.com/twaslowski/grammr/compare/v2.2.0...v2.2.1) (2025-06-11)


### Bug Fixes

* **ui:** mobile ChatHistory display ([74b3cb9](https://github.com/twaslowski/grammr/commit/74b3cb9f3326bf379486a1410d94c6e92162299f))

## [2.2.0](https://github.com/twaslowski/grammr/compare/v2.1.0...v2.2.0) (2025-06-10)


### Features

* **ops:** separate core deployment workflows ([ef6fd04](https://github.com/twaslowski/grammr/commit/ef6fd04982710770c29bc72c8eb566a751f55674))

## [2.1.0](https://github.com/twaslowski/grammr/compare/v2.0.0...v2.1.0) (2025-06-10)


### Features

* **chat:** add entities, repositories for chat; v2 chat controller ([f3a2546](https://github.com/twaslowski/grammr/commit/f3a25465eeb76b96325d96986aa1fb32b9638a37))
* **chat:** add user-level chat retrieval ([dce89ce](https://github.com/twaslowski/grammr/commit/dce89ce61240aa7133798d17144ea6c397cd5a36))
* **chat:** create integration test utilizing Wiremock ([e4030f9](https://github.com/twaslowski/grammr/commit/e4030f91e45f93d36d6e414e61f75734ffa7fa73))
* **chat:** enable chat message retrieval ([1eb9b8e](https://github.com/twaslowski/grammr/commit/1eb9b8e9e0f77b0690582ab44625aaff4be8b57c))
* **chat:** improve chatWindow responsiveness ([60dcd39](https://github.com/twaslowski/grammr/commit/60dcd39d5cb726b5fcb8f829686f371c7e760457))
* **chat:** track token usage, visualize messages properly ([fdd714e](https://github.com/twaslowski/grammr/commit/fdd714e519bdff5b3fcc1b443a9f2e2378d0cd58))
* **ops:** parameterize namespace and release in rollback workflow ([4e13d54](https://github.com/twaslowski/grammr/commit/4e13d54c7c719326d73d1f3387bf618c151e9ac7))
* **tts:** ensure high-quality text-to-speech output ([eba3775](https://github.com/twaslowski/grammr/commit/eba37756d98253e55ac84efb2af262b6d449796a))
* **tts:** provide text-to-speech lambda behind API Gateway ([55ccfcf](https://github.com/twaslowski/grammr/commit/55ccfcfe8c1caeaa435aea02be3a2892cb20e3ba))


### Bug Fixes

* also drop decks belonging to legacy users ([abc4066](https://github.com/twaslowski/grammr/commit/abc4066fd927dcb9f61b57a63d04e273a69d0400))
* **chat:** InputArea message sending ([0a1c898](https://github.com/twaslowski/grammr/commit/0a1c898bd5293138052e9dbe74ad4fdf6e3e99ea))
* **chat:** scripting issues; remove wiremock ([3a9609e](https://github.com/twaslowski/grammr/commit/3a9609e7f1ed2c26691f132e6d49e1aea6f62af9))
* linter issues ([313e331](https://github.com/twaslowski/grammr/commit/313e33119046503d58f80198ca11743585b861dc))
* **ops:** bump aws terraform provider versions ([25417ef](https://github.com/twaslowski/grammr/commit/25417efe79a6121f44cf47309b1c72a1714b7e23))
* **ops:** vercel environment variables, lambda morphology deployment ([0c5d5d1](https://github.com/twaslowski/grammr/commit/0c5d5d1e8169d43098f4fe14c86495e0e4c80df5))
* **ops:** workflow conditions; ts linter issues ([d91976c](https://github.com/twaslowski/grammr/commit/d91976c036924f589df8500cc0734332c9b75f94))
* **tts:** text-to-speech api endpoint ([e261005](https://github.com/twaslowski/grammr/commit/e26100559f9b6f348b34a364a5a28d3a864b3c02))

## [2.0.0](https://github.com/twaslowski/grammr/compare/v1.2.0...v2.0.0) (2025-06-03)


### ⚠ BREAKING CHANGES

* implement chat functionality ([#25](https://github.com/twaslowski/grammr/issues/25))

### Features

* implement chat functionality ([#25](https://github.com/twaslowski/grammr/issues/25)) ([fc76732](https://github.com/twaslowski/grammr/commit/fc76732826bf85678f814127ac03305c8c527755))

## [1.2.0](https://github.com/twaslowski/grammr/compare/v1.1.0...v1.2.0) (2025-05-21)


### Features

* **ui:** improve user login component responsiveness ([d22c63f](https://github.com/twaslowski/grammr/commit/d22c63f133ec9effcdf41ced482c0bf9cd168028))


### Bug Fixes

* **ui:** anki deck sync endpoint ([caf5d00](https://github.com/twaslowski/grammr/commit/caf5d0083989540324263cdd2ba1bd09bc63f79a))
* **ui:** anki deck sync endpoint (2) ([ac92ddc](https://github.com/twaslowski/grammr/commit/ac92ddcba5afe320498d835525b372820c3fff4e))
* **ui:** linting issues ([f35eb2d](https://github.com/twaslowski/grammr/commit/f35eb2dc2341d770d2d67741e555b0fbcac027ba))
* **ui:** path of export api ([2d8734d](https://github.com/twaslowski/grammr/commit/2d8734d32c42bb5492cbae77c376cefbfe89164f))
* **ui:** remove obsolete 'credentials: include' from export button ([41c15b4](https://github.com/twaslowski/grammr/commit/41c15b4e4831ce01b0519168c4ef5cf7bde40abb))

## [1.1.0](https://github.com/twaslowski/grammr/compare/v1.0.0...v1.1.0) (2025-05-18)


### Features

* **ops:** increase helm deploy timeouts ([7747ec3](https://github.com/twaslowski/grammr/commit/7747ec33df1f16b070008cea07c5e6ad670c6322))


### Bug Fixes

* **core:** change paradigm fetching on flashcards to EAGER ([15860b5](https://github.com/twaslowski/grammr/commit/15860b59f3aca21ffc95b076f53ad58d67a7b3f2))

## 1.0.0 (2025-05-16)


### Features

* add conjugation for fr, it, es, pt and ro ([fc57987](https://github.com/twaslowski/grammr/commit/fc57987c1f639b4654bb987e113d186ca983cb2f))
* add cors configuration ([9f927b1](https://github.com/twaslowski/grammr/commit/9f927b1fc45282b6b168661dedb87e65059cc13f))
* add debug mode, fix tests ([fc26618](https://github.com/twaslowski/grammr/commit/fc2661857cb7a4e672a3c35f8ebc2afc4130ab0b))
* add deck and flashcard retrieval ([71f27e9](https://github.com/twaslowski/grammr/commit/71f27e9b08d05c33648b2c669ba71ea092ff2f73))
* add documentation to the README.md ([31625cd](https://github.com/twaslowski/grammr/commit/31625cd42ba092fadf05cdf6150b587040545410))
* add inflection controller ([f08ae25](https://github.com/twaslowski/grammr/commit/f08ae25e089f45ea73247cdf68b5ac0e554d792d))
* add more contents to the readme ([5201141](https://github.com/twaslowski/grammr/commit/52011417f7769efc964d1b77afe275211620f57c))
* add more languages; increase deployment timeouts ([db6abf8](https://github.com/twaslowski/grammr/commit/db6abf83da08e0f70f7955a30003fc9b07a9fe66))
* add part-of-speech tags to morphological analysis ([6a1da15](https://github.com/twaslowski/grammr/commit/6a1da15bd07ca9e0a15addee03ae97392e63e4c2))
* add prometheus scrape config to core pods; increase replica count ([8fce359](https://github.com/twaslowski/grammr/commit/8fce359e8b85ad8cd6354b1b976e8725ddd89ce8))
* add prototypical russian inflections via pymorphy2 ([66af570](https://github.com/twaslowski/grammr/commit/66af570104ea6ee345b600b9c0859e41e77186bd))
* add Python spaCy sidecar for grammar analysis ([2339193](https://github.com/twaslowski/grammr/commit/23391938b526eb76041acb54a8fd8f16f5d8b14b))
* add secondary rest endpoint for translation+analysis ([5c38839](https://github.com/twaslowski/grammr/commit/5c38839dd51969ba392ac11da84dea6e6beb4a0d))
* add Telegram bot, event structure and configuration ([60eca51](https://github.com/twaslowski/grammr/commit/60eca51113ec732fc49c98935f8f83d8183b38d4))
* add terraform plan; fix: docker image --provenance option for ecr ([59c78fe](https://github.com/twaslowski/grammr/commit/59c78fea29057b9782b2c8451aaf70fb04d7a38d))
* add tests for inflection service; build docker image in pipeline ([31bf881](https://github.com/twaslowski/grammr/commit/31bf8813671283f2ca396fd33e3e300e6585df5f))
* allow users to set spoken/learned languages ([8120c31](https://github.com/twaslowski/grammr/commit/8120c31fab7c26948534713ae7cf1eb2c591673f))
* **analysis:** enable reverse translations from spoken to learned language ([b3c0ba8](https://github.com/twaslowski/grammr/commit/b3c0ba8a6c2f3289682a7127d1250892fd16a53e))
* **anki:** add empty test to pass deployment workflow ([235ddf8](https://github.com/twaslowski/grammr/commit/235ddf8fc51edc6069b8bf97af03384af847742a))
* **anki:** add pvc claim and volume mount ([907a984](https://github.com/twaslowski/grammr/commit/907a984e309b40bab7973aa165985f528573facf))
* **anki:** create and deploy anki-exporter chart ([45fab5d](https://github.com/twaslowski/grammr/commit/45fab5d4f3190217e3a99193f807c75b721113ca))
* **anki:** create anki-sync-server helm chart ([6e3c285](https://github.com/twaslowski/grammr/commit/6e3c28548b0bcc094a4a8face9ded5d7fbc48fce))
* **anki:** fix anki-exporter host in application config ([4ebd770](https://github.com/twaslowski/grammr/commit/4ebd7709eb93a6c816603184ed824c82213485ad))
* **anki:** hardcode volume and mounts in deployment.yaml ([b90e49a](https://github.com/twaslowski/grammr/commit/b90e49a6f3b63dde0b429ef3cb5cb558838f2a3d))
* **auth:** delegate authentication and user-management to third-party tools ([fdf8ebf](https://github.com/twaslowski/grammr/commit/fdf8ebf678fe3e4b39742ec1a7b372237f5c6f80))
* **auth:** make jwt authentication more lenient; cleanups ([ed9a622](https://github.com/twaslowski/grammr/commit/ed9a622dc9cc108839b217917a1ebc64ba3dc71c))
* **auth:** remove session-based auth, include clerk sdk ([2fefb97](https://github.com/twaslowski/grammr/commit/2fefb9705a6fce9ef0b2541fd62dc5e0d1c59acd))
* **auth:** use email as primary means of authentication ([c928afd](https://github.com/twaslowski/grammr/commit/c928afd5d79db02d29e4e4616f84d7a4c652a8cc))
* **auth:** validate JWTs using stored public key ([3fc1260](https://github.com/twaslowski/grammr/commit/3fc1260f21efb03c972a6ebbd977ddb2493b805c))
* automatically apply terraform configuration ([bae24a0](https://github.com/twaslowski/grammr/commit/bae24a05c0b661593c4957199aaff9b331421ce0))
* build jar in unit tests ([8d8382f](https://github.com/twaslowski/grammr/commit/8d8382f6505cec9a82922f62461467637f04eba4))
* calculate usage ([6c9ab37](https://github.com/twaslowski/grammr/commit/6c9ab37470eecbb663d64416ceb68402e8861f94))
* change python test directories ([6e41341](https://github.com/twaslowski/grammr/commit/6e41341d04f09cf8489d6815aff54fa9578c28c9))
* **ci:** set up releaseplease ([6d9f571](https://github.com/twaslowski/grammr/commit/6d9f571ee1fb0da1b386e068500a870cc93c05c3))
* compile Telegram code, openAI configuration, language recognition, benchmarking ([7c23767](https://github.com/twaslowski/grammr/commit/7c237671a7d0de1cff5db03300cff06c6d95ce8a))
* complete authentication feature ([acb9866](https://github.com/twaslowski/grammr/commit/acb98661a2af210b963a40fce66cffe4b21188f5))
* **core:** enable cloudflare controller for prod as well ([edcccaf](https://github.com/twaslowski/grammr/commit/edcccafb2d002f00573c2d5aca8a8d6e390bee2a))
* **core:** enable cloudflare controller ingress for dev ([f602275](https://github.com/twaslowski/grammr/commit/f602275cbd13562a7cae082a692956706359a85f))
* correctly create deployments for each language ([506e1b1](https://github.com/twaslowski/grammr/commit/506e1b18818fd754ee606fd5eecbaa046a33d033))
* cosmetic changes to text rendering, improve error handling ([71751c1](https://github.com/twaslowski/grammr/commit/71751c10a29d80a382e44fbd499f80de49a1ca46))
* create a controller to eventually decouple telegram and core functionality ([e883a26](https://github.com/twaslowski/grammr/commit/e883a2631d40a079ba271e3e1e777fd17a00f37b))
* create a shared AnalysisComponent abstraction ([fb334b1](https://github.com/twaslowski/grammr/commit/fb334b13c745b3d3aeaaa7fa278b40884748dc0a))
* create a solid class structure, create semantic translation service ([26705d9](https://github.com/twaslowski/grammr/commit/26705d9e40fe326e235f218b3c86b0103a0542f7))
* create api gateway for morphology; ecr repository; deployment workflow ([48ea194](https://github.com/twaslowski/grammr/commit/48ea1948853d49662c9e1687ca16bb5a11f9fa5e))
* create basic grammatical analysis capability ([506046c](https://github.com/twaslowski/grammr/commit/506046c16bee45c4d69e9678cfcb85855fa5c01f))
* create boilerplate for creating grammatical analyses ([287d35e](https://github.com/twaslowski/grammr/commit/287d35e86f1db164891a0580034ab8cc30ec67a1))
* create capabilities for flashcard and deck management ([f568f11](https://github.com/twaslowski/grammr/commit/f568f115de9c59e92e681e91944870ee8dcf88f3))
* create capability for literal translations ([a6ddd03](https://github.com/twaslowski/grammr/commit/a6ddd03ab8faacb2e59e8f45b983259dff41c850))
* create charts ([6c2875f](https://github.com/twaslowski/grammr/commit/6c2875f935aef926de208a8119e95d2ff018311f))
* create complete rabbitmq setup ([c98a0d3](https://github.com/twaslowski/grammr/commit/c98a0d3f687c4663dc669b6680fa9ce0c67c7440))
* create deployment workflow ([3e38516](https://github.com/twaslowski/grammr/commit/3e3851635c5c684f3ab919b5e6b2856efd84b3ee))
* create feature data structures ([04ef2bc](https://github.com/twaslowski/grammr/commit/04ef2bc94ba825d4ea8ee565de11a13a0acec177))
* create full inflection sidecar; configure application via external configmap ([889284f](https://github.com/twaslowski/grammr/commit/889284f0b6db6b9dd37f9bde65eefff8ae69ba40))
* create functioning inflection-de service ([1a32800](https://github.com/twaslowski/grammr/commit/1a3280008b9e15facd30989542128e80917bdc77))
* create more secure datasource password ([a7f562a](https://github.com/twaslowski/grammr/commit/a7f562ab4c2830fc5bd588d9fb4f00c213be6bce))
* create morphology feature extraction ([a77588e](https://github.com/twaslowski/grammr/commit/a77588eb8720c7cc0f4f225a83a7973e5c78170f))
* create multi-language inflection image ([c4e62ef](https://github.com/twaslowski/grammr/commit/c4e62ef6de2d875142447a83695824549424b3fc))
* create one morphological analysis sidecar deployment per language ([2741a62](https://github.com/twaslowski/grammr/commit/2741a6216978bfc2b1cfc7e9e1f37fd180ff0f4c))
* create preliminary inflection service using derbi ([c9eb9fb](https://github.com/twaslowski/grammr/commit/c9eb9fb0d4bf25699f084587599dd56c3709d00d))
* create queues for analysis requests ([ed9e3f7](https://github.com/twaslowski/grammr/commit/ed9e3f7294560091d8d4ca64a3d492fcfe04599a))
* create scripts ([2ac86f7](https://github.com/twaslowski/grammr/commit/2ac86f7bb4b6db57c3376f3ad0a823b0778ef9e9))
* create text-to-speech endpoint at /speak ([dded609](https://github.com/twaslowski/grammr/commit/dded60971abd259550c05e48943bca7811b7f986))
* create user entity ([c615d8a](https://github.com/twaslowski/grammr/commit/c615d8af37f2e93724efec5e35366935d6364318))
* create working helm deployment ([c8ff2ee](https://github.com/twaslowski/grammr/commit/c8ff2eebbab7d8ac2807473cb5defc3faa1371ca))
* create working inflection-ru subchart ([5975a42](https://github.com/twaslowski/grammr/commit/5975a424a61981c2514c9c6026b99add982d967f))
* derive terraform configuration from values.yaml ([84bb210](https://github.com/twaslowski/grammr/commit/84bb210a2713c101d18729eb9dc96e3d94178a9b))
* do not run integration tests in pipeline for now ([eb24baa](https://github.com/twaslowski/grammr/commit/eb24baa81c891de4ab6bf9f395f0158f7f90fb29))
* dockerization of analysis sidecar ([abe6553](https://github.com/twaslowski/grammr/commit/abe6553d36d09e4c1fa6df377a117c9304beb591))
* **documentation:** add architecture diagram ([30b88d0](https://github.com/twaslowski/grammr/commit/30b88d036da46aaa193011eabfae8d03a6aaf94b))
* **documentation:** add monitoring stack ([4b8fd9f](https://github.com/twaslowski/grammr/commit/4b8fd9fe9b5afdc17967de55412859cf04c8441f))
* **documentation:** complete architecture diagram ([c28b3bd](https://github.com/twaslowski/grammr/commit/c28b3bd67be374377afeca10660e9bff70ae9690))
* enable audio transcription and analysis ([c860804](https://github.com/twaslowski/grammr/commit/c860804f5a432cfcd9720af0642ff50ec4dcf57b))
* enable automated deployment ([742e460](https://github.com/twaslowski/grammr/commit/742e4608aa4a7e54d567102c27ddb7bda9bf41e4))
* enable handling of audio files ([23c9b9d](https://github.com/twaslowski/grammr/commit/23c9b9ddd3cbe4d404ce2ca089dd96902b0dad46))
* enable literal translation into other languages ([57cdf3b](https://github.com/twaslowski/grammr/commit/57cdf3b19c99ef40c67c9b994ceda5f7c84da718))
* ensure end-to-end functionality for analysis ([38212f4](https://github.com/twaslowski/grammr/commit/38212f414ed86d14b5a791270c2caeed573acbdf))
* fix a bad prompt, improve debug message ([c49bfc2](https://github.com/twaslowski/grammr/commit/c49bfc2d186030b907d99e89c67a3916aa28d50a))
* fix readme ([f6d6b5f](https://github.com/twaslowski/grammr/commit/f6d6b5f0a9b07648b19ec7581e4b2c0f188728ba))
* fix request handling ([5570987](https://github.com/twaslowski/grammr/commit/5570987978d763a2006eecd724a110f9c1a6136e))
* **flashcards:** enable flashcard deletion ([69ed484](https://github.com/twaslowski/grammr/commit/69ed484f37e2385693dc15a7539febe245649b36))
* improve inflection error handling ([f73b943](https://github.com/twaslowski/grammr/commit/f73b943777baff2e22569eea4a14cb45e71a4e7d))
* improve keep-warm mechanism; add test ([575a4ac](https://github.com/twaslowski/grammr/commit/575a4ac137f3dbb7b5f287de35208135cf1fe2b1))
* improve model download process ([78d13aa](https://github.com/twaslowski/grammr/commit/78d13aa05c3b55377c0b9e1d37764f30e0a02fba))
* improve observability, include lemmata in analysis ([790ab2f](https://github.com/twaslowski/grammr/commit/790ab2f7761eac567f035bc001b6a404707ec588))
* initial commit ([1328a28](https://github.com/twaslowski/grammr/commit/1328a280fbe26f0867558ac1597850d9ba50edbc))
* initial commit for inflection sidecar ([f8b2511](https://github.com/twaslowski/grammr/commit/f8b251125762bd5351fc81aeeb9e39da56897068))
* introduce serverless implementation of morphology service ([eeba233](https://github.com/twaslowski/grammr/commit/eeba2332037a8f06b58db18b3f1897cd7bbbe999))
* introduce word translations api ([2ffafef](https://github.com/twaslowski/grammr/commit/2ffafefadc236f0b24d8a25aa6644b32134843d4))
* **logging:** add request logging filter ([e672d13](https://github.com/twaslowski/grammr/commit/e672d13a93d96ddea0236f29e9d86c3ba10b69af))
* **logging:** simplify log format for prod ([0810de9](https://github.com/twaslowski/grammr/commit/0810de981ccf14baef50724b40097b17de01f478))
* **MC:** fix address resolution ([95a6859](https://github.com/twaslowski/grammr/commit/95a685948964fb8532e1f49c495528859d81ceb2))
* **ops:** switch from coretto to eclipse-temurin for image size ([8aad78a](https://github.com/twaslowski/grammr/commit/8aad78a62c37e68693b4905fc192cef8867e0722))
* pass audio files to openai api ([e48747a](https://github.com/twaslowski/grammr/commit/e48747a82aa311661f2130946213a5897ba2bf88))
* perform user creation on /start command ([8bbbbe3](https://github.com/twaslowski/grammr/commit/8bbbbe3f48bb0bceda3f1540e2a8e6237641f53b))
* prettify output ([4df838f](https://github.com/twaslowski/grammr/commit/4df838f552862b5881a83dc2411188db4786c390))
* properly mock openai api ([7b3c0d7](https://github.com/twaslowski/grammr/commit/7b3c0d75627584c79a3806ecf2c5f907dd2a73a7))
* refactor for a clearer domain language ([784b84d](https://github.com/twaslowski/grammr/commit/784b84d8fefeb63db7fa66229350009a73c9d785))
* refactor openai services, add benchmarks ([51039e3](https://github.com/twaslowski/grammr/commit/51039e3fe04eb12680ffb70ed134ac064b43d4f0))
* refactor package structure, consolidate tokens after analysis ([021ad3f](https://github.com/twaslowski/grammr/commit/021ad3f3c96ffa113a834dd0fd50e002df6f1645))
* refactor package structure, rename some classes, add unit tests ([f255b3b](https://github.com/twaslowski/grammr/commit/f255b3b30e97edccacf65b534a4dd4ea2dd53652))
* refactor phrase processing ([01e64a2](https://github.com/twaslowski/grammr/commit/01e64a2fde51f4bdfd1845d31b2b52e2d28e4f22))
* **reliability:** create different AWS environments ([921a2dc](https://github.com/twaslowski/grammr/commit/921a2dcef839a41ce92e02ba3afc6fd2bb6b17cd))
* **reliability:** create staging environment in separate namespace ([f1f734b](https://github.com/twaslowski/grammr/commit/f1f734b30dd6fb163e82543f5ca391555b290d09))
* **reliability:** dependencies of serverless deployment workflows ([7758b24](https://github.com/twaslowski/grammr/commit/7758b242a8507fcdbbd94c0806b51f7bf3c18dc5))
* **reliability:** separate lambda deployment from ecr repository creation ([f47584e](https://github.com/twaslowski/grammr/commit/f47584e72d92a877d8f437e9f20b4a9f57a2b9bb))
* remove access constraints to prometheus endpoint ([325a161](https://github.com/twaslowski/grammr/commit/325a1613f70dfd1de1cd2a41dec28fefd08349dd))
* remove inflection stringification test ([58e5fb0](https://github.com/twaslowski/grammr/commit/58e5fb05253fd0b004b3c76edfc374a9e1479591))
* remove telegram; add controllers for anki ([902075f](https://github.com/twaslowski/grammr/commit/902075f9357ffea482d73518e203aed4418e175c))
* Rename "Analysis" to "FullAnalysis" for clarity ([990ec71](https://github.com/twaslowski/grammr/commit/990ec716953e6567312fc05e6913c50a9a555259))
* return tokens in a strictly ordered manner ([f9c5526](https://github.com/twaslowski/grammr/commit/f9c55260d488d761cdeea732d787a79ac98e9d01))
* revert rabbitmq ([bcbff35](https://github.com/twaslowski/grammr/commit/bcbff357a4995bc3d4c6eece3001ebed283b0f0c))
* simplify semantictranslation prompt ([f05e31c](https://github.com/twaslowski/grammr/commit/f05e31c370246adeb9df175fe8782355c7053974))
* split grammr-core into multiple helm charts ([cb5784f](https://github.com/twaslowski/grammr/commit/cb5784faa6027b62cbc400e7f91a7f5b66c13a55))
* start enabling reverse translation if sentence is supplied in spoken language ([ab06f78](https://github.com/twaslowski/grammr/commit/ab06f781df17c1668917e3bf7f33e568d3602cd0))
* streamline deployment; remove terraform ([4b5d1da](https://github.com/twaslowski/grammr/commit/4b5d1da10e557672f2df4d441356d8281d111b11))
* stringify and send complete token morphology ([b0cb651](https://github.com/twaslowski/grammr/commit/b0cb6512d61001fa34d47c4bb191c4c207c54a39))
* successfully transcribe audio, perform cleanups ([48685c7](https://github.com/twaslowski/grammr/commit/48685c715b0270412fb8b2d033f984be4e5f979d))
* support multiple languages in morphological analysis ([7be6bb0](https://github.com/twaslowski/grammr/commit/7be6bb097e4e56585aee656d8f15ae17cc7a81ed))
* test requests to analysis sidecar ([5b28ace](https://github.com/twaslowski/grammr/commit/5b28ace9d848b1ece1a1d77d8422fb18c39b0e11))
* time essential methods with micrometer ([b5fecdb](https://github.com/twaslowski/grammr/commit/b5fecdb0300c348f69c8f937444853948928c552))
* track token usage ([440db6a](https://github.com/twaslowski/grammr/commit/440db6a0288d6ec35d2d2f3fe4dad9ecfd3ad60c))
* use events more extensively; decouple user from main application ([fc772d2](https://github.com/twaslowski/grammr/commit/fc772d2c0dbcab89f08addddfacd3595ce5c9146))
* use larger models; disable deployment to self-hosted ([056126e](https://github.com/twaslowski/grammr/commit/056126e7f06fd0f26886b54fb489b3f46000af1a))
* use serverless functions; cleanups ([4fa7de3](https://github.com/twaslowski/grammr/commit/4fa7de3828a7a80f1cc270a05bcae3c2cdc0a09d))
* use Spring events; create request table ([f285181](https://github.com/twaslowski/grammr/commit/f2851810032935ad3f0dfd20fb6b62a6547b8b17))
* **wip:** core application can parse morphological analysis ([3955745](https://github.com/twaslowski/grammr/commit/3955745fa43f68d66c517cad9af9e5c3146f38c9))


### Bug Fixes

* **#12:** only build new morphology lambda image if not exists ([82ded32](https://github.com/twaslowski/grammr/commit/82ded32c4747a6ccaf96f3b14b6da5160ec5e647))
* add id-token permission to serverless workflow ([4fa3535](https://github.com/twaslowski/grammr/commit/4fa35350680e41e5d7ffb36495973ce2d8c9a4bb))
* allowedOrigin parameter for prod deployment ([eda37a6](https://github.com/twaslowski/grammr/commit/eda37a6588aa4f0eed6dfa1467bd5e9585c04042))
* anki deck retrieval; also increase health check delay ([d9db637](https://github.com/twaslowski/grammr/commit/d9db63756649bdc94f4cb97c78ae10a9d89b3e03))
* **anki:** failing integration tests ([f027aaf](https://github.com/twaslowski/grammr/commit/f027aafe362d78c5925b8231db7ed7373426788b))
* application context creation; remove nginx from local setup ([e17133f](https://github.com/twaslowski/grammr/commit/e17133f1edff199cc683bc2826d9f3356ee6dd71))
* **auth:** add required properties for test profile ([2452e2c](https://github.com/twaslowski/grammr/commit/2452e2cd67611bb831b18c60deb383fbefdcfda4))
* **auth:** ensure spring security configuration denies /api/v1/anki/** ([269017b](https://github.com/twaslowski/grammr/commit/269017b0df4844d5d2a60117775ea201511f3990))
* **auth:** fix environment variable references in properties ([08276a4](https://github.com/twaslowski/grammr/commit/08276a4749dc1cd49463bba4f23ea794b16211e4))
* **auth:** fix k8s secret to create encryption secrets in ([d8ef9a2](https://github.com/twaslowski/grammr/commit/d8ef9a2ee859d8e393571ab06374aa046a2cd293))
* **core:** adjust bad configuration keys ([6deb422](https://github.com/twaslowski/grammr/commit/6deb422e754439e9286dbaae22274a97bf08c02f))
* **core:** also fix bad inflection configuration ([747c613](https://github.com/twaslowski/grammr/commit/747c613635048cbc33f6ba5c86e7171c05ae6bd7))
* **core:** fix ingress target ([47f044c](https://github.com/twaslowski/grammr/commit/47f044c2bb5382e9dd29fb32d0471350e82ce13e))
* **core:** make mvnw executable again; fix(morph): Dockerfile CMD format ([97419fb](https://github.com/twaslowski/grammr/commit/97419fb747c95a1860b43e21ebdffc31c181d850))
* **core:** undo mvnw deletion, fix sidecar tests with pytest-env ([9db8cd7](https://github.com/twaslowski/grammr/commit/9db8cd79b91188e2cf6593002bb5d09745164302))
* dependencies of grammr_core deployment workflow ([ebdb1ee](https://github.com/twaslowski/grammr/commit/ebdb1eefdc91473472cd583fdd196b8590932181))
* disable flakey integration test ([b89b0b9](https://github.com/twaslowski/grammr/commit/b89b0b93f09540eb9e5a72816e14834b675d0f7e))
* docker image format ([9e0b732](https://github.com/twaslowski/grammr/commit/9e0b73286fa42efe5ecd91917863ca69cc2f5307))
* don't use multi-platform images for lambda; arm64 only ([aba8579](https://github.com/twaslowski/grammr/commit/aba85799f3111902edd3ba025562a91f15af8428))
* image name and tag, helm deployment command ([443df6b](https://github.com/twaslowski/grammr/commit/443df6b620a2840719c8332233372cb032d2ae1b))
* improve environment variable handling in tests ([0a7ba0f](https://github.com/twaslowski/grammr/commit/0a7ba0f32f0e7e2e99e99637974f6a29b5ed610b))
* liveness probe check for morphology deployment ([dc554dc](https://github.com/twaslowski/grammr/commit/dc554dcb4d18b4d166ddbfc1f49e1736196c4d31))
* matrix data references ([04b837a](https://github.com/twaslowski/grammr/commit/04b837a0750e8e2a0acb6b7f9ef36bca1236f71a))
* more workflow refactoring ([04640d5](https://github.com/twaslowski/grammr/commit/04640d5d1aaa385d90b4c01650b6a2ce21fd2e02))
* **morph:** increase lambda memory size ([c128c38](https://github.com/twaslowski/grammr/commit/c128c38bcb420d5046cb005d0b6e87d342c4e3bd))
* morphology image version derivation ([d9055b3](https://github.com/twaslowski/grammr/commit/d9055b3485bae9b532f2fdd81a774261b21568be))
* **morphology:** api gateway orchestration across dev/prod environments ([1f31a94](https://github.com/twaslowski/grammr/commit/1f31a94f4f27433712d7e35ca1412c60e288c528))
* multi-inflection image workflow triggers ([1e4de7d](https://github.com/twaslowski/grammr/commit/1e4de7d70059779cc847459cecf0a8f2d114503a))
* problems with metadata uniqueness in helm deployments ([9803687](https://github.com/twaslowski/grammr/commit/9803687b1c626b4ad6d63308434ab77ce193bf80))
* provided value files, value structures ([852eaaa](https://github.com/twaslowski/grammr/commit/852eaaa8ca813e51eecc1a69c172ffdff92c5957))
* python versions and sidecar workflow ([c714c9f](https://github.com/twaslowski/grammr/commit/c714c9f6a900bbac602f5ad27a2727eb68ab678d))
* **quality:** remove obsolete terraform conditional ([0e1ef93](https://github.com/twaslowski/grammr/commit/0e1ef935d9c1b1c2c556968d7218bd8033300eed))
* resolve issue in helm templating ([c903a3f](https://github.com/twaslowski/grammr/commit/c903a3f410c64de112eea9e1c2fcb177d897c109))
* resource creation in morphology chart ([1f423cb](https://github.com/twaslowski/grammr/commit/1f423cb4e635e278b35033e4c506d7d4c2fce974))
* **security:** do not include hashed password in user response ([db78ded](https://github.com/twaslowski/grammr/commit/db78dede55581873073fe78cb3ceb4b4cb04076b))
* service and deployment selector labels ([c551c0d](https://github.com/twaslowski/grammr/commit/c551c0d5fab69bbfe80122dc212e1fd3fef5d174))
* workflow dependency graph and a typo ([c1143a6](https://github.com/twaslowski/grammr/commit/c1143a695696d34f9b417ca21b118c49ea4a8ec9))
* workflow input reference ([8b74719](https://github.com/twaslowski/grammr/commit/8b74719a207b1c5b6480201d944d624126576ffa))
