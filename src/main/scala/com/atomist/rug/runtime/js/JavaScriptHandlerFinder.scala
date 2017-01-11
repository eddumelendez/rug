package com.atomist.rug.runtime.js

import com.atomist.project.archive.{AtomistConfig, DefaultAtomistConfig}
import com.atomist.rug.runtime.js.interop.AtomistFacade
import com.atomist.source.ArtifactSource

/**
  * Finds and evaluates handlers in a Rug archive.
  */
object JavaScriptHandlerFinder {

  /**
    * Find and handlers operations in the given Rug archive
    *
    * @param rugAs   archive to look into
    * @param atomist facade to Atomist
    * @return a sequence of instantiated operations backed by JavaScript

    */
  def registerHandlers(rugAs: ArtifactSource,
                       atomist: AtomistFacade,
                       atomistConfig: AtomistConfig = DefaultAtomistConfig): Unit = {
    val jsc = new JavaScriptContext()

    //TODO - remove this when new Handler model put in
    jsc.engine.put("atomist", atomist)
    jsc.load(rugAs)
  }
}
