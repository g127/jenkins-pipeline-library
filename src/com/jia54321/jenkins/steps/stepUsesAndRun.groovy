package com.jia54321.jenkins.steps

import com.jia54321.jenkins.utils.utils
 
//
def step( target ) {
    // def utils = new utils()
    // utils.printMessage( "step=${target}",  "green" )
    //
    if ( target instanceof Map ) {
        if( target.run || target.uses ) {
            stepItem(target)
        }
    }
    //
    if ( target instanceof ArrayList ) {
        for ( targetItem in target ) {
            if( targetItem.run || targetItem.uses ) {
                stepItem(targetItem)
            }
        }  
    }
}

//======================================================================================================================

//
def stepItem( targetItem ) {
    // def utils = new utils()
    // utils.printMessage( "stepItem=${targetItem}",  "green" )

    if( targetItem.run ) {
        runShellWithEnv(targetItem)
    }
    else if(targetItem.uses) {
        switch(targetItem.uses) {
            //
            case 'ansiblePlaybook':
                def ansiblePlaybook = new com.jia54321.jenkins.steps.ansiblePlaybook()
                ansiblePlaybook.usesWith(targetItem)
            break
            //
            case 'checkout':
                def checkout = new com.jia54321.jenkins.steps.checkout()
                checkout.usesWith(targetItem)
            break
            //
            case 'gav':
                def gav = new com.jia54321.jenkins.steps.gav()
                gav.usesWith(targetItem)
            break
            case 'archiveArtifacts':
                def archiveArtifacts = new com.jia54321.jenkins.steps.archiveArtifacts()
                archiveArtifacts.usesWith(targetItem)
            break
            // 
            case 'nexusPublisher':
                def nexusPublisher = new com.jia54321.jenkins.steps.nexusPublisher()
                nexusPublisher.usesWith(targetItem)
            break
            // 
            case 'sonarScan':
                def sonarScan = new com.jia54321.jenkins.steps.sonarScan()
                sonarScan.usesWith(targetItem)
            break
            // 
            case 'sshPublisher':
                def sshPublisher = new com.jia54321.jenkins.steps.sshPublisher()
                sshPublisher.usesWith(targetItem)
            break
            // 
            case 'updateEnv':
                def updateEnv = new com.jia54321.jenkins.steps.updateEnv()
                updateEnv.usesWith(targetItem)
            break 
            //
            default:
                utils.printMessage( "step target unknown ${targetItem.uses}",  "green" )
            break
        }
    }

}

def runShellWithEnv(targetItem) {
    def utils = new utils()
    utils.printMessage( "shell run=${targetItem.run}",  "green")
	sh(script:targetItem.run)
}
