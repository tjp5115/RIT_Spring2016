import edu.rit.util.Hex;
import edu.rit.util.Packing;
import java.io.IOException;
import java.util.*;

/**
 * Created by Tyler on 3/30/2016.
 * A structural attack on the block cipher Speck that is reduced to three rounds.
 */
public class CrackSpeck {

    /**
     * class used to store the secrete subkeys.
     */
    class Key{
        //keys
        int sk_0;
        int sk_1;
        int sk_2;

        /**
         * return a string representing a key
         * @return string representation of a key.
         */
        public String toString(){
            byte key [] = new byte[4];
            String out = new String();

            Packing.unpackIntBigEndian(sk_0,key,0);
            out += Hex.toString(key,1,3) + "\n";

            Packing.unpackIntBigEndian(sk_1,key,0);
            out += Hex.toString(key,1,3) + "\n";

            Packing.unpackIntBigEndian(sk_2, key, 0);
            out += Hex.toString(key,1,3);

            return out;
        }
    }

    //program entry point
    public static void main(String args[]) throws IOException{
        CrackSpeck cs = new CrackSpeck();
        cs.run(args);
    }

    /**
     * run the CrackSpeck program
     * @param args - plaintext ciphertext pairs.
     */
    public void run(String []args){
        final long startTime = System.currentTimeMillis();
        if(args.length < 2 || args.length % 2 != 0) usage();
        ArrayList<PTCT> p = new ArrayList<>();
        byte[] pt,ct;
        //create PT/CT combinations
        for(int i = 0; i < args.length ; i += 2){
            try {
                pt = Hex.toByteArray(args[i]);
                ct = Hex.toByteArray(args[i + 1]);
                if(ct.length != 6 || pt.length != 6) throw new IllegalArgumentException();
                p.add(new PTCT(pt,ct));
            }catch(IllegalArgumentException iae){
                usage();
            }
        }
        //create the inital viable key combination based off the first PT/CT
        LinkedList<Key> keys  = new LinkedList<>();
        for(int sk_0 = 0; sk_0 < 16777216; ++sk_0 ){
            PTCT curr = p.get(0);
            Key key = firstGuess(sk_0,curr);
            boolean isViable = true;
            for(int i = 1; i < p.size() && isViable; i++){
                curr = p.get(i);
                isViable = isViable(key, curr);
                curr.reset();
            }

            if(isViable){
                keys.add(key);
            }
        }

        //output the results
        if(keys.size() == 0){
            System.out.println("No subkeys found");
        }else if(keys.size() > 1){
            System.out.println("Multiple subkeys found");
        }else {
            System.out.println(keys.get(0));
        }
    }
    /**
     * takes the key, and checks to see if it is viable with the given PT/CT.
     * Viability is based off if the CT is the same after 3 rounds using the given keys.
     * @param k
     * @return
     */
    public boolean isViable(Key k, PTCT ptct){
        round(ptct.pt, k.sk_0);
        round(ptct.pt, k.sk_1);
        round(ptct.pt, k.sk_2);
        return ptct.pt[0] == ptct.ct_orig[0] && ptct.pt[1] == ptct.ct_orig[1];
    }

    /**
     * guess the first set of keys for a given plaintext
     * @param sk_0 - key
     * @param ptct - plaintext
     * @return
     */
    public Key firstGuess(int sk_0, PTCT ptct){
        Key key = new Key();
        key.sk_0 = sk_0;
        round(ptct.pt, sk_0);
        round2CT(ptct);
        getKeys(key, ptct);
        ptct.reset();
        return key;
    }

    /**
     * sets the keys using the structure of a given round.
     * @param k - current keys.
     * @param ptct - plaintext
     */
    public void getKeys(Key k, PTCT ptct) {
        k.sk_1 = ptct.ct2[0] ^ ((rotateRight(ptct.pt[0], 8) + ptct.pt[1]) & 0xFFFFFF);
        k.sk_2 = ptct.ct_orig[0] ^ ((rotateRight(ptct.ct2[0], 8) + ptct.ct2[1]) & 0xFFFFFF);
    }

    /**
     * finds the round 2 Cipher Text
     * @param ptct - plaintext
     */
    private void round2CT(PTCT ptct) {
        int y = ptct.pt[1];
        y = rotateLeft(y, 3);
        ptct.ct2[0] = y ^ ptct.ct2[1];

    }

    /**
     * one round of the speck function
     * @param a PT to encrypt
     * @param k key to use
     */
    private void round(int a[], int k){
        a[0] = rotateRight(a[0], 8);
        a[0] = (a[0] + a[1]) & 0xFFFFFF;
        a[0] ^= k;
        a[1] = rotateLeft(a[1], 3);
        a[1] ^= a[0];
    }

    /**
     * circulary rotates a given int to the right n bits
     * @param x - the int to rotate
     * @param n - number of bit to rotate
     * @return the rotated int
     */
    private int rotateRight(int x, int n){
        return ( (x >>> n) | (x << (24 - n)) ) & 0xFFFFFF;
    }
    /**
     * circulary rotates a given int to the left n bits
     * @param x - the int to rotate
     * @param n - number of bit to rotate
     * @return the rotated int
     */
    private int rotateLeft(int x, int n){
        return ( (x << n) | (x >> (24 - n)) ) & 0xFFFFFF;
    }

    /**
     * returns a big endian packed 24 bit number from the given 3 bytes
     * @param a first byte
     * @param b second byte
     * @param c third byte
     * @return the packed 24 bits into an int.
     */
    private int pack24(byte a, byte b, byte c){
        int x = a & 0xFF;
        x <<= 8;
        x |= b  & 0xFF ;
        x <<= 8;
        x |= c  & 0xFF ;
        return x;
    }


    /**
     * class used to represent the PT/CT pair
     */
    class PTCT{
        //various ways of representing the PT/CT
        int ct2[];
        public int[] pt;
        private int[]pt_orig;
        private int[]ct_orig;
        //constructor
        PTCT(byte[] p, byte[] c){
            pt_orig = new int[2];
            ct_orig = new int[2];
            pt = new int[2];
            pt_orig[0] = pt[0] = pack24(p[0], p[1], p[2]);
            pt_orig[1] = pt[1] = pack24(p[3], p[4], p[5]);

            int ct_x = pack24(c[0], c[1], c[2]);
            int ct_y = pack24(c[3], c[4], c[5]);
            ct_orig[0] = ct_x;
            ct_orig[1] = ct_y;

            ct2 = new int[2];
            ct2[1] = ct_y ^ ct_x;
            ct2[1] = rotateRight(ct2[1] , 3);
        }

        /**
         * resets the pt to the original
         */
        public void reset(){
            pt[0] = pt_orig[0];
            pt[1] = pt_orig[1];
        }
    }
    //usage statement.
    public static void usage(){
        System.err.println("java CrackSpeck <pt1> <ct1> [<pt2> <ct2> ...]\n" +
                "<pt1> is a known plaintext. It must be a 12-digit hexadecimal number (uppercase or lowercase).\n" +
                "<ct1> is the ciphertext corresponding to <pt1>." +
                " It must be a 12-digit hexadecimal number (uppercase or lowercase).");
        System.exit(1);
    }
}
