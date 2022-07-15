Install `hyperfine` on Debian/Ubuntu
```sh
wget https://github.com/sharkdp/hyperfine/releases/download/v1.12.0/hyperfine_1.12.0_amd64.deb && sudo dpkg -i hyperfine_1.12.0_amd64.deb
```

Run `hyperfine`
```sh
hyperfine --warmup 1 --prepare './gradlew clean && ./gradlew --stop' --parameter-list sdk 17.0.2-zulu,22.0.0.2.r17-grl,17.0.2-open,17.0.2-tem,17.0.2-sem,17.0.2-oracle,17.0.2.8.1-amzn 'supplemental/sdk-benchmark/benchmark-sdk.sh {sdk}'
```