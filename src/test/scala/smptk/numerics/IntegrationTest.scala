package smptk.numerics
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import smptk.image.DiscreteImageDomain1D
import smptk.image.ContinuousScalarImage1D
import smptk.image.Geometry._
import smptk.image.Geometry.implicits._
import breeze.linalg.DenseVector
import smptk.image.DiscreteImageDomain2D
import smptk.image.Utils
import smptk.common.BoxedRegion1D

class IntegrationTest extends FunSpec with ShouldMatchers {

  describe("An integration in 1D") {
    it("Correctly integrates x squared on interval [-1,1]") {

      val img =  ContinuousScalarImage1D( (x: Point1D) => x >= 0 && x <= 1, (x: Point1D) => x * x, Some((x: Point1D) => DenseVector(2.) * x(0) ))  

      val domain = DiscreteImageDomain1D(-1, 0.002, 1000)
      val integrator = Integrator[CoordVector1D](IntegratorConfiguration(UniformSampler1D(), domain.numberOfPoints))  
    
      val res = integrator.integrateScalar(img, domain)
      res should be(1. / 3. plusOrMinus 0.001)
    }
    it("Correctly integrates sin(x) on interval [-Pi, Pi]") {

      val img =  ContinuousScalarImage1D( 
          (x: Point1D) => x >= -math.Pi && x <= math.Pi, 
          (x: Point1D) => math.sin(x.toDouble).toFloat, 
          Some((x: Point1D) => DenseVector( - math.cos(x.toDouble).toFloat ))
          )

      val domain = DiscreteImageDomain1D(-math.Pi, math.Pi.toFloat / 500f, 1000)
      val integrator = Integrator[CoordVector1D](IntegratorConfiguration(UniformSampler1D(), domain.numberOfPoints))  
        
      val res = integrator.integrateScalar(img, domain)
      res should be(0. plusOrMinus 0.001)

    }
    
    it("Correctly integrates integrates a compact function") {

      val img =  ContinuousScalarImage1D( ((x: Point1D) => x(0) > -1. && x(0) < 1.),  (x: Point1D) => 1.)

      Utils.show1D(img, DiscreteImageDomain1D(-2., 0.1, 40))
      
      val region1 = BoxedRegion1D(-1.01, 1.01) 
      val region2 = BoxedRegion1D(-8.01, 8.01)
      
      val integrator = Integrator[CoordVector1D](IntegratorConfiguration(UniformSampler1D(), 1000))  

      val res1 = integrator.integrateScalar(img, region1)
      val res2 = integrator.integrateScalar(img, region2)
      
      
      res1 should be(res2 plusOrMinus 0.001)

    }

   
  }

}