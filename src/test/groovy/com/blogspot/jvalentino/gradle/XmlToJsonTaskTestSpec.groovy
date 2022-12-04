package com.blogspot.jvalentino.gradle

import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification
import spock.lang.Subject

class XmlToJsonTaskTestSpec extends Specification {

    @Subject
    XmlToJsonTask task
    Project project
    
    def setup() {
        Project p = ProjectBuilder.builder().build()
        task = p.task('ping', type:XmlToJsonTask)
        task.instance = Mock(XmlToJsonTask)
        project = Mock(ProjectInternal)
        
        FileUtils.deleteDirectory(
            new File("build/testcase/output"))
    }
    
    def cleanup() {
        FileUtils.deleteDirectory(
            new File("build/testcase/output"))
    }
    
    void "test perform"() {
        given:
        task.input = 'src/test/resources/input'
        File buildDir = Mock(File)
        ConfigurableFileTree fileTree = Mock(ConfigurableFileTree)
        fileTree.files >> [
            new File('src/test/resources/input/alpha.xml'),
            new File('src/test/resources/input/bravo.xml')
        ]
        
        when:
        task.perform()
        
        then:
        1 * task.instance.project >> project
        1 * project.buildDir >> buildDir
        1 * buildDir.absolutePath >> 'build/testcase'
        
        and:
        1 * project.fileTree(task.input, _) >> fileTree 
        new File("build/testcase/output/alpha.xml.json").text == 
            '{"b":"1"}'
        new File("build/testcase/output/bravo.xml.json").text == 
            '{"b":"2"}'
        
    }
}
