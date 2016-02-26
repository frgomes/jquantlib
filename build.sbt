import sbt._
import Keys._
import com.typesafe.sbt.SbtPgp.PgpKeys.publishSigned
import xerial.sbt.Sonatype.sonatypeSettings

val `app.organization` = "org.jquantlib.java"
val `app.name`         = None
val `app.description`  = "JQuantLib is a library for Quantitative Finance written in 100% Java."
val `app.license`      = ("BSD Simplified", url("http://opensource.org/licenses/BSD-2-Clause"))

val `bintray.organization`  = Some("jquantlib.org")
val `bintray.packagelabels` = Seq("java", "scala", "quantitative", "finance", "models")

val `java.version`            = "1.7"
val `junit.version`           = "4.12"
val `junit-interface.version` = "0.11"
val `slf4j.version`           = "1.4.0"
val `jcip.version`            = "1.0"
val `jfreechart.version`      = "1.0.0"

// compilation --------------------------------------------------------------------------------------------------

lazy val librarySettings : Seq[Setting[_]] =
  Seq(
    autoScalaLibrary :=  false,
    crossPaths       :=  false,
    javacOptions     ++=
      Seq(
        "-encoding", "UTF-8"
    )
  )

lazy val disableJavadocs : Seq[Setting[_]] =
  Seq(
    publishArtifact in (Compile, packageDoc) := false,
    publishArtifact in packageDoc := false,
    sources in (Compile,doc) := Seq.empty
  )

lazy val javadocSettings : Seq[Setting[_]] =
  Seq(
    javacOptions  in (Compile,compile) ++= Seq("-source", `java.version`, "-target", `java.version`, "-Xlint"),
    javacOptions  in (Compile,doc)     ++= Seq("-Xdoclint", "-notimestamp", "-linksource")
  )

// test frameworks ----------------------------------------------------------------------------------------------

lazy val junitSettings : Seq[Setting[_]] =
  Seq(
    libraryDependencies ++= Seq(
      "junit"        % "junit"           % `junit.version`           % "test",
      "com.novocode" % "junit-interface" % `junit-interface.version` % "test" ),
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")
  )

// dependencies -------------------------------------------------------------------------------------------------

lazy val deps_common : Seq[Setting[_]] =
  Seq(
    libraryDependencies ++= Seq(
      "net.jcip"               %  "jcip-annotations"                      % `jcip.version`,
      "org.slf4j"              %  "slf4j-api"                             % `slf4j.version`,
      "org.slf4j"              %  "slf4j-simple"                          % `slf4j.version`       % "test"
    )
  )

lazy val deps_samples : Seq[Setting[_]] =
  Seq(
    libraryDependencies ++= Seq(
      "jfree"                  %  "jfreechart"                            % `jfreechart.version`
    )
  )

// projects -----------------------------------------------------------------------------------------------------

def makeRoot(p: sbt.Project): sbt.Project =
  p.settings(
    disablePublishing ++
      Seq(
        organization := `app.organization`,
        version      := (version in ThisBuild).value,
        description  := `app.description`,
        licenses     += `app.license`
      ): _*)

def makeModule(p: sbt.Project): sbt.Project =
  p.settings(
    librarySettings ++
    publishSettings ++
    //paranoidOptions ++
    //javadocSettings ++
    disableJavadocs ++
    junitSettings ++
    // otestFramework ++
    deps_common ++
      Seq(
        organization := `app.organization`,
        version      := (version in ThisBuild).value,
        description  := `app.description`,
        licenses     += `app.license`
      ): _*)


lazy val root =
   makeRoot(project.in(file(".")))
    .aggregate(core, helpers, contrib, samples)

lazy val core =
  makeModule(project.in(file("jquantlib")))

lazy val helpers =
  makeModule(project.in(file("jquantlib-helpers")))
    .dependsOn(core)

lazy val contrib =
  makeModule(project.in(file("jquantlib-contrib")))
    .dependsOn(core)

lazy val samples =
  makeModule(project.in(file("jquantlib-samples")))
    .dependsOn(core, helpers)
    .settings(deps_samples:_*)

// publish settings ---------------------------------------------------------------------------------------------

lazy val disablePublishing: Seq[Setting[_]] =
  sonatypeSettings ++
    Seq(
      publishArtifact := false,
      publishSigned := (),
      publish := (),
      publishLocal := ()
    )

lazy val publishSettings: Seq[Setting[_]] =
  Seq(
    name                 in bintray := name.value,
    bintrayOrganization  in bintray := `bintray.organization`,
    bintrayRepository    in bintray := (if((version in ThisBuild).value.contains("SNAPSHOT")) "snapshots" else "releases"),
    bintrayPackageLabels in bintray := `bintray.packagelabels`,
    pomExtra := `pom.extra`
  )

// --------------------------------------------------------------------------------------------------------------

val `pom.extra` =
  <scm>
    <developerConnection>scm:git:git@github.com:frgomes/jquantlib.git</developerConnection>
                 <connection>scm:git:github.com/frgomes/jquantlib.git</connection>
                         <url>http://github.com/frgomes/jquantlib</url>
  </scm>
  <developers>
    <developer>
      <id>frgomes</id>
      <name>Richard Gomes</name>
      <url>http://rgomes-info.blogspot.com</url>
    </developer>
  </developers>

// --------------------------------------------------------------------------------------------------------------




/*
TODO: Translate to SBT

    //TODO: doclet  'gr.spinellis:UmlGraph:4.9.0'
    //TODO: //-- doclet  'org.umlgraph:doclet:5.1'
    //TODO: //-- taglets 'net.sf.latextaglet:latextaglet:0.1.2'
    //TODO: taglets 'org.jquantlib:latextaglet:1.0-SNAPSHOT'

    task umlgraph(type: Javadoc) {
        description = "JQuantLib $version API Javadoc"
        classpath = files( new File("$projectDir/src/main/java") )
        source = sourceSets.main.allJava

        options.doclet     = "gr.spinellis.umlgraph.doclet.UmlGraphDoc"
        // options.doclet     = "org.umlgraph.doclet.UmlGraphDoc"

        options.docletpath = configurations.doclet as List
        options.taglets    = [ 'net.sf.latextaglet.LaTeXBlockEquationTaglet','net.sf.latextaglet.LaTeXEquationTaglet','net.sf.latextaglet.LaTeXInlineTaglet' ]
        options.tagletPath = configurations.taglets as List
        options.addStringOption('inferrel')
        options.addStringOption('inferdep')
        options.addStringOption('quiet')
        options.addStringOption('qualify')
        options.addStringOption('postfixpackage')
        options.addStringOption('hide', 'java.*')
        options.addStringOption('collpackages', 'java.util.*')
        options.addStringOption('nodefontsize', '9')
        options.addStringOption('nodefontpackagesize', '7')
    }


    task demo(type: JavaExec) {
        description = 'Run EquityOptions'
        classpath configurations.demo
        main = 'org.jquantlib.samples.EquityOptions'
    }

*/
