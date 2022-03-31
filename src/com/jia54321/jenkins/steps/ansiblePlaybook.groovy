package com.jia54321.jenkins.steps
import com.jia54321.jenkins.utils.utils


def usesWith(targetItem) {
    def utils = new utils()
    utils.printMessage( "ansiblePlaybook with=${targetItem.with}",  "green" )

    def playbookParams = [:]
    if ( targetItem.with.playbookParams instanceof Map ) {
        playbookParams = targetItem.with.playbookParams
    }

    // // 执行
    // withCredentials([
    //     string(credentialsId: 'DEPLOY_TOKEN', variable: 'deployToken'), 
    //     usernamePassword(credentialsId: "DEPLOY_${params.get("env_info")}_nexus", passwordVariable: 'password', usernameVariable: 'username')
    // ]) {
    //     println("开始部署")
    //     params.put('username', "${username}")
    //     params.put('password', "${password}")
    //     params.put('token_info', "${deployToken}")
    //     executePlaybookWithPath(params)
    // }

    // TODO 
    executeAnsiblePlaybook(
        targetItem.with.credentialsId, 
        targetItem.with.installation, 
        targetItem.with.playbook, 
        targetItem.with.inventory,
        playbookParams
    )
}


//======================================================================================================================



def executeAnsiblePlaybook(credentialsId, installation, playbook, inventory, playbookParams) {
    ansiColor('xterm') {
        ansiblePlaybook(
            colorized: true,
            credentialsId: credentialsId?:"ansible_user",
            installation: installation?:'Ansible',
            playbook: playbook,
            inventory: inventory,
            extraVars: playbookParams?:[:]
        )
    }
}