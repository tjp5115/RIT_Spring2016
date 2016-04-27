import java.math.BigInteger;

/**
 * Created by Tyler on 4/20/2016.
 * Cracks an RSA private key from the given RSA public key (Exponent e and public modulus n).
 */
public class CrackRsa {
    public static void main(String args[]){
        if (args.length != 2) usage();
        try {
            BigInteger e = new BigInteger(args[0]);
            BigInteger n = new BigInteger(args[1]);
            if(n.bitCount() > 80) usage();
            CrackRsa crackRsa = new CrackRsa();
            crackRsa.runCrack(e, n);
        }catch (NumberFormatException nfe){
            usage();
        }
    }

    /**
     * Runs the crack RSA using the Pollard’s rho algorithm for factoring integers.
     * @param e - is the exponent of an RSA public key
     * @param n - is the modulus of an RSA public key
     */
    private void runCrack(BigInteger e, BigInteger n){
        BigInteger a = new BigInteger("2");
        BigInteger b = new BigInteger("2");
        BigInteger d;
        boolean success = false;
        for(;;){
            a = a.pow(2).add(BigInteger.ONE).mod(n);
            b = b.pow(2).add(BigInteger.ONE).mod(n);
            b = b.pow(2).add(BigInteger.ONE).mod(n);
            //System.out.print(a+"\t");
            //System.out.println(b);
            d = a.subtract(b).gcd(n);
            if( d.compareTo(BigInteger.ONE) == 1 && d.compareTo(n) == -1){
                success = true;
                break;
            }
            if(d.compareTo(n) == 0) break;
        }

        if(!success) {
            System.out.println("Failed to find key");
            System.exit(1);
        }

        BigInteger q = n.divide(d);
        if(d.compareTo(q) == -1 ) {
            System.out.println(d);
            System.out.println(q);
        }else{
            System.out.println(q);
            System.out.println(d);
        }
        System.out.println(e.modInverse(d.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))));
    }

    //usage.
    private static void usage(){
        System.err.println("Usage:  java CrackRsa <e> <n>\n" +
                "<e> is the exponent of an RSA public key. It must be a decimal number.\n" +
                "<n> is the modulus of an RSA public key. It must be a decimal number less than 81 bits.");
        System.exit(1);
    }
}
