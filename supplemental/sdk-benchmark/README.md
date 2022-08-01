Install `hyperfine` on Debian/Ubuntu

```sh
wget https://github.com/sharkdp/hyperfine/releases/download/v1.14.0/hyperfine_1.14.0_amd64.deb && sudo dpkg -i hyperfine_1.14.0_amd64.deb
```

Run `hyperfine`

```sh
hyperfine --warmup 1 --prepare './gradlew clean --no-daemon && ./gradlew --stop' --parameter-list sdk 17.0.4-librca,17.0.4-zulu,22.2.r17-grl,17.0.3-ms,17.0.2-open,17.0.4-tem,17.0.3-sem,17.0.4-oracle,17.0.3.6.1-amzn,17.0.4-sapmchn 'supplemental/sdk-benchmark/benchmark-sdk.sh {sdk}'
```

View available java packages:
```sh
sdk list java
```