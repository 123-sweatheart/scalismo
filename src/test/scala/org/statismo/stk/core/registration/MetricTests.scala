package org.statismo.stk.core.registration

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.statismo.stk.core.image.{DifferentiableScalarImage, ScalarImage}
import org.statismo.stk.core.geometry._
import org.statismo.stk.core.geometry.Point.implicits._
import org.statismo.stk.core.numerics.Integrator
import org.statismo.stk.core.numerics.UniformSampler
import org.statismo.stk.core.common.BoxDomain

class MetricTests extends FunSpec with ShouldMatchers {

  describe("A mean squares metric (1D)") {
    it("returns 0 if provided twice the same image") {

      val domain = BoxDomain[_1D](0f, 1.0f)
      val img = DifferentiableScalarImage(BoxDomain[_1D](0.0f, 1.0f),
        (x: Point[_1D]) => x * x,
        (x: Point[_1D]) => Vector(2f) * x(0))
      val transSpace = TranslationSpace[_1D]
      val identityTransform = transSpace.transformForParameters(transSpace.identityTransformParameters)
      val integrator = Integrator(UniformSampler(domain, 1000))
      MeanSquaresMetric1D(integrator)(img, img, identityTransform) should be(0.0 plusOrMinus 0.001)
    }
  }
}