import com.jia54321.jenkins.steps.*
import com.jia54321.jenkins.utils.utils

def call(paramMap) {
    //
    def stepUsesAndRun = new stepUsesAndRun()

    //下载代码
    stage("下载代码") { //阶段名称
        script {
            stepUsesAndRun.step(paramMap.repo)
        }
    }

    //代码扫描
    stage("代码扫描") {
        script {
            stepUsesAndRun.step(paramMap.sonar)  
        }
    }

    //构建代码
    stage("构建代码") {
        script {
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
            // stepUseAndRun
            stepUsesAndRun.step(paramMap.deploy)
        }
    }

} // func

return this