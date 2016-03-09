import edu.rit.util.Hex;
import edu.rit.util.Instance;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Class MultiBlockDecryptFile is a unit test main program for a multi-block
 * cipher class that decrypts a ciphertext file. The file's contents are
 * replaced with the plaintext.
 * <P>
 * Usage: <TT>java MultiBlockDecryptFile <I>cipherClass</I> <I>key</I>
 * <I>file</I></TT>
 * <BR><TT><I>cipherClass</I></TT> = MultiBlockCipher class name
 * <BR><TT><I>key</I></TT> = Key (hexadecimal)
 * <BR><TT><I>file</I></TT> = Ciphertext file name
 *
 * @author  Alan Kaminsky
 * @version 05-Mar-2016
 */
public class MultiBlockDecryptFile
	{

	/**
	 * Main program.
	 */
	public static void main
		(String[] args)
		throws Exception
		{
		// Parse command line arguments.
		if (args.length != 3) usage();
		String cipherClass = args[0];
		byte[] key = Hex.toByteArray (args[1]);
		File file = new File (args[2]);

		// Create an instance of the multi-block cipher class.
		MultiBlockCipher cipher = (MultiBlockCipher)
			Instance.newInstance (cipherClass+"()");

		// Verify precondition.
		if (key.length != cipher.keySize())
			error (String.format ("Key must be %d bytes", cipher.keySize()));

		// Set cipher key.
		cipher.setKey (key);

		// Read ciphertext from file into a byte array.
		FileInputStream fin = new FileInputStream (file);
		ByteArrayOutputStream ct = new ByteArrayOutputStream();
		int b;
		while ((b = fin.read()) != -1)
			ct.write (b);
		fin.close();
		byte[] ciphertext = ct.toByteArray();

		// Verify another precondition.
		int BLOCK = cipher.blockSize();
		if (ciphertext.length < BLOCK || ciphertext.length % BLOCK != 0)
			error ("Ciphertext length is not a multiple of the block size");

		// Decrypt ciphertext blocks.
		ByteArrayOutputStream pt = new ByteArrayOutputStream();
		byte[] text = new byte [BLOCK];
		int i = 0;
		while (i < ciphertext.length - BLOCK)
			{
			System.arraycopy (ciphertext, i, text, 0, BLOCK);
			cipher.decrypt (text);
			pt.write (text);
			i += BLOCK;
			}
		System.arraycopy (ciphertext, i, text, 0, BLOCK);
		int n = cipher.finalDecrypt (text);
		pt.write (text, 0, n);

		// Write plaintext back into file.
		FileOutputStream fout = new FileOutputStream (file);
		pt.writeTo (fout);
		fout.close();
		}

	/**
	 * Print the given error message and exit.
	 *
	 * @param  msg  Error message.
	 */
	private static void error
		(String msg)
		{
		System.err.printf ("MultiBlockDecryptFile: %s%n", msg);
		usage();
		}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
		{
		System.err.println ("Usage: java MultiBlockDecryptFile <cipherClass> <key> <file>");
		System.err.println ("<cipherClass> = MultiBlockCipher class name");
		System.err.println ("<key> = Key (hexadecimal)");
		System.err.println ("<file> = Ciphertext file name");
		System.exit (1);
		}

	}
