/**
 * Created by Crystal on 3/30/2016.
 */
import edu.rit.util.Hex;
import edu.rit.util.Packing;
import java.io.IOException;
import java.util.*;

public class CrackSpeck_bad {

    private HashSet<Guess> map[];
    public static void main(String args[]) throws IOException{
        CrackSpeck_bad cs = new CrackSpeck_bad();
        cs.run(args);
    }

    private void run(String args[]){
        PTCT ptct[] = new PTCT[args.length / 2];
        int c=0;
        int size = args.length / 2;
        int count = 0;
        for(int i = 0; i < 16777216; ++i ){count += 1;}
        System.out.println("Done " + count);
        //int size = 1;
        for(int i = 0; i < size; i += 1){
            ptct[c++] = new PTCT(args[i], args[i+1]);
        }

        map = new HashSet[16];
        for(byte i = 0; i < 16; ++i)
            map[i] = new HashSet();

        for(byte i = 0; i < 16; i++) {
            for (byte k = 0; k < 16; k++){
                map[i^k].add(new Guess(i,k));
            }
        }
        /*
        for(byte i = 0; i < map.length; i++) {
            for(Guess g: map[i])
                System.out.println(g);
            System.out.println();
        }
        */
        round(ptct[0].pt_int, Hex.toInt("0fb43f"));
        HashSet<Byte> guess1 = findCTByte(ptct[0].pt_x[1], ptct[0].pt_y[1]);
        System.out.println("Number of Keys: " + guess1.size());
        for(Byte b: guess1)
            System.out.printf("%02x ",b);
        System.out.println();
        HashSet<Byte> guess2;
        for(int i = 1; i < ptct.length; ++i){
            guess2 = findCTByte(ptct[i].pt_x[1], ptct[i].pt_x[1]);
            guess1.retainAll(guess2);
            System.out.println("Number of Keys: " + guess1.size());
            for(Byte b: guess1)
                System.out.printf("%02x ",b);
            System.out.println();
        }
    }

    private HashSet<Byte> findCTByte(byte pt_x, byte pt_y){
       // System.out.printf("findCTByte:%nx = %02x y = %02x%n",pt_x, pt_y);
        ArrayList<Guess> ctX,ctY ;
        HashSet<Byte> out = new HashSet();

        ctX = getXOR(pt_x);
        ctY = getXOR(pt_y);

        for(Guess guess1: ctY) {
            for (Guess guess2: ctX) {
                if(guess1.a == guess2.a){
                    out.add(guess2.b); break;
                }else if(guess1.a == guess2.b){
                    out.add(guess2.a); break;
                }else if(guess1.b == guess2.b){
                    out.add(guess2.a); break;
                }else if(guess1.b == guess2.a){
                    out.add(guess2.b); break;
                }
            }
        }

        /*
        for(Byte b: out)
            System.out.printf("%02x ", b);
        System.out.println("\n" + out.size());
        */
        return out;
    }

    public ArrayList<Guess> getXOR(byte b){
        ArrayList<Guess> xor = new ArrayList();
        //System.out.printf("%02x , %02x %n",(b >> 4) & 0x0F, b & 0x0f );
        for(Guess g1: map[(b >> 4) & 0x0F])
            for(Guess g2: map[b & 0x0F]) {
                xor.add(new Guess((byte) ((g1.a << 4) | (g2.a)), (byte) ((g1.b << 4) | (g2.b))));
            }
        System.out.println(xor.size());
        System.out.println(xor);
        return xor;
    }

    private void findCTByte(byte pt_x, byte pt_y, HashSet<Byte> k){
        for (Iterator<Byte> iterator = k.iterator(); iterator.hasNext();) {
            byte guess = iterator.next();
            //System.out.printf("%02x^%02x = %02x%n",guess.a,guess.b,guess.a^guess.b);
        }
    }

    private void round(int a[], int k){
        System.out.printf("key = %02x%n",k);
        a[0] = rotateRight(a[0], 8);
        a[0] = (a[0] + a[1]) & 0xFFFFFF;
        System.out.println("round X = " + Hex.toString(a[0]));
        a[0] ^= k;
        a[1] = rotateLeft(a[1], 3);
        System.out.println("round Y = " + Hex.toString(a[1]));
        a[1] ^= a[0];
        System.out.println("round: " + Hex.toString(a[0]) + Hex.toString(a[1]));
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


    class Guess{
        byte a,b;
        HashSet<Byte> set;
        Guess(byte a, byte b){
            if(a < b) {
                this.a = a;
                this.b = b;
            }else {
                this.a = b;
                this.b = a;
            }
            set = new HashSet();
            set.add(a);
            set.add(b);
        }

        public int hashCode() {
            return set.hashCode();
        }

        @Override
        public boolean equals(Object o){
            Guess g = (Guess)o;
            return set.size() == g.set.size() && g.set.containsAll(set);
        }
        public String toString(){
            return Hex.toString(a) + " " + Hex.toString(b) ;
        }

    }

    class PTCT{
        String plaintext,ciphertext;
        byte[] pt,ct;
        byte pt_y[] = new byte[4];
        byte pt_x[] = new byte[4];
        int pt_int[] = new int[2];
        PTCT(String plaintext, String ciphertext){
            this.plaintext = plaintext;
            this.ciphertext = ciphertext;
            pt = Hex.toByteArray(plaintext);
            ct = Hex.toByteArray(ciphertext);
            setXY(pt);
            //System.out.printf("%02x %02x",pt_x[1],pt_y[1]);
        }

        public void setXY(byte[] pt){
            int pt_x,pt_y;
            pt_x =pack24(pt[0], pt[1], pt[2]);
            pt_y = pack24(pt[3], pt[4], pt[5]);
            pt_int[0] = pt_x;
            pt_int[1] = pt_y;
            pt_x = rotateRight(pt_x,8);
            pt_x = (pt_x + pt_y) & 0xFFFFFF;
            pt_y = rotateLeft(pt_y,3);

            Packing.unpackIntBigEndian(pt_x, this.pt_x, 0);
            Packing.unpackIntBigEndian(pt_y, this.pt_y, 0);

            //System.out.println("x = " + Hex.toString(this.pt_x) + " y = " + Hex.toString(this.pt_y));
        }

        public String toString(){
            return plaintext + " " + ciphertext;
        }
    }
}
