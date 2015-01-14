package org.statismo.stk.core.numerics

import scala.language.implicitConversions
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.statismo.stk.core.image.{DifferentiableScalarImage, DiscreteImageDomain, ScalarImage}
import org.statismo.stk.core.geometry._
import org.statismo.stk.core.geometry.Point.implicits._
import org.statismo.stk.core.common.BoxDomain


class IntegrationTest extends FunSpec with ShouldMatchers {

  implicit def doubleToFloat(d: Double) = d.toFloat

  describe("An integration in 1D") {
    it("Correctly integrates x squared on interval [-1,1]") {

      val domain = BoxDomain[_1D](0f, 1.0f)
      val img = DifferentiableScalarImage(domain, (x: Point[_1D]) => x * x, (x: Point[_1D]) => Vector(2f) * x(0))

      val grid = DiscreteImageDomain(domain.origin, domain.extent * (1.0 / 255.0), Index(255))
      val integrator = Integrator[_1D](GridSampler(grid))

      val res = integrator.integrateScalar(img)
      res should be((1.0 / 3.0).toFloat plusOrMinus 0.01)
    }

    it("Correctly integrates sin(x) on interval [-Pi, Pi]") {

      val img = DifferentiableScalarImage(
        BoxDomain[_1D](-math.Pi.toFloat, math.Pi.toFloat),
        (x: Point[_1D]) => math.sin(x.toDouble).toFloat,
        (x: Point[_1D]) => Vector(-math.cos(x.toDouble).toFloat)
      )

      val numPoints = 1000
      val grid = DiscreteImageDomain(Point(-math.Pi.toFloat), Vector(2 * math.Pi.toFloat / numPoints), Index(numPoints))
      val integrator = Integrator(GridSampler(grid))

      val res = integrator.integrateScalar(img)
      res should be(0.0f plusOrMinus 0.01)

    }

    it("Correctly integrates a compact function") {

      val img = ScalarImage(BoxDomain[_1D](-1.0f, 1.0f), (x: Point[_1D]) => 1.0)

      val region1 = BoxDomain[_1D](-1.0f, 1.0f)
      val region2 = BoxDomain[_1D](-8.0f, 8.0f)

      val numPoints = 200
      val grid1 = DiscreteImageDomain(Point(-1.0), Vector(2.0 / numPoints), Index(numPoints))
      val grid2 = DiscreteImageDomain(Point(-8.0), Vector(16.0 / numPoints), Index(numPoints))
      val integrator1 = Integrator(GridSampler(grid1))
      val integrator2 = Integrator(GridSampler(grid2))
      val res1 = integrator1.integrateScalar(img)
      val res2 = integrator2.integrateScalar(img)


      res1 should be(res2 plusOrMinus 0.01)

    }


  }

}