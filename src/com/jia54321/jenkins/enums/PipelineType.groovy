package com.jia54321.jenkins.enums

/**
 * PipelineType
 */
enum PipelineType {
	/** 标准流水线 */
    STANDARD,
	/** gradle工程流水线 */
    STANDARD_GRADLE,
    /** 部署流水线 */
    DEPLOY,
    /** 回滚部署流水线 */
    ROLLBACK
}