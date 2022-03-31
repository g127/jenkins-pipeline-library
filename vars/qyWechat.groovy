import com.jia54321.jenkins.steps.*
import com.jia54321.jenkins.utils.utils


def call(Map notify = [
    /* 标题 */
    title: '',
    /* 结果 */
    result: '',
    /* 部署分支 */
    branchName: '',
    /* 部署服务 */
    serviceName: '',
    /* 制品库utl */
    archiveUploadUrl: '',
    /* 部署服务器 */
    archiveDeployUrl: '',
    /* 部署耗时 */
    duration: '',
    /* 部署人 */
    buildUser: '',
    /* 详情url */
    detailUrl: '',
    /* robotId */
    robotId:''
]) {

    if (!notify.robotId) {
        return
    }

    def contentBuf = new StringBuilder()
    // head
    contentBuf.append("\"${notify.title} <font color=\\\"info\\\">${notify.result}</font>").append('\n')
    //
    contentBuf.append(">部署分支：<font color=\\\"comment\\\">${notify.branchName?:''}</font>").append('\n')
    contentBuf.append(">部署服务：<font color=\\\"comment\\\">${notify.serviceName}</font>").append('\n')
    if(notify.archiveUploadUrl) {
        contentBuf.append(">制品库：<font color=\\\"comment\\\">${notify.archiveUploadUrl?:''}</font>").append('\n')
    }
    if(notify.archiveDeployUrl) {
        contentBuf.append(">部署服务器：<font color=\\\"comment\\\">${notify.archiveDeployUrl?:''}</font>").append('\n')
    }
    contentBuf.append(">部署耗时：<font color=\\\"comment\\\">${notify.duration}</font>").append('\n')
    if(notify.buildUser) {
        contentBuf.append(">部署人：<font color=\\\"comment\\\">${notify.buildUser?:''}</font>").append('\n')
    }
    contentBuf.append(">[查看详情](${notify.detailUrl?:''}) \"")

    def content = contentBuf.toString()
    //
    def body = "{ \"msgtype\": \"markdown\", \"markdown\": { \"content\": ${content} } }"

    sh(returnStdout: true, script: """
           curl 'https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${notify.robotId}'  \
                -H 'content-type:application/json'  \
                -d '${body}' 
        """).trim()
}
