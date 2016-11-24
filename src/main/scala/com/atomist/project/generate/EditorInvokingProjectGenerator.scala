package com.atomist.project.generate

import com.atomist.param._
import com.atomist.project.ProjectOperationArguments
import com.atomist.project.common.InvalidParametersException
import com.atomist.project.common.support.ProjectOperationSupport
import com.atomist.project.edit.{FailedModificationAttempt, NoModificationNeeded, ProjectEditor, SuccessfulModification}
import com.atomist.rug.UndefinedProjectNameParameterException
import com.atomist.source.{ArtifactSource, ArtifactSourceCreationException}
import com.typesafe.scalalogging.LazyLogging

/**
  * The heart of the new "templates are just editor invocations" support
  */
class EditorInvokingProjectGenerator(val name: String,
                                     val editor: ProjectEditor,
                                     val startProject: ArtifactSource)
  extends ProjectGenerator
    with ParameterizedSupport
    with ProjectOperationSupport
    with LazyLogging {

  for (p <- editor.parameters) {
    addParameter(p)
  }

  for (t <- editor.tags) {
    addTag(t)
  }

  override def description(): String = editor.description

  @throws(classOf[InvalidParametersException])
  override def generate(poa: ProjectOperationArguments): ArtifactSource = {
    ensureMandatoryProjectNameParameter(poa)
    editor.modify(startProject, poa) match {
      case sm: SuccessfulModification =>
        sm.result
      case nmn: NoModificationNeeded =>
        logger.warn(s"Editor-generator $name modified no files from start project ${startProject.id}. This probably indicates invalidate arguments or a bad template.")
        startProject
      case fm: FailedModificationAttempt =>
        throw new ArtifactSourceCreationException(s"Editor-generator $name failed with message: ${fm.failureExplanation}")
    }
  }

  private def ensureMandatoryProjectNameParameter(poa: ProjectOperationArguments): Unit = {
    val mandatoryDefaultProjectNameParameter = "project_name"

    if (!poa.parameterValueMap.contains(mandatoryDefaultProjectNameParameter))
      throw new UndefinedProjectNameParameterException(name,
        s"'project_name' parameter is mandatory for generators but not present " +
          s"Known parameters are [${poa.parameterValueMap.keys.mkString(",")}]",
        name)
  }
}
