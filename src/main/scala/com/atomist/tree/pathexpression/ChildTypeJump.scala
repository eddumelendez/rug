package com.atomist.tree.pathexpression

import com.atomist.rug.kind.DefaultTypeRegistry
import com.atomist.rug.kind.dynamic.ChildResolver
import com.atomist.rug.spi.{MutableView, TypeRegistry}
import com.atomist.tree.TreeNode

/**
  * Descend to the given type name, looking first for direct children,
  * and otherwise trying to resolve using ChildResolver.
  *
  * @param typeName name of the type to descend to
  */
case class ChildTypeJump(typeName: String) extends AxisSpecifier {

  private val typeRegistry: TypeRegistry = DefaultTypeRegistry

  private val _childResolver: Option[ChildResolver] = typeRegistry.findByName(typeName) match {
    case Some(cr: ChildResolver) => Some(cr)
    case None => throw new IllegalArgumentException(s"No type with name [$typeName]")
    case _ =>
      // Doesn't support contextless resolution
      None
  }

  private def childResolver = _childResolver.getOrElse(
    throw new IllegalArgumentException(s"Type [$typeName] does not support contextless resolution")
  )

  def follow(tn: TreeNode): Seq[TreeNode] = tn match {
    case mv: MutableView[_] =>
      childResolver.findAllIn(mv).getOrElse(Nil)
    case x =>
      throw new UnsupportedOperationException(s"Type ${x.getClass} not yet supported for resolution")
  }
}
