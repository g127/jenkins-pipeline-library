import com.jia54321.jenkins.steps.*
import com.jia54321.jenkins.utils.utils


/**
 *  gradle 工程构建模板
 *
 * // 参数说明
 *
 * // 构建文件（build.gradle） 所在路径（需包含末尾 / , 没有可保持空串）
 * // paramMap.ext.gradleBuildFilePath     : ''
 *
 * // 子项目名，没有可保持空串
 * // paramMap.ext.gradleSubProjectName    : ''
 *
 *
 * // 构建过程中的 上传制品的 "制品库" 节点名
 * // paramMap.ext.uploadNodeName          : 'test_node'
 *
 * // 构建过程中的 上传制品的 "制品库" 路径 （根路径-snapshots-项目-服务, 需包含 末尾 /）
 * // paramMap.ext.uploadRootPath          : '/ArtifactRepository/snapshots/shenzhen-baoan-project/pay-trade-core/'
 *
 * // 构建过程中的 上传制品的 "制品库" 路径, 后续命令
 * // paramMap.ext.uploadExecCommand       : 'echo  ----上传制品库 snapshots ----'
 *
 *
 * // 部署过程中的 部署制品的 "环境" 节点名
 * // paramMap.ext.deployNodeName          : 'test_node'
 *
 * // 部署过程中的 部署制品的 "环境" 路径
 * // paramMap.ext.deployRootPath          : '/tmp'
 *
 * // 构建过程中的 部署制品的 "环境" 路径, 后续命令
 * // paramMap.ext.deployExecCommand       : 'echo  ----CD:1创建备份 2备份软件 3启停服务----'
 *
 */
def call(paramMap) {
    //
    def stepUsesAndRun = new stepUsesAndRun()

    //下载代码
    stage("下载代码") { //阶段名称
        script {
            // checkout
            if( paramMap.repo != null && paramMap.repo.size() == 1) {
                def co = paramMap.repo[0]
                def a = ""
                if (paramMap.ext.gradleSubProjectName) {
                    a =":"
                }
                def execCommand = "gradle -b ${paramMap.ext.gradleBuildFilePath?:''}build.gradle ${a}${paramMap.ext.gradleSubProjectName?:''}:meta -q"
                def archiveUploadUrl = "${paramMap.ext.uploadNodeName?:''}:${paramMap.ext.uploadRootPath?:''}"
                def archiveDeployUrl = "${paramMap.ext.deployNodeName?:''}:${paramMap.ext.deployRootPath?:''}"
                paramMap.repo = [
                    co,
                    // [ use: 'gav',       with: [  execCommand: 'gradle -b src/pay-core/build.gradle :pay-trace-core:meta -q' ] ]
                    [ uses: 'gav',       with: [  execCommand: execCommand ] ],
                    // [ use: 'updateEnv', with: [  archiveUrl: '/ArtifactRepository/snapshots/shenzhen-baoan-project/pay-trace-core/' ] ]
                    [ uses: 'updateEnv', with: [  archiveUploadUrl: archiveUploadUrl ] ],
                    [ uses: 'updateEnv', with: [  archiveDeployUrl: archiveDeployUrl ] ]
                ]
            }

            stepUsesAndRun.step(paramMap.repo)
        }
    }

    //代码扫描
    stage("代码扫描") {
        script {
            // stepUsesAndRun
            stepUsesAndRun.step(paramMap.sonar)
        }
    }

    //构建代码
    stage("构建代码") {
        // script
        script {
            // build
            if (paramMap.build == null ) {
                def buildRun0 = "gradle -b ${paramMap.ext.gradleBuildFilePath?:''}build.gradle projects -q"
                def a = ""
                if (paramMap.ext.gradleSubProjectName) {
                    a =":"
                }
                def b = ""
                if (paramMap.ext.gradleSubProjectName) {
                    b ="/"
                }
                def buildRun1 = "gradle -b ${paramMap.ext.gradleBuildFilePath?:'' }build.gradle -PmetaProfile=test ${a}${paramMap.ext.gradleSubProjectName?:''}:clean ${a}${paramMap.ext.gradleSubProjectName?:''}:build -x ${a}${paramMap.ext.gradleSubProjectName?:''}:test -q "
                def execCommand = "${paramMap.ext.uploadExecCommand?:'echo  ----上传制品库 snapshots ----'}"
                paramMap.build = [
                  [ run : buildRun0 ],
                  [ run : buildRun1 ],
                  [ uses: 'archiveArtifacts', with: [ artifacts: "${paramMap.ext.gradleBuildFilePath?:''}build/libs/${paramMap.ext.gradleSubProjectName?:'*'}-${env.paramsMap_gav_version?:''}*.jar" ] ],
                  [ uses: 'sshPublisher', with: [
                      configName: "${paramMap.ext.uploadNodeName?:'test_node'}",
                      execCommand: execCommand,
                      remoteDirectory: "${paramMap.ext.uploadRootPath?:''}",
                      removePrefix: "${paramMap.ext.gradleBuildFilePath?:''}${paramMap.ext.gradleSubProjectName?:''}${b}build/libs/",
                      sourceFiles: "${paramMap.ext.gradleBuildFilePath?:''}${paramMap.ext.gradleSubProjectName?:''}${b}build/libs/${paramMap.ext.gradleSubProjectName?:'*'}-${env.paramsMap_gav_version?:''}*.jar"
                    ]
                  ],
                ]
            }
            // stepUsesAndRun
            stepUsesAndRun.step(paramMap.build)
        }
    }

    //iq-sever扫描
    stage("包扫描") {
        script {
            // nexus.iqServerScan(paramMap)
        }
    }

    //部署
    stage("部署代码") {
        script {
            def a = ""
            if (paramMap.ext.gradleSubProjectName) {
                a =":"
            }
            def b = ""
            if (paramMap.ext.gradleSubProjectName) {
                b ="/"
            }
            if (paramMap.deploy == null ) {
                def execCommand = "${paramMap.ext.deployExecCommand?: 'echo  ----CD:1创建备份 2备份软件 3启停服务----'}"
                paramMap.deploy = [
                  [
                    uses: 'sshPublisher',
                    with: [
                      configName: "${paramMap.ext.deployNodeName?:'test_node'}",
                      execCommand: execCommand,
                      remoteDirectory: "${paramMap.ext.deployRootPath?:'/tmp'}",
                      removePrefix: "${paramMap.ext.gradleBuildFilePath?:''}${paramMap.ext.gradleSubProjectName?:''}${b}build/libs/",
                      sourceFiles: "${paramMap.ext.gradleBuildFilePath?:''}${paramMap.ext.gradleSubProjectName?:''}${b}build/libs/${paramMap.ext.gradleSubProjectName?:'*'}-${env.paramsMap_gav_version?:''}*.jar"
                    ]
                  ]
                ]
            }

            // stepUsesAndRun
            stepUsesAndRun.step(paramMap.deploy)
        }
    }

} // func

return this
