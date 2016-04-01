/**
 * Created by Crystal on 3/30/2016.
 */
import java.lang.Integer;
public class Speck implements BlockCipher{
    int m = 4;
    int [] key = new int[m];

    static int bit24_mask = 16777215;
    /**
     * Returns this block cipher's block size in bytes.
     *
     * @return Block size.
     */
    @Override
    public int blockSize() {
        return 6;
    }

    /**
     * Returns this block cipher's key size in bytes.
     *
     * @return Key size.
     */
    @Override
    public int keySize() {
        return 12;
    }

    /**
     * Returns the number of rounds in this block cipher.
     *
     * @return Number of rounds.
     */
    @Override
    public int numRounds() {
        return 23;
    }

    /**
     * Set the key for this block cipher. <TT>key</TT> must be an array of bytes
     * whose length is equal to <TT>keySize()</TT>.
     *
     * @param key Key.
     * @throws IllegalArgumentException (unchecked exception) Thrown if <TT>key.length</TT> &ne;
     *                                  <TT>keySize()</TT>.
     */
    @Override
    public void setKey(byte[] key) {
        int c = 0;
        for (int i = 0; i < keySize(); i+=3)
            this.key[c++] = pack24(key[i], key[i + 1], key[i + 2]);
    }

    /**
     * Encrypt the given plaintext. <TT>text</TT> must be an array of bytes
     * whose length is equal to <TT>blockSize()</TT>. On input, <TT>text</TT>
     * contains the plaintext block. The plaintext block is encrypted using the
     * key specified in the most recent call to <TT>setKey()</TT>. On output,
     * <TT>text</TT> contains the ciphertext block.
     *
     * @param text Plaintext (on input), ciphertext (on output).
     * @throws IllegalArgumentException (unchecked exception) Thrown if <TT>text.length</TT> &ne;
     *                                  <TT>blockSize()</TT>.
     */
    @Override
    public void encrypt(byte[] text) {
        int CT[] = new int[2];
        CT[0] = pack24(text[0], text[1], text[2]);
        CT[1] = pack24(text[3], text[4], text[5]);
        int K[] = new int[2];

        for(int i=0; i < numRounds(); ++i){
            System.out.printf("Key (%d):\t", i);
            printHex(key);
            K[0] = key[i&m-1];
            K[1] = key[i&1];
            round(K,i);
            key[i&m-1] = K[0];
            key[i&1] = K[1];
            round(CT,key[i&1]);
        }
        text[2] = (byte)(CT[0] & 255);
        text[1] = (byte)(CT[0] >> 8 & 255);
        text[0] = (byte)(CT[0] >> 16 & 255);

        text[5] = (byte)(CT[1] & 255);
        text[4] = (byte)(CT[1] >> 8 & 255);
        text[3] = (byte)(CT[1] >> 16 & 255);
        System.out.println("\n6e06a5acf156 = test vec");
    }

    public void round(int a[], int k){
        int x = a[1];
        int y = a[0];
        x=rotateRight(x, 8);
        x = (x + y) % 0xFFFFFF;
        x ^= k;
        y=rotateLeft(x, 3);
        y ^= x;
        a[0] = x;
        a[1] = y;
    }


    public void printHex(int[] A){
        for(int i = 0; i < A.length; ++i)
            System.out.printf("%02x ",A[i]);
        System.out.println();
    }
    public void printHex(byte[] A){
        for(int i = 0; i < A.length; ++i)
            System.out.printf("%02x ",A[i]);
        System.out.println();
    }
    /**
     * Decrypt the given ciphertext. <TT>text</TT> must be an array of bytes
     * whose length is equal to <TT>blockSize()</TT>. On input, <TT>text</TT>
     * contains the ciphertext block. The ciphertext block is decrypted using
     * the key specified in the most recent call to <TT>setKey()</TT>. On
     * output, <TT>text</TT> contains the plaintext block.
     *
     * @param text Ciphertext (on input), plaintext (on output).
     * @throws IllegalArgumentException (unchecked exception) Thrown if <TT>text.length</TT> &ne;
     *                                  <TT>blockSize()</TT>.
     */
    @Override
    public void decrypt(byte[] text) {

    }
    public void printBinary(int x){
        System.out.println(Integer.toBinaryString(x & 0xFFFFFF).replace(' ', '0'));
    }

    public int rotateRight(int x, int n){
        return ( (x >>> n) | (x << (23 - n)) );
    }

    public int rotateLeft(int x, int n){
        return ( (x << n) | (x >> (23 - n)) );
    }
    public int pack24(byte a, byte b, byte c){
        byte temp[] = {a,b,c};
        //printHex(temp);
        return a << 16 | b << 8 | c ;
    }

}
