package com.jia54321.jenkins.enums

enum BuildType {
    /** mvn */
    MVN('mvn'),
    /** npm */
    NPM('npm'),
    /** gradle */
    GRADLE('gradle');

    private String code;

    BuildType(String code) {
        this.code = code
    }

    public String getCode() {
        return code;
    }

    public static BuildType valueOfName(String type) {
        return values().find {
            BuildType it ->
                it.code == type
        }
    }

}