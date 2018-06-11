import com.typesafe.tools.mima.core._
import com.typesafe.tools.mima.plugin.MimaKeys._

val commonSettings = Seq(
  scodecModule := "scodec-cats",
  rootPackage := "scodec.cats",
  contributors ++= Seq(
    Contributor("mpilquist", "Michael Pilquist"),
    Contributor("durban", "Daniel Urban")
  ),
  crossScalaVersions := crossScalaVersions.value.filterNot { _.startsWith("2.13") } // Cats not yet built for 2.13
)

lazy val root = project.in(file(".")).aggregate(coreJVM, coreJS).settings(commonSettings: _*).settings(
  publishArtifact := false
)

val catsVersion = "1.1.0"

lazy val core = crossProject.in(file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(commonSettings: _*).
  settings(scodecPrimaryModule: _*).
  jvmSettings(scodecPrimaryModuleJvm: _*).
  settings(
    libraryDependencies ++= Seq(
      "org.scodec" %%% "scodec-core" % "1.10.3",
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.typelevel" %%% "cats-laws" % catsVersion % "test",
      "org.scalatest" %%% "scalatest" % "3.0.3" % "test",
      "org.typelevel" %%% "discipline" % "0.8" % "test"
    )
  ).
  jvmSettings(
    docSourcePath := new File(baseDirectory.value, ".."),
    OsgiKeys.exportPackage := Seq("scodec.interop.cats.*;version=${Bundle-Version}"),
    OsgiKeys.importPackage := Seq(
      """scodec.*;version="$<range;[==,=+);$<@>>"""",
      """scala.*;version="$<range;[==,=+);$<@>>"""",
      """cats.*;version="$<range;[==,=+);$<@>>"""",
      "*"
    ),
    binaryIssueFilters ++= Seq(
    )
  ).
  jsSettings(commonJsSettings: _*)

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

