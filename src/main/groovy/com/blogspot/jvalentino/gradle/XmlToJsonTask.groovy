package com.blogspot.jvalentino.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

import groovy.json.JsonBuilder
import groovy.util.slurpersupport.GPathResult

/**
 * <p>Converts XML to JSON</p>
 * @author jvalentino2
 */
@SuppressWarnings(['Println'])
class XmlToJsonTask extends DefaultTask {

    protected XmlToJsonTask instance = this
    protected String input = 'input'

    @TaskAction
    void perform() {
        Project p = instance.project

        String output = "${p.buildDir.absolutePath}/output"
        File outputDir = new File(output)
        outputDir.mkdirs()

        FileTree tree = p.fileTree(input) { include '**/*.xml' }

        tree.files.each { File file ->
            GPathResult xml = new XmlSlurper().parseText(file.text)
            Map map = convertToMap(xml)

            JsonBuilder builder = new JsonBuilder(map)

            String json = "${outputDir.absolutePath}/${file.name}.json"
            File jsonFile = new File(json)
            jsonFile.text = builder.toString()

            println "- ${file.name} was converted to ${jsonFile.name}"
        }
    }

    Map convertToMap(GPathResult nodes) {
        nodes.children().collectEntries {
            [
                it.name(),
                it.childNodes() ? convertToMap(it) : it.text(),
            ]
        }
    }
}
