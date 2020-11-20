import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.3.72"
    id("org.jetbrains.intellij") version "0.4.21"
}
val ideaVersion = extra.properties["ideaVersion"] as? String ?: "IC-203.5419.21-EAP-SNAPSHOT"
val jetbrainsPublishToken: String by project

apply {
    plugin("org.jetbrains.intellij")
}

// See: https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    pluginName = "cucumber-kotlin"
    version = ideaVersion

    downloadSources = true
    instrumentCode = true

    when (ideaVersion) {
        // Gherkin plugin version: https://plugins.jetbrains.com/plugin/9164-gherkin/versions
        "2020.2" ->
            setPlugins(
                "java",
                "gherkin:202.6397.21",
                "Kotlin"
            )
        "IC-203.5419.21-EAP-SNAPSHOT" ->
            setPlugins(
                "java",
                "gherkin:203.5419.7",
                "Kotlin"
            )
        "201.8743.12" -> {
            setPlugins(
                "java",
                "gherkin:201.8538.45",
                "Kotlin"
            )
        }
    }
}

repositories {
    mavenCentral()
    maven("https://www.jetbrains.com/intellij-repository/snapshots")
}

dependencies {
    implementation(kotlin("stdlib"))
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions.jvmTarget = "1.8"

tasks {
    named<PublishTask>("publishPlugin") {
        token(jetbrainsPublishToken)
    }
    named<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
        pluginDescription("""
              <p>
                This plugin enables <a href="http://cukes.info/">Cucumber</a> support with step definitions written in Kotlin.
              </p>
              <p>
                The following coding assistance features are available:
              </p>
              <ul>
                <li>Navigation in the source code.
              </ul>
        """)
        changeNotes("""
      <ul>
        <li><b>1.1.6</b> <em>(2020-11-03)</em> - Upgrade to 2020.3-EAP</li>
        <li><b>1.1.5</b> <em>(2020-04-16)</em> - Upgrade to 2020.2</li>
        <li><b>1.1.4</b> <em>(2020-04-16)</em> - Fix warnings from JetBrains plugin compatibility check</li>
        <li><b>1.1.3</b> <em>(2020-04-14)</em> - Compatible with Intellij IDEA 2020.1</li>
        <li><b>1.1.2</b> <em>(2020-01-17)</em> - ArrayOutOfBoundsException in Intellij IDEA 2019.3.2</li>
        <li><b>1.1.1</b> <em>(2019-05-30)</em> - Fix NoSuchMethodError on 2019.1</li>
        <li><b>1.1.0</b> <em>(2019-05-24)</em> - Create step definitions</li>
        <li><b>1.0.2</b> <em>(2018-03-14)</em> - Running features now populates the glue automatically</li>
        <li><b>1.0.1</b> <em>(2018-03-14)</em> - Support regex shorthand character classes</li>
        <li><b>1.0.0</b> <em>(2018-03-13)</em> - Initial release</li>
      </ul>
    """)
    }
}
