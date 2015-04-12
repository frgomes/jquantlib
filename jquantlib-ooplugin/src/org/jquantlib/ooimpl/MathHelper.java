/*
 Copyright (C) 2008 Praneet Tiwari

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.ooimpl;
import org.jquantlib.math.Factorial;
import org.jquantlib.math.PrimeNumbers;
import org.jquantlib.math.distributions.BinomialDistribution;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.jquantlib.math.distributions.CumulativePoissonDistribution;
import org.jquantlib.math.distributions.GammaDistribution;
import org.jquantlib.math.distributions.InverseCumulativeNormal;
import org.jquantlib.math.distributions.InverseCumulativePoisson;
import org.jquantlib.math.distributions.NonCentralChiSquaredDistribution;
import org.jquantlib.math.distributions.PoissonDistribution;

/**
 *
 * @author Praneet Tiwari
 */
public class MathHelper {

    public static double getFactorial(int n){

    Factorial f = new Factorial();
     /*return*/ f.get(n);
     return n;
    }

     public static double getlNFactorial(int n){
    Factorial f = new Factorial();
     return f.ln(n);
    }

     public static long getPrimeNumberAt(int n){
         PrimeNumbers pn = new PrimeNumbers();
         return pn.get(n);
     }


     public static double  evaluateBinomialDistributionValue(double probability, int k) {
         BinomialDistribution bs  = new BinomialDistribution(probability, 0);
         return bs.op(k);

     }

      public static  double evaluateCumulativeNormalDistribution(double mean, double sigma, double z){
          CumulativeNormalDistribution cnd = new CumulativeNormalDistribution(mean, sigma);
      return     cnd.op(z);
      }

      public  static double evaluateCumulativePoissonDistribution(double mean, int k){
          CumulativePoissonDistribution cpd = new CumulativePoissonDistribution(mean);
          return cpd.op(k);

      }

          public static double evaluateGammaDistribution(double a, double x) {
              GammaDistribution gd = new GammaDistribution(a);
              return gd.op(x);
          }
           public static double evaluateInverseCumulativeNormal(double average, double sigma, double x){
               InverseCumulativeNormal icn = new InverseCumulativeNormal(average,sigma);
               return icn.op(x);
           }



 public static double evaluateNonCentralChiSquaredDistribution(double df, double x, double ncp) {
     NonCentralChiSquaredDistribution ncd = new NonCentralChiSquaredDistribution(df,ncp);
     return ncd.op(x);

 }
    public static double evaluateInverseCumulativePoisson(double lambda, double x) {
        InverseCumulativePoisson icp = new InverseCumulativePoisson(lambda);
        return icp.op(x);

    }
    // fix this return type and argument type got mixed up
      public static double evaluatePoissonDistribution(double mu, double sigma){
          PoissonDistribution pd = new PoissonDistribution(mu);
          int k = (int) sigma;
          return pd.op(k);
      }
}