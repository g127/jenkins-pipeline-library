package com.jia54321.jenkins.steps
import com.jia54321.jenkins.utils.utils


def usesWith(targetItem) {
    def utils = new utils()
    utils.printMessage( "sshPublisher with=${targetItem.with}",  "green" )

    executeSshPublisher(
    	targetItem.with.configName,
		targetItem.with.execCommand,
		targetItem.with.remoteDirectory,
		targetItem.with.removePrefix,
		targetItem.with.sourceFiles
    )

}

//======================================================================================================================


def executeSshPublisher(String configName, execCommand, remoteDirectory, removePrefix, sourceFiles) {
    sshPublisher(
        publishers: [
            sshPublisherDesc(configName: configName, transfers: [
                sshTransfer(
                    cleanRemote: false, 
                    excludes: '', 
                    execCommand:  execCommand,
                    execTimeout: 120000, 
                    flatten: false, 
                    makeEmptyDirs: false, 
                    noDefaultExcludes: false, 
                    patternSeparator: '[, ]+', 
                    remoteDirectory: remoteDirectory?:'/tmp', 
                    remoteDirectorySDF: false, 
                    removePrefix: removePrefix?:'build/libs/', 
                    sourceFiles: sourceFiles?:'**/build/libs/*.jar')
                ], 
                usePromotionTimestamp: false, 
                useWorkspaceInPromotion: false, 
                verbose: false
    )])
}