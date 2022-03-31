@Library('jenkinslibs@dev') _

import com.jia54321.jenkins.enums.PipelineType

// 加载流水线
pipelineLoad(PipelineType.STANDARD, [
    // 下载代码, 并获取gav
    repo: [
      [ uses: 'checkout',  with: [  url: 'http://192.168.110.252:9999/fly/park-service.git', branch: 'baoan-qa', credentialsId: 'repo_git_secret' ] ],
      [ uses: 'gav',       with: [  execCommand: 'gradle -b member_group/park-service/build.gradle :park-service:meta -q' ] ],
      [ uses: 'updateEnv', with: [  archiveUrl: '/ArtifactRepository/snapshots/shenzhen-baoan-project/park-service/' ] ]
    ],
    // 构建代码
    build: [ 
      [ run : 'gradle -b member_group/build.gradle projects -q ' ],
      [ run : 'gradle -b member_group/build.gradle -PmetaProfile=test :park-service:clean :park-service:build -q ' ],
      [ uses: 'archiveArtifacts', with: [ artifacts: '**/build/libs/park-service-*.jar' ] ],
      [ uses: 'sshPublisher', with: [
          configName: 'test_node',
          execCommand: 'echo  ----上传制品库 snapshots ----',
          remoteDirectory: '/ArtifactRepository/snapshots/shenzhen-baoan-project/park-service/',
          removePrefix: 'member_group/park-service/build/libs/',
          sourceFiles: 'member_group/park-service/build/libs/park-service-*.jar'
        ]
      ],
    ],
    // 部署代码
    deploy: [
      [ 
        uses: 'sshPublisher', 
        with: [
          configName: 'test_node',
          execCommand: 'echo  ----CD:1创建备份 2备份软件 3启停服务----',
          remoteDirectory: '/tmp',
          removePrefix: 'member_group/park-service/build/libs/',
          sourceFiles: 'member_group/park-service/build/libs/park-service-*.jar'
        ]
      ]
    ],

    notify: [
      robotId: '430d0627-748b-4905-96f8-945edcc09b16'
    ]
])