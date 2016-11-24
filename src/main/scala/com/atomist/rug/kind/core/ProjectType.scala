package com.atomist.rug.kind.core

import com.atomist.project.ProjectOperationArguments
import com.atomist.rug.parser.Selected
import com.atomist.rug.runtime.{DefaultEvaluator, Evaluator}
import com.atomist.rug.spi.{MutableView, ReflectivelyTypedType, Type}
import com.atomist.source.ArtifactSource

class ProjectType(
                   evaluator: Evaluator
                 )
  extends Type(evaluator)
    with ReflectivelyTypedType {

  def this() = this(DefaultEvaluator)

  override def name = "project"

  override def description =
    """
      |Type for a project. Supports global operations.
      |Consider using file and other lower types by preference as project
      |operations can be inefficient.
    """.stripMargin

  override def viewManifest: Manifest[ProjectMutableView] = manifest[ProjectMutableView]

  override protected def findAllIn(rugAs: ArtifactSource, selected: Selected,
                                   context: MutableView[_],
                                   poa: ProjectOperationArguments, identifierMap: Map[String, Object]): Option[Seq[MutableView[_]]] = {
    // Special case where we want only one
    context match {
      case pmv: ProjectMutableView =>
        Some(Seq(pmv))
      case _ => None
    }
  }
}

object ProjectType {

  /**
    * File containing provenance information in root of edited projects
    */
  val ProvenanceFilePath = "provenance.txt"

}