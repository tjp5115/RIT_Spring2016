import java.math.BigInteger;

/**
 * Created by Tyler Paulsen on 3/9/2016.
 */
public class ARK4 implements BlockCipher {

    public static int c1 = 0x69; // x6 + x5 + x3 + 1
    public static int c2 = 0x87; // x7 + x2 + x + 1
    public static int c3 = 0x55; // x6 + x4 + x2 + 1
    private byte key[] = new byte[keySize()];
    /**
     * Returns this block cipher's block size in bytes.
     *
     * @return Block size.
     */
    @Override
    public int blockSize() {
        return 8;
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
        return 32;
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
    public void setKey(byte[] key) throws IllegalArgumentException{
        if ( key.length != keySize() ) throw new IllegalArgumentException("key not the right length.");
        this.key = key;
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
    public void encrypt(byte[] text) throws IllegalArgumentException{
        if ( text.length != blockSize()) throw new IllegalArgumentException("text.length != cipher.blockSize())");


        for (int i = 1; i < numRounds()+1; ++i){

            subkeyRound(i);

            /*
            System.out.print(i + "\t");
            for (int j = 0; j < text.length; ++ j)
                System.out.printf ("%02x", text[j]);
            System.out.print("\t");
            for (int j = 0; j < 4; ++ j)
                System.out.printf ("%02x", key[j]);
            System.out.print("\t");
*/
            round(text);
/*
            for (int j = 0; j < text.length; ++ j)
                System.out.printf ("%02x", text[j]);
            System.out.println();
  */
        }

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


    public int F(byte a, byte b){
     return times(a,b) ^ times(c1,a) ^ times(c2,b) ^ c3;
    }

    /**
     * Compute the GF(2^8) product of the given quantities using the irreducible
     * polynomial x^8 + x^4 + x^3 + x + 1.
     * source: Alan Kaminsky  --  https://cs.rit.edu/~ark/462/java2/java2html.php?file=14
     */
    private static int times
    (int a,
     int b)
    {
        int c = 0;
        for (int bit = 0x80; bit > 0; bit >>= 1)
        {
            c <<= 1;
            if ((c & 0x100) != 0) c ^= 0x12D;
            if ((b & bit) != 0) c ^= a;
        }
        return c;
    }
    private void round(byte[] text){
        byte D[] = new byte[8];
        text[0] = (byte)( F( text[4], key[0] ) ^ text[0]);
        text[1] = (byte)( F( text[5], key[1] ) ^ text[1]);
        text[2] = (byte)( F( text[6], key[2] ) ^ text[2]);
        text[3] = (byte)( F( text[7], key[3] ) ^ text[3]);
        System.arraycopy(text,0,D,0,8);
        D[0] = text[1];
        D[1] = text[3];
        D[2] = text[5];
        D[3] = text[7];
        D[4] = text[0];
        D[5] = text[2];
        D[6] = text[4];
        D[7] = text[6];
        System.arraycopy(D,0,text,0,8);
    }

    private void subkeyRound(int round){
        byte K[] = new byte[12];
        System.arraycopy(key,0,K,0,12);
        key[0] = K[8];
        key[1] = K[9];
        key[2] = K[10];
        key[3] = K[11];
        key[4] = K[0];
        key[5] = K[1];
        key[6] = K[2];
        key[7] = K[3];
        key[8] = K[4];
        key[9] = K[5];
        key[10] = K[6];
        key[11] = K[7];

        for (int i = 0; i < 4; ++i)
            key[i] = (byte)(F(K[i],(byte)(round + i)) ^ K[8+i]);
    }
}
