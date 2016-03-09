import edu.rit.util.Hex;
import edu.rit.util.Instance;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Class MultiBlockEncryptFile is a unit test main program for a multi-block
 * cipher class that encrypts a plaintext file. The file's contents are replaced
 * with the ciphertext.
 * <P>
 * Usage: <TT>java MultiBlockEncryptFile <I>cipherClass</I> <I>key</I>
 * <I>file</I></TT>
 * <BR><TT><I>cipherClass</I></TT> = MultiBlockCipher class name
 * <BR><TT><I>key</I></TT> = Key (hexadecimal)
 * <BR><TT><I>file</I></TT> = Plaintext file name
 *
 * @author  Alan Kaminsky
 * @version 05-Mar-2016
 */
public class MultiBlockEncryptFile
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

		// Read plaintext from file into a byte array.
		FileInputStream fin = new FileInputStream (file);
		ByteArrayOutputStream pt = new ByteArrayOutputStream();
		int b;
		while ((b = fin.read()) != -1)
			pt.write (b);
		fin.close();
		byte[] plaintext = pt.toByteArray();

		// Encrypt plaintext blocks.
		ByteArrayOutputStream ct = new ByteArrayOutputStream();
		int BLOCK = cipher.blockSize();
		byte[] text = new byte [BLOCK];
		int i = 0;
		int n;
		while (i < plaintext.length)
			{
			n = plaintext.length - i;
			if (n >= BLOCK)
				{
				System.arraycopy (plaintext, i, text, 0, BLOCK);
				cipher.encrypt (text);
				}
			else
				{
				System.arraycopy (plaintext, i, text, 0, n);
				cipher.finalEncrypt (text, n);
				}
			ct.write (text);
			i += BLOCK;
			}
		if (i == plaintext.length)
			{
			cipher.finalEncrypt (text, 0);
			ct.write (text);
			}

		// Write ciphertext back into file.
		FileOutputStream fout = new FileOutputStream (file);
		ct.writeTo (fout);
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
		System.err.printf ("MultiBlockEncryptFile: %s%n", msg);
		usage();
		}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
		{
		System.err.println ("Usage: java MultiBlockEncryptFile <cipherClass> <key> <file>");
		System.err.println ("<cipherClass> = MultiBlockCipher class name");
		System.err.println ("<key> = Key (hexadecimal)");
		System.err.println ("<file> = Plaintext file name");
		System.exit (1);
		}

	}
