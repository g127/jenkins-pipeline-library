package com.jia54321.jenkins.steps
import com.jia54321.jenkins.utils.utils


def usesWith(targetItem) {
    def utils = new utils()
    utils.printMessage( "gav with=${targetItem.with}",  "green" )

    executeGav(
        targetItem.with.execCommand,
        targetItem.with.groupId,
        targetItem.with.artifactId,
        targetItem.with.version
    )
}

//======================================================================================================================

/**
 * 取包的的坐标信息
 * @param execCommand     gav execCommand命令
 */
def executeGav(String execCommand, groupId, artifactId, version) {
    //
    def gav = [ groupId : groupId,  artifactId : artifactId, version : version ]
    //
    if (null == groupId || null == artifactId || null == version) {
        if(execCommand) {
            gav = runGavScript(execCommand)
        }
    }

    // 新定义变量 gav 相关
    env['paramsMap_gav_groupId']        = "${gav.groupId}"
    env['paramsMap_gav_artifactId']     = "${gav.artifactId}"
    env['paramsMap_gav_version']        = "${gav.version}"

}


 /**
 * 获取包的坐标信息
 * @param execCommand  gav execCommand命令
 * @return [ groupId : null,  artifactId : null, version : null ]
 */
def runGavScript(String execCommand = "echo groupId='org.test' && echo artifactId='app' && echo version='1.0.0'" ) {
    def utils = new utils()
    def gav = [ groupId : null,  artifactId : null, version : null ]
    // gradle -b build.gradle :meta
    // echo -e `mvn -f pom.xml -Dexec.executable='echo' -Dexec.args='name=${project.name}\\ngroupId=${project.groupId}\\nartifactId=${project.artifactId}\\nversion=${project.version}' --non-recursive exec:exec -q`
    def getGavText = sh(script: execCommand, returnStdout: true).trim()
    def gavInfo = readProperties text: getGavText
    utils.printMessage("获取包坐标 execCommand=${execCommand}, gavInfo=${gavInfo}", "green")
    //
    if (null == gavInfo.get("groupId") || null == gavInfo.get("artifactId") || null == gavInfo.get("version")) {
        throw new Exception('项目的包的坐标信息不全，请先补全 groupId, artifactId, version 信息')
    }

    gav.groupId = gavInfo.get("groupId")
    gav.artifactId = gavInfo.get("artifactId")
    gav.version = gavInfo.get("version")

    //
    return gav 
}

