/**
 * Created by Crystal on 3/30/2016.
 */

import edu.rit.util.DList;
import edu.rit.util.DListEntry;
import edu.rit.util.Hex;
import edu.rit.util.Packing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class CrackSpeck {
    class Guess{
        PTCT ptct;
        Guess(PTCT ptct){
            this.ptct = ptct;
        }

        public void newGuess(int sk_0){
            ptct.reset();
            round(ptct.pt24,sk_0);
            round2CT();
            isViable();
        }
        private void round2CT(){
            int y = ptct.pt24[1];
            y = rotateLeft(y, 3);
            ptct.ct2[0] = y ^ ptct.ct2[1];
        }

    }
    public static void main(String args[]) throws IOException{
        CrackSpeck cs = new CrackSpeck();
        cs.run(args);
    }

    public void run(String []args){
        PTCT ptct[] = new PTCT[args.length / 2];
        int c=0;
        int size = args.length / 2;
        byte[] pt,ct;
        for(int i = 0; i < size; i += 1){
            try {
                pt = Hex.toByteArray(args[i]);
                ct = Hex.toByteArray(args[i + 1]);
                ptct[c++] = new PTCT(pt,ct);
            }catch(IllegalArgumentException iae){
                useage();
            }
        }


        Guess guess  = new Guess(ptct[0]);
        for(int sk_0 = 0; sk_0 < 16777216; ++sk_0 ){
            guess.newGuess(sk_0);
        }
    }
   private void round(int a[], int k){
        //System.out.printf("key = %02x%n",k);
        a[0] = rotateRight(a[0], 8);
        a[0] = (a[0] + a[1]) & 0xFFFFFF;
        //System.out.println("round X = " + Hex.toString(a[0]));
        a[0] ^= k;
        a[1] = rotateLeft(a[1], 3);
        //System.out.println("round Y = " + Hex.toString(a[1]));
        a[1] ^= a[0];
        //System.out.println("round: " + Hex.toString(a[0]) + Hex.toString(a[1]));
    }

    private void round_inv(int a[], int k){
        a[1] ^= a[0];
        a[1] = rotateRight(a[1], 3);
        a[0] ^= k;
        a[0] = (a[0] - a[1]) & 0xFFFFFF;
        a[0] = rotateLeft(a[0], 8);
    }

    private int rotateRight(int x, int n){
        return ( (x >>> n) | (x << (24 - n)) ) & 0xFFFFFF;
    }

    private int rotateLeft(int x, int n){
        return ( (x << n) | (x >> (24 - n)) ) & 0xFFFFFF;
    }

    private int pack24(byte a, byte b, byte c){
        int x = a & 0xFF;
        x <<= 8;
        x |= b  & 0xFF ;
        x <<= 8;
        x |= c  & 0xFF ;
        return x;
    }


    class PTCT{
        int ct2[];
        byte []ct,pt;
        public int[] pt24;
        private int[]pt24_save;
        PTCT(byte[] plaintext, byte[] ciphertext){
            pt = plaintext;
            pt24_save = new int[2];
            pt24 = new int[2];
            ct2 = new int[2];
            pt24_save[0] = pt24[0] = pack24(pt[0], pt[1], pt[2]);
            pt24_save[1] = pt24[1] = pack24(pt[3], pt[4], pt[5]);

            ct = ciphertext;
            int ct_x = pack24(ct[0], ct[1], ct[2]);
            int ct_y = pack24(ct[3], ct[4], ct[5]);
            ct2[1] ^= ct_x;
            ct2[1] = rotateRight(ct_y, 3);
        }
        public void reset(){
            pt24[0] = pt24_save[0];
            pt24[1] = pt24_save[1];
        }
        public String toString(){
            return Hex.toString(ct);
        }
    }
    public static void useage(){
        System.err.println("java CrackSpeck <pt1> <ct1> [<pt2> <ct2> ...]\n" +
                "<pt1> is a known plaintext. It must be a 12-digit hexadecimal number (uppercase or lowercase).\n" +
                "<ct1> is the ciphertext corresponding to <pt1>." +
                " It must be a 12-digit hexadecimal number (uppercase or lowercase).");
    }
}
