addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8.3")

libraryDependencies += "org.qirx" %% "sbt-release-custom-steps" % "0.5"

resolvers += "Rhinofly Internal Repository" at "http://maven-repository.rhinofly.net:8081/artifactory/libs-release-local"

