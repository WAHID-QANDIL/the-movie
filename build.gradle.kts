// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dagger.hilt.android.plugin) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.3.0")
        verbose.set(true)
        android.set(true)
        ignoreFailures.set(true)
        additionalEditorconfig.set(
            mapOf(
                "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                "ij_kotlin_allow_trailing_comma" to "false",
                "ij_kotlin_allow_trailing_comma_on_call_site" to "false",
                "ktlint_standard_import-ordering" to "disabled",
                "ktlint_standard_annotation" to "disabled",
                "ktlint_standard_no-consecutive-blank-lines" to "disabled",
                "ktlint_standard_no-blank-line-before-rbrace" to "disabled",
            )
        )
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        }
    }
}