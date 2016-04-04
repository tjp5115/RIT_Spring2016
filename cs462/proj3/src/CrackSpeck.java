/**
 * Created by Crystal on 3/30/2016.
 */
import edu.rit.util.Hex;
import edu.rit.io.OutStream;
import edu.rit.util.Packing;
import java.io.IOException;

public class CrackSpeck {
    private static int mask_guess[] = {
            0xFFFFF0,
            0xFFFF0F,
            0xFFF0FF,
            0xFF0FFF,
            0xF0FFFF,
            0x0FFFFF
    };
    private static int mask_key[] = {
            0x00000F,
            0x0000F0,
            0x000F00,
            0x00F000,
            0x0F0000,
            0xF00000,
    };
    public static void main(String args[]) throws IOException{
                /*
9c2b81   4970ee   7534f3   27a6cac092c4   a031b582b774
fa048e   6ef8be   8669d7   bf8b2b0c6acf   f17614694e1c
725686   fcfaa9   e0acdd   b666660e1c03   1de32748506e
5af17d   543383   5ae1e5   a9e47fac33b5   41dd2a6500db
         */
        int rounds = 3;
        int mask = 3;
        System.out.println("Test 1");
        String key[] = {"9c2b81", "4970ee", "7534f3"};
        test("27a6cac092c4", key, mask, rounds);

        System.out.println("Test 2");
        String key1[]= {"fa048e", "6ef8be", "8669d7"};
        test("bf8b2b0c6acf", key1, mask, rounds);

        System.out.println("Test 3");
        String key3[] = {"725686", "fcfaa9", "e0acdd"};
        test("b666660e1c03", key3, mask, rounds);

        System.out.println("Test 4");
        String key2[] = {"5af17d", "543383", "5ae1e5"};
        test("a9e47fac33b5", key2, mask, rounds);

    }
    public static void test(String pt, String key[], int mask_num, int num_rounds){
        byte text[] = Hex.toByteArray(pt);
        int CT1[] = new int[2];
        int CT2[] = new int[2];
        CT1[0] = pack24(text[0],text[1],text[2]);
        CT1[1] = pack24(text[3],text[4],text[5]);
        CT2[0] = pack24(text[0],text[1],text[2]);
        CT2[1] = pack24(text[3],text[4],text[5]);

        int k1;
        System.out.println("PT: " + pt);
        for(int i = 0; i < num_rounds;++i){
            k1 = Hex.toInt(key[i]) & mask_key[mask_num] | (CT1[0] & mask_guess[mask_num]);
            round(CT1, k1);
            round(CT2, Hex.toInt(key[i]));
            //System.out.printf("Key("+i+"):\t%02x%n", k1);
            System.out.printf("%02x %02x%n", CT1[0], CT1[1]);
            System.out.printf("%02x %02x%n%n", CT2[0], CT2[1]);
        }

    }

    private static void round(int a[], int k){
        a[0] = rotateRight(a[0], 8);
        a[0] = (a[0] + a[1]) & 0xFFFFFF;
        a[0] ^= k;
        a[1] = rotateLeft(a[1], 3);
        a[1] ^= a[0];
    }

    private static void round_inv(int a[], int k){
        a[1] ^= a[0];
        a[1] = rotateRight(a[1], 3);
        a[0] ^= k;
        a[0] = (a[0] - a[1]) & 0xFFFFFF;
        a[0] = rotateLeft(a[0], 8);
    }

    public static int rotateRight(int x, int n){
        return ( (x >>> n) | (x << (24 - n)) ) & 0xFFFFFF;
    }

    public static int rotateLeft(int x, int n){
        return ( (x << n) | (x >> (24 - n)) ) & 0xFFFFFF;
    }

    public static int pack24(byte a, byte b, byte c){
        int x = a & 0xFF;
        x <<= 8;
        x |= b  & 0xFF ;
        x <<= 8;
        x |= c  & 0xFF ;
        return x;
    }
}
