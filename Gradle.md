# Gradle Commands

Application tasks
-----------------
bootRun - Run the project with support for auto-detecting main class and reloading static resources

Build tasks
-----------
assemble - Assembles the outputs of this project.
bootRepackage - Repackage existing JAR and WAR archives so that they can be executed from the command line using 'java -jar'
build - Assembles and tests this project.
buildDependents - Assembles and tests this project and all projects that depend on it.
buildNeeded - Assembles and tests this project and all projects it depends on.
classes - Assembles main classes.
clean - Deletes the build directory.
jar - Assembles a jar archive containing the main classes.
testClasses - Assembles test classes.
war - Generates a war archive with all the compiled classes, the web-app content and the libraries.

Build Setup tasks
-----------------
init - Initializes a new Gradle build.
wrapper - Generates Gradle wrapper files.

Documentation tasks
-------------------
groovydoc - Generates Groovydoc API documentation for the main source code.
javadoc - Generates Javadoc API documentation for the main source code.

Help tasks
----------
buildEnvironment - Displays all buildscript dependencies declared in root project 'tbex'.
components - Displays the components produced by root project 'tbex'. [incubating]
dependencies - Displays all dependencies declared in root project 'tbex'.
dependencyInsight - Displays the insight into a specific dependency in root project 'tbex'.
dependentComponents - Displays the dependent components of components in root project 'tbex'. [incubating]
help - Displays a help message.
model - Displays the configuration model of root project 'tbex'. [incubating]
projects - Displays the sub-projects of root project 'tbex'.
properties - Displays the properties of root project 'tbex'.
tasks - Displays the tasks runnable from root project 'tbex'.

IDE tasks
---------
cleanEclipse - Cleans all Eclipse files.
cleanEclipseWtp - Cleans Eclipse wtp configuration files.
cleanIdea - Cleans IDEA project files (IML, IPR)
eclipse - Generates all Eclipse files.
eclipseWtp - Generates Eclipse wtp configuration files.
idea - Generates IDEA project files (IML, IPR, IWS)

Verification tasks
------------------
check - Runs all checks.
test - Runs the unit tests.

Rules
-----
Pattern: clean<TaskName>: Cleans the output files of a task.
Pattern: build<ConfigurationName>: Assembles the artifacts of a configuration.
Pattern: upload<ConfigurationName>: Assembles and uploads the artifacts belonging to a configuration.

To see all tasks and more detail, run gradlew tasks --all

To see more detail about a task, run gradlew help --task <task>

BUILD SUCCESSFUL

Total time: 0.628 secs
bpaxton@TPLNK-BPAXTON3 tbex % ./gradlew --help

USAGE: gradlew [option...] [task...]

-?, -h, --help          Shows this help message.
-a, --no-rebuild        Do not rebuild project dependencies.
-b, --build-file        Specify the build file.
--build-cache           Enables the Gradle build cache. Gradle will try to reuse outputs from previous builds. [incubating]
-c, --settings-file     Specify the settings file.
--configure-on-demand   Configure necessary projects only. Gradle will attempt to reduce configuration time for large multi-project builds. [incubating]
--console               Specifies which type of console output to generate. Values are 'plain', 'auto' (default) or 'rich'.
--continue              Continue task execution after a task failure.
-D, --system-prop       Set system property of the JVM (e.g. -Dmyprop=myvalue).
-d, --debug             Log in debug mode (includes normal stacktrace).
--daemon                Uses the Gradle Daemon to run the build. Starts the Daemon if not running.
--foreground            Starts the Gradle Daemon in the foreground. [incubating]
-g, --gradle-user-home  Specifies the gradle user home directory.
--gui                   Launches the Gradle GUI.
-I, --init-script       Specify an initialization script.
-i, --info              Set log level to info.
--include-build         Include the specified build in the composite. [incubating]
-m, --dry-run           Run the builds with all task actions disabled.
--max-workers           Configure the number of concurrent workers Gradle is allowed to use. [incubating]
--no-build-cache        Disables the Gradle build cache. [incubating]
--no-daemon             Do not use the Gradle Daemon to run the build.
--no-scan               Disables the creation of a build scan. (https://gradle.com/scans) [incubating]
--offline               Execute the build without accessing network resources.
-P, --project-prop      Set project property for the build script (e.g. -Pmyprop=myvalue).
-p, --project-dir       Specifies the start directory for Gradle. Defaults to current directory.
--parallel              Build projects in parallel. Gradle will attempt to determine the optimal number of executor threads to use. [incubating]
--profile               Profile build execution time and generates a report in the <build_dir>/reports/profile directory.
--project-cache-dir     Specify the project-specific cache directory. Defaults to .gradle in the root project directory.
-q, --quiet             Log errors only.
--recompile-scripts     Force build script recompiling.
--refresh-dependencies  Refresh the state of dependencies.
--rerun-tasks           Ignore previously cached task results.
-S, --full-stacktrace   Print out the full (very verbose) stacktrace for all exceptions.
-s, --stacktrace        Print out the stacktrace for all exceptions.
--scan                  Creates a build scan. Gradle will emit a warning if the build scan plugin has not been applied. (https://gradle.com/scans) [incubating]
--status                Shows status of running and recently stopped Gradle Daemon(s).
--stop                  Stops the Gradle Daemon if it is running.
-t, --continuous        Enables continuous build. Gradle does not exit and will re-execute tasks when task file inputs change. [incubating]
-u, --no-search-upward  Don't search in parent folders for a settings.gradle file.
-v, --version           Print version info.
-w, --warn              Set log level to warn.
-x, --exclude-task      Specify a task to be excluded from execution.

