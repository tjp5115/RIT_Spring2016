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




    private class Guess_1{
        int sk_0;
        int sk_1;
        Guess_1(int sk_0,int sk_1){
            this.sk_0 = sk_0;
            this.sk_1 = sk_1;
        }
        public String toString(){
            return "";
        }

        boolean isViable(PTCT ptct){
            round(ptct.pt_xy, sk_0);
            round(ptct.pt_xy, sk_1);
            return ptct.pt_xy[1] == ptct.ct_y;
        }
    }

    private class Guess_2{
        public int sk_1;
        public int sk_2;

        public Guess_2(int sk_1, int sk_2, int ct){

        }

        public boolean isViable(int pt[], int ct[]){
            return false;
        }

    }

    public static void main(String args[]) throws IOException{
        CrackSpeck cs = new CrackSpeck();
        cs.run(args);
    }

    private void run(String args[]){

        DList<Guess_1> guessList = new DList();;
        // list of first round SK
        for(int sk_0 = 0; sk_0 < 16777216; ++sk_0 ){
            guessList.addLast(new Guess_1(sk_0, 0));
        }
        System.out.println("Added Guesses");
        //get the input to the CT
        PTCT ptct[] = new PTCT[args.length / 2];
        int c=0;
        int size = args.length / 2;
        for(int i = 0; i < size; i += 1){
            ptct[c++] = new PTCT(args[i], args[i+1]);
        }

        boolean unique = false;
        int ptct_count = 0;
        while(! unique && ptct_count < size) {
            unique = true;
            DListEntry<Guess_1> curr = guessList.first();
            while (curr != null) {
                DListEntry<Guess_1> succ = curr.succ();
                if (!curr.item().isViable(ptct[ ptct_count ]))
                    curr.remove();
                curr = succ;
            }
            if(guessList.size() != 1 && guessList.size() > 1)
                unique = false;
            System.out.println("Keys: " + guessList.size());
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
        int ct_y;
        int ct,pt;
        int pt_xy[] = new int[2];
        int ct_xy[] = new int[2];
        PTCT(String plaintext, String ciphertext){
            this.pt = Hex.toInt(plaintext);
            this.ct = Hex.toInt(ciphertext);
            byte []ct = Hex.toByteArray(ciphertext);
            byte []pt = Hex.toByteArray(plaintext);
            pt_xy[0] = pack24(pt[0], pt[1], pt[2]);
            pt_xy[1] = pack24(pt[3], pt[4], pt[5]);
            int ct_x;
            ct_x = pack24(ct[0], ct[1], ct[2]);
            ct_y = pack24(ct[3], ct[4], ct[5]);
            ct_xy[0] = ct_x;
            ct_xy[1] = ct_y;
            ct_y ^= ct_x;
            ct_y = rotateRight(ct_y, 3);
        }
        public String toString(){
            return Hex.toString(ct);
        }
    }
}
