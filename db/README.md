# Installation
1. Download flyway from https://documentation.red-gate.com/fd/command-line-184127404.html.
2. Unzip flyway and add it to your PATH

# Running migrations
```sh
cd db
flyway -environment=<env_name> migrate
```
