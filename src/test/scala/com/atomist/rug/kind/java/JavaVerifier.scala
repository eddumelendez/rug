package com.atomist.rug.kind.java

import com.atomist.source.ArtifactSource
import com.github.javaparser.JavaParser
import org.scalatest.Matchers
import JavaClassType._

/**
  * Utilities for use in testing
  */
object JavaVerifier extends Matchers {

  /**
    * Verify that the contents of this artifact source are still well formed
    *
    * @param result
    */
  def verifyJavaIsWellFormed(result: ArtifactSource): Unit = {
    for {
      f <- result.allFiles
      if f.name.endsWith(JavaExtension)
    } {
      try {
        //println(s"Parsing ${f.path}")
        JavaParser.parse(f.inputStream)
      }
      catch {
        case t: Throwable => fail(s"File ${f.path} is ill-formed\n${f.content}", t)
      }
    }
  }
}
