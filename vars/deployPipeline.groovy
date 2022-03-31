import com.jia54321.jenkins.steps.*
import com.jia54321.jenkins.utils.utils

def call(paramMap) {
    def stepUsesAndRun = new stepUsesAndRun()
    //部署
    stage("部署代码") {
        script {
            // stepUseAndRun
            stepUsesAndRun.step(paramMap.deploy)
        }
    }

}

return this