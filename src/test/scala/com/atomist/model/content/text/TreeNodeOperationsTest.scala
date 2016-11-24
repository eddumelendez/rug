package com.atomist.model.content.text

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by rod on 11/1/16.
  */
class TreeNodeOperationsTest extends FlatSpec with Matchers {

  import TreeNodeOperations._

  it should "remove padding" in {
    val input = "The quick brown fox"
    val f1 = new MutableTerminalTreeNode("f1", "The", OffsetInputPosition(0))
    val f2 = new MutableTerminalTreeNode("f2", "quick", OffsetInputPosition(4))
    val mc = SimpleMutableContainerTreeNode.wholeInput("name", Seq(f1, f2), input)
    println(mc)
    mc.childNodes.exists(cn => cn.isInstanceOf[PaddingNode]) should be (true)
    val transformed = RemovePadding(mc)
    transformed.childNodes.exists(cn => cn.isInstanceOf[PaddingNode]) should be (false)
  }

  it should "remove empty container nodes" in {
    val input = "The quick brown fox"
    val f1 = new MutableTerminalTreeNode("f1", "The", OffsetInputPosition(0))
    val f2 = new MutableTerminalTreeNode("f2", "quick", OffsetInputPosition(4))
    val empty = new SimpleMutableContainerTreeNode("empty", Seq(f2), f2.startPosition, f2.endPosition)

    val mc = SimpleMutableContainerTreeNode.wholeInput("name", Seq(f1, empty), input)
    println(mc)
    mc.childNodes.exists(cn => cn.nodeName.equals("empty")) should be (true)
    val transformed = Flatten(mc)
    //println("transformed=" + transformed)
    transformed.childNodes.exists(cn => cn.nodeName.equals("empty")) should be (false)
  }

  it should "collapse selected nodes" in {
    val input = "The quick brown fox"
    val f1 = new MutableTerminalTreeNode("f1", "The", OffsetInputPosition(0))
    val f2 = new MutableTerminalTreeNode("f2", "quick", OffsetInputPosition(4))
    val empty = new SimpleMutableContainerTreeNode("empty", Seq(f2), f2.startPosition, f2.endPosition)

    val mc = SimpleMutableContainerTreeNode.wholeInput("name", Seq(f1, empty), input)
    println(mc)
    mc.childNodes.exists(cn => cn.nodeName.equals("empty")) should be (true)
    val transformed = (collapse("empty"))(mc)
    //println("transformed=" + transformed)
    transformed.childNodes.exists(cn => cn.nodeName.equals("empty")) should be (false)
  }

  it should "update from mapped" in {
    val input = "The quick brown fox"
    val f1 = new MutableTerminalTreeNode("f1", "The", OffsetInputPosition(0))
    val f2 = new MutableTerminalTreeNode("f2", "quick", OffsetInputPosition(4))
    val empty = new SimpleMutableContainerTreeNode("empty", Seq(f2), f2.startPosition, f2.endPosition)

    val mc = SimpleMutableContainerTreeNode.wholeInput("name", Seq(f1, empty), input)
    println(mc)
    mc.childNodes.exists(cn => cn.nodeName.equals("empty")) should be (true)
    val transformed = Flatten(mc)
    transformed.value should equal (input)
    val f2f = TreeNodeFinders.findByName(f2.nodeName, transformed).head
    f2f should be(f2)
    f2f.asInstanceOf[MutableTerminalTreeNode].update("ballistic")
    transformed.value should equal (input.replace("quick", "ballistic"))
  }

}
