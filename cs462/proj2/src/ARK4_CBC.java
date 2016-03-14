/**
 * Created by Tyler Paulsenon 3/9/2016.
 * A cipher block chaining mode class to encrypt/decrypt multiple blocks for the encryption algorithm ARK4.
 */
public class ARK4_CBC implements MultiBlockCipher{

    ARK4 ciper = new ARK4();
    byte nonce[] = new byte[8];
    byte key[] = new byte[ciper.keySize()];
    /**
     * Returns this multi-block cipher's block size in bytes.
     *
     * @return Block size.
     */
    @Override
    public int blockSize() {
        return ciper.blockSize();
    }

    /**
     * Returns this multi-block cipher's key size in bytes. If the mode of
     * operation includes both a key and a nonce, <TT>keySize()</TT> returns the
     * size of the key plus the size of the nonce in bytes.
     *
     * @return Key size.
     */
    @Override
    public int keySize() {
        return ciper.keySize() + 8;
    }

    /**
     * Set the key for this multi-block cipher. <TT>key</TT> must be an array of
     * bytes whose length is equal to <TT>keySize()</TT>. If the mode of
     * operation includes both a key and a nonce, <TT>key</TT> contains the
     * bytes of the key followed by the bytes of the nonce. The multi-block
     * cipher is initialized, such that successive calls to <TT>encrypt()</TT>
     * or <TT>decrypt()</TT> will encrypt or decrypt a series of blocks.
     *
     * @param key Key.
     * @throws IllegalArgumentException (unchecked exception) Thrown if <TT>key.length</TT> &ne;
     *                                  <TT>keySize()</TT>.
     */
    @Override
    public void setKey(byte[] key) throws IllegalArgumentException{
        if ( key.length != keySize() ) throw new IllegalArgumentException("key not the right length.");
        System.arraycopy(key,ciper.keySize(),nonce,0,8);
        System.arraycopy(key, 0, this.key, 0, ciper.keySize());
        ciper.setKey(this.key);
        ciper.encrypt(nonce);
    }

    /**
     * Encrypt the given plaintext block, which is not the final block in a
     * series of blocks being encrypted. <TT>text</TT> must be an array of bytes
     * whose length is equal to <TT>blockSize()</TT>. On input, <TT>text</TT>
     * contains the plaintext block. On output, <TT>text</TT> contains the
     * ciphertext block.
     *
     * @param text Plaintext (on input), ciphertext (on output).
     * @throws IllegalArgumentException (unchecked exception) Thrown if <TT>text.length</TT> &ne;
     *                                  <TT>blockSize()</TT>.
     */
    @Override
    public void encrypt(byte[] text) throws IllegalArgumentException {
        if ( text.length != blockSize()) throw new IllegalArgumentException("text.length != cipher.blockSize())");
        ciper.setKey(key);
        xor_nonce(text);
        ciper.encrypt(text);
        System.arraycopy(text, 0, nonce, 0, blockSize());
    }

    /**
     * Encrypt the given plaintext block, which is the final block in a series
     * of blocks being encrypted. <TT>text</TT> must be an array of bytes whose
     * length is equal to <TT>blockSize()</TT>. On input, <TT>text</TT> contains
     * the plaintext block. On output, <TT>text</TT> contains the ciphertext
     * block.
     * <p>
     * The argument <I>n</I> states the number of bytes in the array that
     * contain actual plaintext, from 0 through (block size &minus; 1). The last
     * (block size &minus; <I>n</I>) bytes of the block are replaced with
     * padding bytes before encrypting the block.
     *
     * @param text Plaintext (on input), ciphertext (on output).
     * @param n    Number of actual plaintext bytes.
     * @throws IllegalArgumentException (unchecked exception) Thrown if <TT>text.length</TT> &ne;
     *                                  <TT>blockSize()</TT>. Thrown if <TT>n</TT> is not in the range 0
     *                                  through <TT>blockSize()</TT>&minus;1.
     */
    @Override
    public void finalEncrypt(byte[] text, int n) {
        if ( text.length != blockSize()) throw new IllegalArgumentException("text.length != cipher.blockSize())");
        text[n] = (byte)0x80;
        for( int i = n+1; i < blockSize(); ++i){
            text[i] = (byte)0x00;
        }
        encrypt(text);
    }

    /**
     * Decrypt the given ciphertext block, which is not the final block in a
     * series of blocks being decrypted. <TT>text</TT> must be an array of bytes
     * whose length is equal to <TT>blockSize()</TT>. On input, <TT>text</TT>
     * contains the ciphertext block. On output, <TT>text</TT> contains the
     * plaintext block.
     *
     * @param text Ciphertext (on input), plaintext (on output).
     * @throws IllegalArgumentException (unchecked exception) Thrown if <TT>text.length</TT> &ne;
     *                                  <TT>blockSize()</TT>.
     */
    @Override
    public void decrypt(byte[] text) {
        if ( text.length != blockSize()) throw new IllegalArgumentException("text.length != cipher.blockSize())");
        byte []temp = new byte[nonce.length];
        System.arraycopy(text, 0, temp, 0, blockSize());
        ciper.setKey(key);
        ciper.decrypt(text);
        xor_nonce(text);
        System.arraycopy(temp, 0, nonce, 0, blockSize());
    }

    /**
     * Decrypt the given ciphertext block, which is the final block in a series
     * of blocks being decrypted. <TT>text</TT> must be an array of bytes whose
     * length is equal to <TT>blockSize()</TT>. On input, <TT>text</TT> contains
     * the ciphertext block. On output, <TT>text</TT> contains the plaintext
     * block.
     * <p>
     * The method detects padding bytes at the end of the array and returns the
     * number of bytes at the beginning of the array that contain actual
     * plaintext, from 0 through (block size &minus; 1).
     *
     * @param text Ciphertext (on input), plaintext (on output).
     * @return Number of actual plaintext bytes.
     * @throws IllegalArgumentException (unchecked exception) Thrown if <TT>text.length</TT> &ne;
     *                                  <TT>blockSize()</TT>. Thrown if <TT>text</TT> does not end with one
     *                                  or more correct padding bytes.
     */
    @Override
    public int finalDecrypt(byte[] text) {
        if ( text.length != blockSize()) throw new IllegalArgumentException("text.length != cipher.blockSize())");
        decrypt(text);
        int n = 0;
        for(int i = text.length-1; text[i] == 0x00; --i){
            n += 1;
        }
        return text.length - n - 1;
    }

    /**
     * xor a given text with the nonce.
     * @param text - text to xor with nonce.
     */
    public void xor_nonce(byte[] text){
        for(int i = 0; i < text.length; ++i)
            text[i] ^= nonce[i];
    }

    /**
     * Function to help debugging. hex dumps a given byte array.
     * @param text - text to print out.
     */
    public void printByteArray(byte[] text){
        for (int j = 0; j < text.length; ++ j)
            System.out.printf ("%02x", text[j]);
        System.out.println();
    }

}
