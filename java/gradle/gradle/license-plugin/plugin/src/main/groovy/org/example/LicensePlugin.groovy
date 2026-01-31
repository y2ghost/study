package org.example

import org.gradle.api.Project
import org.gradle.api.Plugin

class LicensePlugin implements Plugin<Project> {
    void apply(Project project) {
        project.tasks.register("greeting") {
            doLast {
                println("Hello from plugin 'org.example.greeting'")
            }
        }
    }
}

