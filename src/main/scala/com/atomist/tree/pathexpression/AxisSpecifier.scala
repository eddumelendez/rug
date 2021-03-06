package com.atomist.tree.pathexpression

import com.atomist.tree.{ContainerTreeNode, TreeNode}
import com.atomist.util.{Visitable, Visitor}
import com.fasterxml.jackson.annotation.JsonProperty

import scala.collection.mutable.ListBuffer

/**
  * Inspire by XPath axis specifier concept. Represents a direction
  * of navigation from a node.
  */
trait AxisSpecifier {

  @JsonProperty
  def name: String = {
    // Get rid of the trailing $ from an object
    getClass.getSimpleName.replace("$", "")
  }

  override def toString: String = name

}

/**
  * The current node
  */
object Self extends AxisSpecifier

/**
  * Any child of the current node
  */
object Child extends AxisSpecifier

/**
  * Any descendant of the current node.
  */
object Descendant extends AxisSpecifier {

  // TODO this is very inefficient and needs to be optimized.
  // Subclasses can help, or knowing a plan
  def allDescendants(tn: TreeNode): Seq[TreeNode] = tn match {
    case ctn: ContainerTreeNode =>
      val v = new SaveAllDescendantsVisitor
      ctn.accept(v, 0)
      // Remove this node
      v.nodes.diff(Seq(tn))
    case x => Nil
  }

  private class SaveAllDescendantsVisitor extends Visitor {

    private val _nodes = ListBuffer.empty[TreeNode]

    override def visit(v: Visitable, depth: Int): Boolean = v match {
      //    case ctn: ContainerTreeNode =>
      //      true
      case tn: TreeNode =>
        _nodes.append(tn)
        true
    }

    def nodes: Seq[TreeNode] = _nodes.distinct
  }

}

object Attribute extends AxisSpecifier

/**
  * Navigation via the node property with the given name
  *
  * @param propertyName name to navigate into
  */
case class NavigationAxis(propertyName: String)
  extends AxisSpecifier
