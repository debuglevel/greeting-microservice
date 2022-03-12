# Development notes

## Use `person` as a template for new entities
Hint: In `bash`, STRG+X STRG+E opens your `$EDITOR` to paste one or multiple commands.

```bash
export new_camelcase=Animal
export new_lower="${new_camelcase,,}"
export old_camelcase=Person
export old_lower="${old_camelcase,,}"
cp -a $old_lower $new_lower
cd $new_lower
sed -i "s/$old_camelcase/$new_camelcase/g" *
sed -i "s/$old_lower/$new_lower/g" *
rename "s/$old_camelcase/$new_camelcase/g" *
cd ..
```

## Update dependencies

Run `./gradlew dependencyUpdates` to list all dependencies which have newer versions available.

## Report licenses

`./gradlew generateLicenseReport` creates a report about all licenses of all dependencies
in `build/reports/dependency-license/index.html`.

## Release a version

With `./gradlew release` a new version is released. Before running the command, you must `git push`. It's also a good
idea to commit everything or to rollback pending changes (although the configuration in `build.gradle` should ensure
nothing is committed that was not intended).

If the default branch is changed from `master` to `main`, it has to be changed in `build.gradle`.

```groovy
git {
  // depends on old vs. new default branch
  requireBranch = 'master'
  // requireBranch = 'main'
}
```

## Liquibase

Liquibase is a tool to migrate a service's database between releases. Often Hibernate creates or alters (if possible)
the database structure on startup (usually via `jpa.default.properties.hibernate.hbm2ddl.auto=update`).
See https://docs.jboss.org/hibernate/orm/5.4/javadocs/org/hibernate/tool/schema/Action.html
and https://www.baeldung.com/spring-boot-data-sql-and-schema-sql#controlling-database-creation-using-hibernate on the
Hibernate feature.

With Liquibase, this Hibernate feature is deactivated (`jpa.default.properties.hibernate.hbm2ddl.auto=none`) and
accomplishes with a set of changelogs instead. A changelog (
e.g. `src/main/resources/liquibase-changelogs/changelogs/1.0.2.xml`) might span all changes needed to migrate from a
version 1.0.1 to 1.0.2 (which we assume as an example here).

### Example changelog

The following changelog `1.0.2.xml` would add a new column `color` to the `animals` table.

```xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.1.xsd"
                   logicalFilePath="path-independent">
    <changeSet author="debuglevel" id="1636143355907-1">
        <addColumn tableName="animals">
            <column name="color" type="VARCHAR(255 BYTE)"/>
        </addColumn>
    </changeSet>
</databaseChangeLog>
```

### Liquibase command

Liquibase documentation usually says that configuration should be put in `liquibase.properties`. But while working on
different microservices, it might be easier to just pass credentials and connection string as parameters:

```liquibase generate-changelog --changelog-file=2.0.0.xml --url=jdbc:mariadb://localhost:3306/greeting_old --username=root --password=root```

### Generating the initial changelog

Creating the initial tables (in contrast to alter them in all subsequent changelogs) should also be done via a first
changelog. It just creates all tables on the first startup.

```xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.1.xsd"
                   logicalFilePath="path-independent">
    <changeSet author="debuglevel" id="1636143270364-1">
        <createTable tableName="animal">
            <column name="id" type="BINARY(16)">
                <constraints nullable="false" primaryKey="true"/>
            </column>
            <column defaultValueComputed="NULL" name="created_on" type="datetime"/>
            <column defaultValueComputed="NULL" name="last_modified_on" type="datetime"/>
            <column defaultValueComputed="NULL" name="name" type="VARCHAR(255 BYTE)"/>
        </createTable>
    </changeSet>
    <changeSet author="debuglevel" id="1636143270364-2">
        <!-- create another table... -->
    </changeSet>
</databaseChangeLog>
```

As writing those initial changelogs would be a tedious job, it is usually generated
via `./liquibase generate-changelog --changelog-file=1.0.0.xml`. It might still be a good idea to check the results.

For introducing Liquibase into an existing service, it might be the best approach to generate a changelog for the oldest
known deployed version (or the oldest one you want to support - if you're developing version 1.0.2 you could generate an
initial changelog for 1.0.0 and diff changelogs for 1.0.1 and 1.0.2 (if there were database changes at all) but not
bother about migrating your alpha 0.x.y versions).

### Introducing Liquibase to an existing project

If there are already some deployments with production databases, Liquibase would try to apply this changelog and fail as
the tables already exist. It should therefore check whether those tables already exist and mark this changelog as "
ran" (although it was never actually ran by Liquibase, but by Hibernate's table creation). This is done by checking
the `<preConditions>` that `<not>` a `<tableExists>` with `tableName="form"` and that `<not>`
a `columnName="id"` `<columnExists>` in `tableName="form"` et cetera. This `preConditions` will actually fail and should
then be marked as `onFail="MARK_RAN"`.

```xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.1.xsd"
                   logicalFilePath="path-independent">
    <changeSet author="debuglevel" id="1636143270364-1">
        <!-- Check whether table "animal" does exist in its 1.0.0 structure.
             Marks this ChangeSet as run, if it already exists.
             This avoids manually running "liquibase changelog-sync" before migrating to 1.0.2 -->
        <preConditions onFail="MARK_RAN">
            <not>
                <tableExists tableName="animal"/>
                <columnExists tableName="animal" columnName="id"/>
                <columnExists tableName="animal" columnName="created_on"/>
                <columnExists tableName="animal" columnName="last_modified_on"/>
                <columnExists tableName="animal" columnName="name"/>
            </not>
        </preConditions>
        <createTable tableName="animal">
            <column name="id" type="BINARY(16)">
                <constraints nullable="false" primaryKey="true"/>
            </column>
            <column defaultValueComputed="NULL" name="created_on" type="datetime"/>
            <column defaultValueComputed="NULL" name="last_modified_on" type="datetime"/>
            <column defaultValueComputed="NULL" name="name" type="VARCHAR(255 BYTE)"/>
        </createTable>
    </changeSet>
    <changeSet author="debuglevel" id="1636143270364-2">
        <!-- create another table... -->
    </changeSet>
</databaseChangeLog>
```

This job could also be done by running `liquibase changelog-sync` against a production database which marks all
changelogs up to a certain one as already applied. But as this would rather be a real pain with containerized services,
writing some `<preConditions>` for all deployed versions is probably a way better idea. (The best idea is of course to
use Liquibase from the beginning - because there *will* be database changes.)

For new deployments, the `<preConditions>` check will simply succeed and therefore create the database structure.

### Creating diffs

To create a migration for the 1.0.2 version, run `./liquibase diff-changelog --changelog-file=1.0.2.xml`. The actual
command (see Liquibase documentation and `liquibase.properties` in the Liquibase distribution for now) has to be run
against two databases to compare them:

* `liquibase.command.referenceUrl` or "reference", which is the new/after database, which already contains the changes.
* `liquibase.command.url` or "target", which is the old/before database which does not contain the changes.

This would create something like this changelog:

```xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.1.xsd">
    <changeSet author="debuglevel" id="1636143355907-1">
        <addColumn tableName="animals">
            <column name="color" type="VARCHAR(255 BYTE)"/>
        </addColumn>
    </changeSet>
</databaseChangeLog>
```

This is useful to make use of Hibernate's `jpa.default.properties.hibernate.hbm2ddl.auto=update` which created the
wanted database structure during development, and then just create a diff.

But creating a changelog on your own is probably also a good way, considering they are usually not that complex.

### Removing file path as Liquibase identifier

Liquibase identifies ran changeSets by the file path, the author and the id. If one of those changes, the changeSet
would be applied again. While author and id might be quite obvious identifiers, the file path might be changed too
easily - and all changeSets would be applied again. To avoid this, `logicalFilePath` can be set to a constant,
e.g. `logicalFilePath="path-independent"`.

```xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.1.xsd"
                   logicalFilePath="path-independent">
    <!-- ... -->
</databaseChangeLog>
```

### Dockerized MariaDB for database diffs

To create a diff with `liquibase diff-changelog`, you need two databases with the old and new structure. To create the
database, validate things or just to look at it, phpmyadmin is usually a nice choice.

This `docker-compose.yml` spins up a MariaDB at the usual port 3306 and a phpMyAdmin on port 8888.

```yaml
version: "3.1"

services:
  mariadb:
    image: mariadb:10.5.8-focal
    ports:
      - 3306:3306
    volumes:
      - data:/var/lib/mysql
    environment:
      MYSQL_ROOT_PASSWORD: root

  phpmyadmin:
    image: phpmyadmin
    ports:
      - 8888:80
    environment:
      - PMA_HOST=mariadb

volumes:
  data:
```

With this setup, just

1. create a `old` and a `new` database
2. change `jpa.default.properties.hibernate.hbm2ddl.auto=update`
3. point the `datasources.default` in `application.yml` to jdbc:mariadb://localhost:3306/old for previous/old version
   and to `jdbc:mariadb://localhost:3306/new` for your new version:

```yaml
datasources:
  default:
    url: jdbc:mariadb://localhost:3306/old
    driver-class-name: org.mariadb.jdbc.Driver
    username: root
    password: root
    dialect: MariaDB103
```

4. run `liquibase diff-changelog`

For `liquibase generate-changelog`, only the `old` database is used.

### Deactivate Liquibase during development

If you want to use Hibernate database structure generation during development, you can set in `application.yml`:

```yaml
jpa.default.properties.hibernate.hbm2ddl.auto=update
liquibase.enabled: false
```

You could also set this only in `application-test.yml` if you want to run your tests without Liquibase but with
Hibernate instead.
