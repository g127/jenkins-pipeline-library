package com.jia54321.jenkins.steps
import com.jia54321.jenkins.utils.utils


def usesWith(targetItem) {
    def utils = new utils()
    utils.printMessage( "sonarScan with=${targetItem.with}",  "green" )

    executeSonarScan(
        targetItem.with.projectKey?:'${env.artifactId}',
        targetItem.with.projectName?:'${env.artifactId}',
        targetItem.with.sources?:'.'
    )

}


/**
 * runSonarScannerUseScript
 * @param projectKey
 * @param projectName
 * @param sources
 * @return
 */
def executeSonarScan(String projectKey, String projectName, String sources = '.') {
    withSonarQubeEnv('SonarQube') {
        def scannerHome = tool 'Sonar_Scanner'
        sh """
            ${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${projectKey} \
                -Dsonar.projectName=${projectName} \
                -Dsonar.sources=${sources}
        """
    }
}
