package com.atomist.param

import org.scalatest.{FlatSpec, Matchers}

class ParameterGeneratorTest extends FlatSpec with Matchers {

  import ParameterGenerator._
  import ParametersToTest._

  it should "generate value for string parameter" in testGenerationFor(StringParam)

  it should "generate value for numeric parameter" in testGenerationFor(AgeParam)

  it should "generate value for custom parameter" in testGenerationFor(ParamStartingWithX)

  it should "generate values for parameterized" in {
    val pvs = validParameterValuesFor(ParameterizedToTest)
    ParameterizedToTest.areValid(pvs) should be(true)
  }

  private def testGenerationFor(p: Parameter) {
    val generatedPv = validValueFor(p)
    p.isValidValue(generatedPv.getValue) should be(true)
  }
}
