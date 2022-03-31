import com.jia54321.jenkins.enums.PipelineType
import com.jia54321.jenkins.utils.utils


def call(pipelineType, extParams = [:]) {
    def utils = new utils()
    println "pipelineType=${pipelineType}, extParams=${extParams}"
    
    // 合并
    def paramMap = [:]

    // // 加载默认
    // def cfg_text = libraryResource("pipelineCfg.yaml")

    // // 读取
    // def cfg = readYaml text: cfg_text

    // if ( null != cfg && cfg instanceof Map ) {
    //     for ( c in cfg ) {
    //         // print "key = ${c.key}, value = ${c.value}"
    //         if( null != c.value ) {
    //             paramMap[c.key] = c.value
    //         }
    //     }
    //     // paramMap.putAll(cfg)
    //     println "putAll cfg, paramMap=${paramMap}"
    // }

    if ( null != extParams && extParams instanceof Map ) {
        for ( p in extParams ) {
            // print "key = ${c.key}, value = ${c.value}"
            if( null != p.value ) {
                paramMap[p.key] = p.value
            }
        }
        // paramMap.putAll(extParams)
        println "putAll extParams, paramMap=${paramMap}"
    }

    println "paramMap=${paramMap}"


    pipeline {
        agent any

        // tools {
        //     jdk 'JDK_8'
        //     maven 'M3'
        //     gradle 'Gradle'
        //     git 'GIT'
        //     nodejs 'NodeJS'
        // }

        // 参数集合
        parameters {
          // =======================================================================================================================
          // 动作
          // =======================================================================================================================
          // CD_ACTION 发布动作 standard 标准流程  deploy 仅部署  rollback 回滚, 默认 standard
          choice(name: 'CD_ACTION', choices: ['standard', 'deploy', 'rollback'], description: '请选择标准,发布或回滚，默认为标准（标准：standard, 发布：deploy, 回滚：rollback）')
          // CD_ENV    发布环境   test 或者 prod, 默认test
          choice(name: 'CD_ENV', choices: ['test'], description: '请选择发布环境，默认为测试  (测试：test，生产：prod)')
          // =======================================================================================================================
        }

        options {
            skipDefaultCheckout()  //删除隐式checkout scm语句
            disableConcurrentBuilds() //禁止并行
            timeout(time: 1, unit: 'HOURS')  //流水线超时设置1h
            timestamps()
        }

        stages {
            stage("初始化步骤") {
                steps {
                    script{
                        // 标准流程standard，由 PipelineType 决定
                        if (env.CD_ACTION == 'standard') {
                            //
                            switch (pipelineType) {
                                case PipelineType.STANDARD:
                                    standardPipeline(paramMap)
                                    break
                                case PipelineType.STANDARD_GRADLE:
                                    standardGradlePipeline(paramMap)
                                    break
                                case PipelineType.DEPLOY:
                                    deployPipeline(paramMap)
                                    break
                                case PipelineType.ROLLBACK:
                                    rollbackPipeline(paramMap)
                                    break
                            }

                        }
                        // deploy 仅部署
                        else if (env.CD_ACTION == 'deploy') {
                            deployPipeline(paramMap)
                        } 
                        // rollback 仅回滚
                        else if (env.CD_ACTION == 'rollback') {
                            rollbackPipeline(paramMap)
                        }
                    }

                }
            }
        }

        // =======================================================================================================================
        // 通知 mail
        // =======================================================================================================================
        post {
            always {
                script {
                    //清理工作
                    // utils.clearSpace(["jar", "zip"], paramMap.get("ansible_src"))
                    def robotId = "${paramMap.notify.robotId?:''}"
                    if ( robotId ) {
                        wrap([$class: 'BuildUser']) {
                            buildUser = env.BUILD_USER_ID
                        }
                        
                        //
                        qyWechat([
                            /* 标题 */
                            title:        currentBuild.fullDisplayName, 
                            /* 结果 */
                            result:       currentBuild.currentResult, 
                            /* 部署分支 */
                            branchName:   env.paramsMap_repo_branch?:'', 
                            /* 部署服务 */
                            serviceName:  currentBuild.projectName, 
                            /* 制品库 */
                            archiveUploadUrl:   env.archiveUploadUrl?:'', 
                            /* 部署服务器 */
                            archiveDeployUrl: env.archiveDeployUrl?:'',
                            /* 部署耗时 */
                            duration:     currentBuild.durationString, 
                            /* 部署人 */
                            buildUser:    buildUser?:'',
                            /* 详情url */
                            detailUrl:    currentBuild.absoluteUrl?:'', 
                            /* robotId */
                            robotId:      robotId
                        ])                    
                    }

                } // script
            } // always
        } // post

    } // pipeline

}  // func

return this