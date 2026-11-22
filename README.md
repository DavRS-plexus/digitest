# Digitest


# MIN-SDK = 28 (Android 9)
It covers the 97% of devices
https://composables.com/android-distribution-chart


* LINT
* CONVENTION PLUGINS (build-logic folder)
* CONFIG
  * catalog versions
* ARCHITECTURE
  * modules
    * core
    * features
    * libs

* DI = Koin (with compiler plugin)
* Navigation using Roots and Routes
* UNIT TEST
* BuildVariant + ofuscación

## Static analysis

Run `./gradlew detekt` (or `.\gradlew.bat detekt` on Windows) to analyze
main, unit-test, and Android instrumented-test Kotlin sources. Reports are
available in each module's `build/reports/detekt` directory. Detekt also runs
as part of `check` and in pull-request CI.

The module-level `detekt-baseline.xml` files record existing findings; new
findings fail the check. Remove baseline entries as the underlying issues are
fixed rather than regenerating baselines to suppress new findings.