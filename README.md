# Gradle Introduction

This code is intended to accompany a Gradle talk, covering:

- the basics of using Gradle for building Java projects.
- everyday use of Gradle from the command line and IDE.
- how to use third-party gradle plugins.

We start out with a trivial "hello world" application, and with each
commit add some feature which illustrates one or more aspects of Gradle.

## Hello Gradle

A trivial `build.gradle` with a "hello" task.

Open project in IntelliJ

=> Intellij adds the "boilerplate" automatically:
    - `gradlew`
    - `gradlew.bat`
    - `gradle/wrapper/gradle-wrapper.jar`
    - `gradle/wrapper/gradle-wrapper.properties`

We also added a `.gitignore`

### Talking points:

- Gradle wrapper
- Running a gradle task from command line
- Running a gradle task within IntelliJ
- Paths

## Simple java application

Using java "application" plugin to build a "one directory" Java app

### Talking points

- refreshing gradle project
- application plugin
  - https://docs.gradle.org/current/userguide/application_plugin.html
  - `build`, `clean`, `jar` targets
  - `run` target
- build.gradle
  - configuration of main class
  - version
- source code layout (same as Maven)
- the `settings.gradle` file, rootProject.name
- build artifacts
  - `build/libs/greetings-0.0.1.jar`
  - `build/distributions/greetings-0.0.1.zip` and `.tar`
- IntelliJ annoyances
  - `//file:noinspection GroovyUnusedAssignment`

## Gradle subprojects

For all but the very simplest projects, it makes sense to split
the gradle project into multiple **subprojects**.

In this example, we can restructure like this:

    `greeting-app` (application) -> `greeting-service` (library)

### Talking points

- settings file (include)
- the "java" plugin
- top-level gradle file - `allprojects` and `subprojects` configuration
- project dependencies
- subprojects in IntelliJ
- helpy gradle targets - `projects`, `tasks`
- target cascading (`:greeting-app:run` vs `run`)

### Useful links:

- https://docs.gradle.org/current/userguide/multi_project_builds.html
- https://docs.gradle.org/current/samples/sample_jvm_multi_project_with_toolchains.html

## Dependencies and Annotation Processing

Most projects use annotation processors and logging framework.

How to integrate these into the build? We will illustrate this by:

- Adding a "model" subproject with a lombok-annotated `Language` class.
- Adding a "logback" dependency to the service layer, for outputting logs.

### Talking points

- maven repository
- types of dependency (implementation, compileOnly, annotationProcessor, ...)
- gradle.properties
- dependency duplication

### Useful links

- https://www.baeldung.com/slf4j-with-log4j2-logback
- https://docs.gradle.org/current/userguide/java_plugin.html#tab:configurations

## Dependency Injection with Weld

Not strictly a Gradle thing, but adding a DI framework:

- will make it easier to do component level testing later.
- illustrates `compileOnly` and `implementation` dependencies (again).

Many DI frameworks (Dagger, Guice, ...) exist, but here we use [Weld](https://weld.cdi-spec.org/),
which is easy to use and CDI standards compliant.

We will:

- Instantiate a Weld container in our main() function.
- Use it to obtain an instance of the `Hello` class.
- Inject the `GreetingService` into the `Hello` class using CDI.
- Use Lombok to write inject-annotated constructors for us, reducing boilerplate.

### Talking points

- `@ApplicationScoped` annotation
- `@Inject` annotation on classes with nontrivial constructor
- `@RequiredArgsConstructoronConstructor = @__(@Inject)` magic
- `beans.xml` files
- Bean discovery
- Logging config

### Useful links

- https://www.baeldung.com/java-ee-cdi
- https://weld.cdi-spec.org/

## Introduce Repository Layer / Unit Testing

The Gradle `java` plugin automatically adds targets such as `test`.

We'll illustrate their use, via these steps:

- Add a repository layer for looking up the greeting message for a given language
- Have the service layer inject this repository layer, and make use of it.
- Add a "mockito" style unit test for the service layer

The repository implementation just returns hard-coded messages at the moment.
Later, we'll connect it to a database (which will illustrate some more actual
gradle stuff!)

### Talking points

- Mockito and junit dependencies
- useJUnitPlatform() configuration
- Running all tests, from command line: `./gradlew test`
- Running/debugging tests under IntelliJ
- Duplication of dependencies across subprojects (again!)

### Useful links

- https://www.baeldung.com/mockito-series
- https://stackoverflow.com/questions/22505533/how-to-run-only-one-unit-test-class-using-gradle

## Use Docker Compose Plugin to start a local DB

It's very common to want to spin up docker containers as part of the build process.

There's a Docker plugin for that: `com.avast.gradle.docker-compose`

We'll illustrate using this to create a local Postgres database.

Inside the container this runs on port 5432. We'll expose it on the host port 5437.

### Discussion points

- docker compose configuration
- gradle plugin configuration
- environment variables
- gradle tasks `composeUp` and `composeDown`

### Useful Links

- https://github.com/avast/gradle-docker-compose-plugin
- https://docs.docker.com/compose/compose-file

## Use Flyway Plugin to create DB tables

Ok, great we've got an empty database. How to create tables in it?

Flyway is one option (see link below).

There's a Gradle plugin `org.flywaydb.flyway` that allows you to run flyway migrations
via Gradle tasks - very convenient for local dev and CI.

Aside: for prod, you'd probably use another mechanism to invoke flyway,
such as an AWS lambda built using the flyway Java library.


### Discussion points

- flyway basics (what does it do?)
- migration scripts
- targets `flywayClean`, `flywayMigrate`
- build.gradle ordering (`buildScript`, then `plugins`, then other stuff)

### Useful links

- https://flywaydb.org/documentation/
- https://flywaydb.org/documentation/usage/gradle

## Use JOOQ Plugin to generate a fluent DB API from schema

We now have a database, and the ability to manage its schema using flyway.

We'd like to make queries on that DB from our repository layer.

There are several possible frameworks/approaches we could use.

One solution is [JOOQ](https://www.jooq.org/). This has two parts:

- compile-time (connect to the DB, generate Java classes based on its schema)
- runtime (implementation of the abstractions these classes depend on)

We will:

- use the JOOQ Gradle Plugin to perform the code generation
- add the necessary runtime dependency on JOOQ

The result will be a `greetings-jooq` library (but we won't be using it yet)

### Talking Points

- gradle.build plugin, configuration and dependencies
- groovy `def`
- jooq target `generateDbJooq`
- additional source set
- generated source code
- custom target `cleanDbJooq`
- task ordering
- checking in vs. compilation

## Rework repository layer to use JOOQ classes

We will now update our `greeting-repository` subproject,
adding a dependency on the JOOQ generated library, and
updating the code to make use of it (to retrieve the greeting
text from the database).

There isn't really any Gradle specific stuff here that we haven't
already covered. Essentially, we need to add:

- new `greeting-config` subproject to provide `DataSource` and `DslContext` beans
- extra runtime dependencies on  `greeting-app` (jooq, DBCP, postgres driver)
- extra implementation dependencies on `greeting-repository` (`greeting-jooq`, JOOQ runtime)

and reimplement `GreetingRepository` as a JOOQ query:

    public Optional<String> readGreetingText(Language language) {
        return dsl.selectFrom(GREETING_TEXT)
                .where(GREETING_TEXT.LANGUAGE.eq(language.name()))
                .fetchOptional(GREETING_TEXT.CONTENT);
    }

### Talking points

- Demo: update DB manually, inspect `./gradlew run` output
- The `@Produces` annotation

### Useful Links

- https://docs.jboss.org/weld/reference/2.4.8.Final/en-US/html/beanscdi.html#_producer_methods

## Tidying up

Our gradle project structure now looks like this:

    $ ./gradlew projects
    
    > Task :projects
    
    ------------------------------------------------------------
    Root project 'greetings'
    ------------------------------------------------------------
    
    Root project 'greetings'
    +--- Project ':greeting-app'
    +--- Project ':greeting-config'
    +--- Project ':greeting-db'
    +--- Project ':greeting-jooq'
    +--- Project ':greeting-model'
    +--- Project ':greeting-repository'
    \--- Project ':greeting-service'

It's not very clear what each project does. We can fix that by adding
a `description = "..."` to each build.gradle file.

We then get:

    Root project 'greetings' - Top level build + docker-compose
    +--- Project ':greeting-app' - Greeting application
    +--- Project ':greeting-config' - Config for SQL DataSource and other injected objects.
    +--- Project ':greeting-db' - DB schema definition and migrations
    +--- Project ':greeting-jooq' - JOOQ code generation
    +--- Project ':greeting-model' - Domain model for the greeting application
    +--- Project ':greeting-repository' - Repository layer
    \--- Project ':greeting-service' - Service layer

Nice!
