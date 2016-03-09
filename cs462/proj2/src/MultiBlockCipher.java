/**
 * Interface MultiBlockCipher specifies the interface for a block cipher with a
 * mode of operation that encrypts or decrypts a series of whole blocks.
 *
 * @author  Alan Kaminsky
 * @version 02-Mar-2016
 */
public interface MultiBlockCipher
	{

	/**
	 * Returns this multi-block cipher's block size in bytes.
	 *
	 * @return  Block size.
	 */
	public int blockSize();

	/**
	 * Returns this multi-block cipher's key size in bytes. If the mode of
	 * operation includes both a key and a nonce, <TT>keySize()</TT> returns the
	 * size of the key plus the size of the nonce in bytes.
	 *
	 * @return  Key size.
	 */
	public int keySize();

	/**
	 * Set the key for this multi-block cipher. <TT>key</TT> must be an array of
	 * bytes whose length is equal to <TT>keySize()</TT>. If the mode of
	 * operation includes both a key and a nonce, <TT>key</TT> contains the
	 * bytes of the key followed by the bytes of the nonce. The multi-block
	 * cipher is initialized, such that successive calls to <TT>encrypt()</TT>
	 * or <TT>decrypt()</TT> will encrypt or decrypt a series of blocks.
	 *
	 * @param  key  Key.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>key.length</TT> &ne;
	 *     <TT>keySize()</TT>.
	 */
	public void setKey
		(byte[] key);

	/**
	 * Encrypt the given plaintext block, which is not the final block in a
	 * series of blocks being encrypted. <TT>text</TT> must be an array of bytes
	 * whose length is equal to <TT>blockSize()</TT>. On input, <TT>text</TT>
	 * contains the plaintext block. On output, <TT>text</TT> contains the
	 * ciphertext block.
	 *
	 * @param  text  Plaintext (on input), ciphertext (on output).
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>text.length</TT> &ne;
	 *     <TT>blockSize()</TT>.
	 */
	public void encrypt
		(byte[] text);

	/**
	 * Encrypt the given plaintext block, which is the final block in a series
	 * of blocks being encrypted. <TT>text</TT> must be an array of bytes whose
	 * length is equal to <TT>blockSize()</TT>. On input, <TT>text</TT> contains
	 * the plaintext block. On output, <TT>text</TT> contains the ciphertext
	 * block.
	 * <P>
	 * The argument <I>n</I> states the number of bytes in the array that
	 * contain actual plaintext, from 0 through (block size &minus; 1). The last
	 * (block size &minus; <I>n</I>) bytes of the block are replaced with
	 * padding bytes before encrypting the block.
	 *
	 * @param  text  Plaintext (on input), ciphertext (on output).
	 * @param  n     Number of actual plaintext bytes.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>text.length</TT> &ne;
	 *     <TT>blockSize()</TT>. Thrown if <TT>n</TT> is not in the range 0
	 *     through <TT>blockSize()</TT>&minus;1.
	 */
	public void finalEncrypt
		(byte[] text,
		 int n);

	/**
	 * Decrypt the given ciphertext block, which is not the final block in a
	 * series of blocks being decrypted. <TT>text</TT> must be an array of bytes
	 * whose length is equal to <TT>blockSize()</TT>. On input, <TT>text</TT>
	 * contains the ciphertext block. On output, <TT>text</TT> contains the
	 * plaintext block.
	 *
	 * @param  text  Ciphertext (on input), plaintext (on output).
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>text.length</TT> &ne;
	 *     <TT>blockSize()</TT>.
	 */
	public void decrypt
		(byte[] text);

	/**
	 * Decrypt the given ciphertext block, which is the final block in a series
	 * of blocks being decrypted. <TT>text</TT> must be an array of bytes whose
	 * length is equal to <TT>blockSize()</TT>. On input, <TT>text</TT> contains
	 * the ciphertext block. On output, <TT>text</TT> contains the plaintext
	 * block.
	 * <P>
	 * The method detects padding bytes at the end of the array and returns the
	 * number of bytes at the beginning of the array that contain actual
	 * plaintext, from 0 through (block size &minus; 1).
	 *
	 * @param  text  Ciphertext (on input), plaintext (on output).
	 *
	 * @return  Number of actual plaintext bytes.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>text.length</TT> &ne;
	 *     <TT>blockSize()</TT>. Thrown if <TT>text</TT> does not end with one
	 *     or more correct padding bytes.
	 */
	public int finalDecrypt
		(byte[] text);

	}
