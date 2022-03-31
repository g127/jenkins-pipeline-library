package com.jia54321.jenkins.steps
import com.jia54321.jenkins.utils.utils

def usesWith(targetItem) {
    def utils = new utils()
    utils.printMessage( "archiveArtifacts with=${targetItem.with}",  "green" )

    executeArchiveArtifacts(
    	targetItem.with.artifacts
    )

}

//======================================================================================================================


def executeArchiveArtifacts(artifacts) {
        // // mvn 工程
        // archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true, fingerprint: true 
        // // gradle 工程
        // archiveArtifacts artifacts: '**/build/libs/*.jar', allowEmptyArchive: true, fingerprint: true

    archiveArtifacts artifacts: artifacts?:'**/build/libs/*.jar', allowEmptyArchive: true, fingerprint: true
}