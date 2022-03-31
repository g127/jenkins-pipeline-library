package com.jia54321.jenkins.steps

import com.jia54321.jenkins.utils.utils

def usesWith(targetItem) {
    def utils = new utils()
    utils.printMessage( "checkOut with=${targetItem.with}",  "green" )

    executeCheckout(
    	targetItem.with.url, 
    	targetItem.with.branch, 
    	targetItem.with.credentialsId, 
    	targetItem.with.scmClass?:'GitSCM'
    )


}

//======================================================================================================================


/**
 * 检出代码
 * @param url            配置库地址
 * @param branch         配置库分支
 * @param credentialsId  配置库SK
 * @param scmClass          GitSCM, SubversionSCM
 * @return
 */
def executeCheckout(url = '', branch = 'master', credentialsId = 'repo_git_secret', scmClass = 'GitSCM') {
    // SubversionSCM
    // GitSCM
    checkout([$class                           : scmClass, branches: [[name: branch ]],
              doGenerateSubmoduleConfigurations: false,
              extensions                       : [],
              submoduleCfg                     : [],
              userRemoteConfigs                : [[credentialsId: credentialsId, url: url ]]])

    // 新定义变量 repo 相关
    env['paramsMap_repo_url']           = "${url}"
    env['paramsMap_repo_branch']        = "${branch}"
    env['paramsMap_repo_credentialsId'] = "${credentialsId}"
    env['paramsMap_repo_scmClass']      = "${scmClass}"
}