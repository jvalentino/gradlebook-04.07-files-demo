package com.blogspot.jvalentino.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification
import spock.lang.Subject

class FilesDemoPluginTestSpec extends Specification {

    Project project
    @Subject
    FilesDemoPlugin plugin

    def setup() {
        project = ProjectBuilder.builder().build()
        plugin = new FilesDemoPlugin()
    }

    void "test plugin"() {
        when:
        plugin.apply(project)

        then:
        project.tasks.getAt(0).toString() == "task ':xmlToJson'"
    }
}
