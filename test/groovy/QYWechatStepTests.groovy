import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

class QYWechatStepTests extends BaseTest {
    static final String scriptName = 'vars/qyWechat.groovy'

    def robotId
    @Override
    @Before
    void setUp() {
        super.setUp()
        // It is expected to be on Maven by default. Override this behavior when you need to specialize
        helper.registerAllowedMethod('fileExists', [String.class], { s -> return s.equals('pom.xml') })
        env.NODE_LABELS = 'docker'
        env.JOB_NAME = 'build/plugin/test'

        robotId = "430d0627-748b-4905-96f8-945edcc09b16"
    }

    @Test
    void test_qyWechat_with_notify() {
        Script script = loadScript(scriptName)
        script.call([
                /* 标题 */
                title:              "标题测试",
                /* 结果 */
                result:             "结果测试",
                /* 部署分支 */
                branchName:         "部署分支测试",
                /* 部署服务 */
                serviceName:        "部署服务测试",
                /* 制品库 */
                archiveUploadUrl:   "制品库测试",
                /* 部署服务器 */
                archiveDeployUrl:   "部署服务器",
                /* 部署耗时 */
                duration:           "部署耗时",
                /* 部署人 */
                buildUser:          "部署人测试",
                /* 详情url */
                detailUrl:          "详情url测试",
                /* robotId */
                robotId:            robotId
        ])
        printCallStack()
        assertJobStatusSuccess()
    }

}
