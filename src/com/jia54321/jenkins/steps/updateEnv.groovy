package com.jia54321.jenkins.steps
import com.jia54321.jenkins.utils.utils


def usesWith(targetItem) {
    def utils = new utils()
    utils.printMessage( "updateEnv with=${targetItem.with}",  "green" )

    for(e in targetItem.with) {
        env["${e.key}"] = "${e.value}"
    }

    for(e in targetItem.with) {
        println env["${e.key}"]
    }
}
