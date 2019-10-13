import org.graalvm.compiler.hotspot.replacements.BigIntegerSubstitutions;

import java.math.BigInteger;
import java.util.Random;

public class RSA {
  private  Random r;
  private final BigInteger one;
  private  BigInteger p;
  private  BigInteger q;
  private  BigInteger n;
  private  BigInteger phi;
  private BigInteger e;
  private final BigInteger d;

  // gako publikoa (n,e)
  // gako pribatua d

   /*
    1. Bi zenbaki lehen (handi) aukeratu, p eta q
    2. n = p*q kalkulatu  (n = modulua)
    3. ϕ(n) = (p-1)(q-1)  (Totient -edo phi- funtzioa) kalkulatu
    4. e zenbaki bat aukeratu non 1 < e < ϕ(n) && gcd(e,ϕ(n)) = 1  (e = gako publik. berretzailea)
    5. d bat aukeratu non d*e mod ϕ(n) = 1       (d = gako pribatuaren berretzailea)
    Alegia, d*e ≡ 1 (mod ϕ(n))
    */


  public RSA() {

    r = new Random();
    one = new BigInteger("1");
    p = BigInteger.probablePrime(256, r);
    q = BigInteger.probablePrime(256, r);

    n = p.multiply(q); // n = modulua

    // (Totient funtzioa) kalkulatu
    phi = p.subtract(one).multiply( q.subtract(one));

    // System.out.println(phi);

    e = new BigInteger(phi.bitLength(), r);

    while (e.compareTo(one) <= 0 || !phi.gcd(e).equals(one) || e.compareTo(phi) >= 0) {
      e = new BigInteger(phi.bitLength() - 1, r);
    }

    // d*e mod phi =1   e eta phi jakinda, nola kalkulatu d?
    d = e.modInverse(phi);

    // check d*e mod phi = 1
    // System.out.println("Check: " + d.multiply(e).mod(phi));

    /*
    e = new BigInteger("1850567623300615966303954877");
    n = new BigInteger("4951760154835678088235319297");
    d = new BigInteger("4460824882019967172592779313");
     */


  }



  public static void main(String[] args) {

    RSA rsa = new RSA();
    String mezua = "HELLOWORLD";
    // HELLOWORLD => 72697676798779827668

    System.out.println(rsa.zifratu(mezua));
    System.out.println( rsa.deszifratu( rsa.zifratu(mezua)) );


  }

  private BigInteger zifratu(String mezua) {

    // k = c(m) = m^e mod n    (zifratu)
    String mezua2ascii = "";
    for(int i=0; i<mezua.length(); i++) mezua2ascii += "" + ((int)mezua.charAt(i));
    BigInteger message = new BigInteger(mezua2ascii);
    return message.modPow(e, n);
  }

  private BigInteger deszifratu( BigInteger ciphertext) {
    return ciphertext.modPow(d, n);
  }

}
