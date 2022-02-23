# Gradle: Good Practice

These are some bits of advice (some more opinionated than others), which
may be helpful to people starting out with Gradle.

## Keep gradle subprojects small

- Apply the "single purpose" principle

## Name subprojects carefully

- Use hyphenated names
- Use a common prefix for related things

## Prefer a flat directory structure

- Gradle subprojects can be organised in an arbitrary directory structure
- Except for large projects, a single level is better (navigation easier in IDE etc)

## Add a description to every build.gradle

- These descriptions appear in the `./gradlew projects` output
- Also shown in IntelliJ gradle window (tooltip)

## Define "group" and "description" for "public" custom targets

- By public, I mean a target you expect users to invoke
- Only tasks with a defined "group" show up in `./gradlew tasks` output
- The "description" is shown there

## Add a small README.md per subproject, plus top-level README.md

- Hard to generalise about what to include as it's project dependent
- But a top-level README.md with links to each subproject is often useful

## Always define library versions centrally

- Makes it easy to fix OWASP issues
- Generally, best place to do this is `gradle.properties`

## Don't obsess over duplication

- Some duplication of dependencies/config across subprojects is OK
- Trying to centralise too much complicates the build.gradle files
- It also means changes to have a large blast radius

## Consider using custom plugins

- Proceed only after reading the previous item!
- Custom plugins can be used to share build logic between subprojects
- E.g. to avoid repeating a commonly used set of Java dependencies
- Or to enforce project norms (e.g. `spotless` code formatting)
- Can be written in groovy, Java, or even Kotlin
- See https://www.baeldung.com/gradle-create-plugin
- Not for the faint-hearted though!

## Review build scripts even more carefully than code

- Good design is critical for build speed and developer experience

## Enable parallel builds and build cache early in development cycle

- Makes use of multiple cores, hence faster local builds
- Can be awkward to add later
- Might to add some "dependsOn" tweaks
- Don't use build cache in CI/CD pipeline

## Aim for binary-repeatable builds

- Especially beneficial if using incremental deployment in CI or locally
- Some tweaks may be needed, e.g.

    tasks.withType(Zip).configureEach {
        includeEmptyDirs = false
        preserveFileTimestamps = false
        reproducibleFileOrder = true
    }

## For large projects (only!), consider composing builds with includeBuild

See https://docs.gradle.org/current/userguide/composite_builds.html

- As number of subprojects increases, so does Gradle configuration time
- Eventually, local build/test cycle may become annoyingly slow
- At that point, spin off groups of subprojects into separate gradle builds
- Comes with considerable complexity
- Shared config may be obtained from global gradle.properties via `property("foo.bar")`
- Consider using a higher-level mechanism (e.g. `make`) to orchestrate

## Groovy recommendations

- Use `def ...` for constants