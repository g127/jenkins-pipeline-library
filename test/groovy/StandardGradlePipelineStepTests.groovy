import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

class StandardGradlePipelineStepTests extends BaseTest {
    static final String scriptName = 'vars/standardGradlePipeline.groovy'

    @Override
    @Before
    void setUp() {
        super.setUp()
        // It is expected to be on Maven by default. Override this behavior when you need to specialize
        helper.registerAllowedMethod('fileExists', [String.class], { s -> return s.equals('pom.xml') })
        env.NODE_LABELS = 'docker'
        env.JOB_NAME = 'build/plugin/test'
    }

    @Test
    void test_standardGradlePipeline_with_normal() {
//        Script script = loadScript(scriptName)
//        script.call([
//                // ext
//                ext: [
//                        // 构建文件（build.gradle） 所在路径（需包含末尾 / , 没有可保持空串）
//                        gradleBuildFilePath         : 'src/pay-core/',
//                        // 子项目名，没有可保持空串
//                        gradleSubProjectName        : 'pay-trace-core',
//
//                        // 构建过程中的 上传制品的 "制品库" 节点名
//                        uploadNodeName              : 'test_node',
//                        // 构建过程中的 上传制品的 "制品库" 路径 （根路径-snapshots-项目-服务, 需包含 末尾 /）
//                        uploadRootPath              : '/ArtifactRepository/snapshots/shenzhen-baoan-project/pay-trace-core/',
//                        // 构建过程中的 上传制品的 "制品库" 路径, 后续命令
//                        uploadExecCommand           : 'echo  ----上传制品库 snapshots ----',
//
//                        // 部署过程中的 部署制品的 "环境" 节点名
//                        deployNodeName              : 'test_node',
//                        // 部署过程中的 部署制品的 "环境" 路径
//                        deployRootPath              : '/tmp',
//                        // 构建过程中的 部署制品的 "环境" 路径, 后续命令
//                        deployExecCommand           : 'echo  ----CD:1创建备份 2备份软件 3启停服务----'
//                ],
//                // 下载代码, 并获取gav
//                repo: [
//                        [ uses: 'checkout',  with: [  url: 'http://192.168.110.252:9999/fly/tnar_pay.git', branch: 'baoan_from_tpay-core-xwang', credentialsId: 'repo_git_secret' ] ]
//                ],
//                // 推送 robotId
//                notify: [ robotId: '430d0627-748b-4905-96f8-945edcc09b16' ]
//        ])
//        printCallStack()
//        assertJobStatusSuccess()
    }

}

