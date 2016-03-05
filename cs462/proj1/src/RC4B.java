import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.DatatypeConverter.*;

/**
 * @author  Tyler Paulsen
 * @author  Alan Kaminsky   -  used the RC4.java implementation as a template.
 *                              URL: https://cs.rit.edu/~ark/462/java2/RC4.java
 * @version 21-Feb-2016
 */
public class RC4B implements StreamCipher{
    private byte[] S = new byte [256];
    private byte[] K = new byte [256];
    private int j,i;
    /**
     * Returns this stream cipher's key size in bytes. If the stream cipher
     * includes both a key and a nonce, <TT>keySize()</TT> returns the size of
     * the key plus the size of the nonce in bytes.
     *
     * @return Key size.
     */
    @Override
    public int keySize() {
        return 32;
    }

    /**
     * Set the key for this stream cipher. <TT>key</TT> must be an array of
     * bytes whose length is equal to <TT>keySize()</TT>. If the stream cipher
     * includes both a key and a nonce, <TT>key</TT> contains the bytes of the
     * key followed by the bytes of the nonce. The keystream generator is
     * initialized, such that successive calls to <TT>encrypt()</TT> or
     * <TT>decrypt()</TT> will encrypt or decrypt a series of bytes.
     *
     * @param key Key.
     */
    @Override
    public void setKey(byte[] key) throws IllegalArgumentException{
        if(key.length != keySize()) throw new IllegalArgumentException("RC4B.setKey(key):  key.length != RC4B.keySize() ");
        for (int i = 0; i <= 255; ++i)
            S[i] = (byte)i ;
        j = 0;
        for (int i = 0; i <= 255; ++i) {
            j = (j + S[i] + key[i & 15]) & 255;
            swap(i, j);
        }
        int t = S[0] & 255;
        for( int i = 1; i <= 255; ++i){
            K[t] = S[i];
            t = S[i] & 255;
        }
        K[t] = S[0];
        int jj = S[j] & 255;
        for( int i = 0; i <=255; ++i){
            jj = K[jj] & 255;
            j = (j + S[jj] + key[(i & 15)+16]) & 255;
            swap(jj,j);
        }
        i = S[j];
        /*
        //testing output.
        System.out.println("S =");
        char s[]= DatatypeConverter.printHexBinary(S).toCharArray();
        //System.out.println(s);
        for (int i = 0; i < 16; ++i){
            for (int k = 0; k < 32; k+=2)
                System.out.print(s[i*32+k] + "" + s[i*32+k+1] +" ");
            System.out.println();
        }
        s = DatatypeConverter.printHexBinary(K).toCharArray();
        System.out.println("K = \n");
        for (int i = 0; i < 16; ++i){
            for (int k = 0; k < 32; k+=2)
                System.out.print(s[i*32+k] + "" + s[i*32+k+1] +" ");
            System.out.println();
        }
        System.out.printf("i = %d\nj = %d\n", this.i & 255, this.j & 255);
        */
    }

    /**
     * Encrypt the given byte. Only the least significant 8 bits of <TT>b</TT>
     * are used. The ciphertext byte is returned as a value from 0 to 255.
     *
     * @param b Plaintext byte.
     * @return Ciphertext byte.
     */
    @Override
    public int encrypt(int b) {
        return encryptOrDecrypt(b);
    }

    /**
     * Decrypt the given byte. Only the least significant 8 bits of <TT>b</TT>
     * are used. The plaintext byte is returned as a value from 0 to 255.
     *
     * @param b Ciphertext byte.
     * @return Plaintext byte.
     */
    @Override
    public int decrypt(int b) {
        return encryptOrDecrypt(b);
    }


    /**
     * Encrypt or decrypt the given byte. Only the least significant 8 bits of
     * <TT>b</TT> are used. If <TT>b</TT> is a plaintext byte, the ciphertext
     * byte is returned as a value from 0 to 255. If <TT>b</TT> is a ciphertext
     * byte, the plaintext byte is returned as a value from 0 to 255.
     *
     * @param  b  Plaintext byte (if encrypting), ciphertext byte (if
     *            decrypting).
     *
     * @return  Ciphertext byte (if encrypting), plaintext byte (if decrypting).
     */
    private int encryptOrDecrypt(int b) {
        i = K[i & 255] & 255;
        j = (j + S[i]) & 255;
        swap (i, j);
        return (S[(S[i] + S[j]) & 255] ^ b) & 255;
    }


    /**
     * Swap S[i] with S[j].
     */
    private void swap (int i, int j) {
        byte t = S[i];
        S[i] = S[j];
        S[j] = t;
    }
}
