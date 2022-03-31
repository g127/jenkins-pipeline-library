package com.jia54321.jenkins.steps
import com.jia54321.jenkins.utils.utils


def usesWith(targetItem) {
    def utils = new utils()
    utils.printMessage( "nexusPublisher with=${targetItem.with}",  "green" )

    executeNexusPublisher(
    	targetItem.with.nexusInstanceId?:'Nexus',
        targetItem.with.nexusRepositoryId?:'release',
        targetItem.with.nexusPackagesClass?:'MavenPackage',
        targetItem.with.filePath,
        targetItem.with.groupId,  
        targetItem.with.artifactId,
        targetItem.with.version,
        targetItem.with.packaging?:'zip',
        targetItem.with.tagName?:'${artifactId}-${version}'
    )
    
}


//======================================================================================================================


def executeNexusPublisher(nexusInstanceId, nexusRepositoryId, nexusPackagesClass, filePath, groupId, artifactId, version, packaging, tagName) {
    nexusPublisher(
        nexusInstanceId: nexusInstanceId, 
        nexusRepositoryId: nexusRepositoryId,
        packages: [[
            $class: nexusPackagesClass, mavenAssetList: [[classifier: '', extension: '', filePath: filePath ]], 
            mavenCoordinate: [
               groupId: groupId, artifactId: artifactId, version: version, packaging: packaging
            ]
        ]], 
        tagName: tagName
    )

}