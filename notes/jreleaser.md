# JReleaser Notes for irimo

This document outlines the key configurations for `irimo` using JReleaser, focusing on packaging and
distribution.

## JBang Packager

[JBang](https://www.jbang.dev/) is configured as a packager for `irimo`, providing easy access to the
software and its code. For each release, JReleaser creates a JBang script accessible via
the [garodriguezlp/jbang-catalog](https://github.com/garodriguezlp/jbang-catalog/).

With JBang installed, you can run:

```bash
jbang irimo@garodriguezlp
```

This executes the `irimo` alias defined in the catalog's `jbang-catalog.json` file.

## Snapshot Versions and JitPack

JReleaser also publishes an `irimo-snapshot` alias for accessing intermediate versions during
development. This script depends on a `SNAPSHOT` dependency pointing to the JitPack repository.

JitPack pulls the latest code from the main branch, builds it, and makes it available when
a `-SNAPSHOT` version is requested, allowing easy access to the most recent development version.

Check the project's build status
at: [https://jitpack.io/#garodriguezlp/irimo/](https://jitpack.io/#garodriguezlp/irimo/)

For more details on JBang, JReleaser, and JitPack, refer to their respective documentation. These
tools offer powerful capabilities for Java developers working on open-source projects.

## How to Create a Release

This section documents the steps and dependencies required to create a release using JReleaser.
Releasing `irimo` is designed to be straightforward.

### Steps for Release (-SNAPSHOT and Full Release)

1. **Update Version in `build.gradle.kts` (Full Release Only)**:

   For a full release, ensure the version is not a `-SNAPSHOT` version. Remove the snapshot suffix
   and commit the change:

   ```bash
   git commit -m "chore(release): release X.Y.Z"
   ```

2. **Create Distribution Artifact**:

   Generate the distribution artifact in the expected location (refer to
   `distributions.irimo.artifacts.path` in `jreleaser.yml` for details). Run:

   ```bash
   ./gradlew bootJar
   ```

3. **Prepare for Deployment (Mainly for Full Release)**:

   For non-`-SNAPSHOT` versions, set up deployment to the Maven repository. Ensure all required
   Maven assets are in the staging repository (see `deploy.maven.github.irimo.stagingRepositories`
   in `jreleaser.yml`). Execute:

   ```bash
   ./gradlew publish
   ```

   Note: This command includes `bootJar`, so for full releases, you can start from this step.

4. **Perform Full Release**:

   Once everything is prepared, execute the full release. Specify the version to release, which
   should match the version in `build.gradle.kts`. For example, to release version `0.1.0`:

   ```bash
   jreleaser full-release -Djreleaser.project.version=0.1.0
   ```

5. **Update to Next Snapshot Version (Full Release Only)**:

   After a full release, update `build.gradle.kts` to the next `-SNAPSHOT` version. It's
   recommended to increment the patch version. Commit the change:

   ```bash
   git commit -m "chore(release): bump to next snapshot version"
   ```

This process ensures a smooth release cycle for both snapshot and full releases of `irimo`.
Always verify all configurations and prerequisites before initiating the release process.
