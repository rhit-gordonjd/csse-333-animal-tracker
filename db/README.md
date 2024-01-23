# Installation
1. Download flyway from https://documentation.red-gate.com/fd/command-line-184127404.html.
2. Unzip flyway and add it to your PATH

# Setting up a new database
1. Create the database in SQL Server Management Studio. Add the migrator account to the database
    ```sql
    CREATE DATABASE [AnimalTracker-<name>]
    GO
    USE [AnimalTracker-<name>]
    GO
    CREATE USER AnimalTrackerMigrator FOR LOGIN AnimalTrackerMigrator
    ALTER ROLE db_accessadmin ADD MEMBER AnimalTrackerMigrator
    ```
2. Add a configuration to [flyway.toml](flyway.toml)
    ```toml
    [environments.<name>]
    url = "jdbc:sqlserver://golem.csse.rose-hulman.edu;databaseName=AnimalTracker-<name>;encrypt=false"
    user = "<name>"
    ```
3. Run the migrations (must be in the db folder wih flyway.toml)
    ```sh
    flyway -environment=<name> migrate -password=<password>
    ```
