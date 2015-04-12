import sbt._
import Keys._
import sbtrelease.ReleasePlugin.{ReleaseKeys => release, releaseSettings}
import sbtrelease.{ReleaseStep, releaseTask}
import sbtrelease.ReleaseStateTransformations._
import org.qirx.sbtrelease.UpdateVersionInFiles.updateVersionInFiles
import com.typesafe.sbt.SbtPgp.PgpKeys.publishSigned
import xerial.sbt.Sonatype.SonatypeKeys.sonatypeReleaseAll
import sbt.ScriptedPlugin.scripted

object ReleaseSettings {
  
  def rootProjectSettings = 
    releaseSettings ++ 
    Seq(
      release.crossBuild := true,
      
      release.releaseProcess := 
        Seq(
          checkSnapshotDependencies,
          inquireVersions,
          runTest,
          runTestProjects,
          setReleaseVersion,
          createReadme,
          updateVersionInReadme,
          commitReleaseVersion,
          tagRelease,
          publishSignedArtifacts,
          sonatypeRelease,
          setNextVersion,
          commitNextVersion,
          pushChanges
        ),
        
        runTestProjectsTask <<= {
          val task = scripted.toTask("")
          val scopedTask = task.all(ScopeFilter(inAggregates(ThisProject, includeRoot = false)))
          scopedTask.map(_ => ())
        },
        
        createReadmeTask := {
          streams.value.log.info("Creating README.md")
          val base = baseDirectory.value
          val staticFiles = 
            (base * "*.md").get.filterNot(_.name == "README.md").sorted
          val documentationFiles = 
            (base / "documentation" * "*.md").get.sorted
          val files = staticFiles ++ documentationFiles
          val content = 
            "*This file is generated using sbt*\n\n" +
            files.map(file => IO.read(file)).mkString("\n\n")
          IO.write(readme, content)
       }
    )
  
  lazy val runTestProjectsTask = taskKey[Unit]("runTestProjects")
  
  lazy val runTestProjects = ReleaseStep(
    action = releaseTask(runTestProjectsTask in ThisProject), 
    enableCrossBuild = true)
  
  lazy val createReadmeTask = taskKey[Unit]("createReadme")

  lazy val createReadme = releaseTask(createReadmeTask in ThisProject)

  lazy val readme = file("README.md")

  lazy val updateVersionInReadme = updateVersionInFiles(Seq(readme, file("extra/documentation/README.md")))

  lazy val publishSignedArtifacts = ReleaseStep(
    action = publishSignedArtifactsAction, 
    enableCrossBuild = true)

  lazy val publishSignedArtifactsAction = { st: State =>
    val extracted = Project.extract(st)
    val ref = extracted.get(thisProjectRef)
    extracted.runAggregated(publishSigned in Global in ref, st)
  }

  lazy val sonatypeRelease = releaseTask(sonatypeReleaseAll in ThisProject)
}
