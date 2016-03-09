/**
 * Created by Crystal on 3/9/2016.
 */
public class TestMain {
    public static void main(String args[]) throws Exception{
        String arg[] = {"ARK4", "000000000000000000000000", "0000000000000000"};
        BlockCipherEncrypt.main(arg);

        String arg1[] = {"ARK4", "000000000000000000000000", "ffffffffffffffff"};
        BlockCipherEncrypt.main(arg1);

        String arg2[] = {"ARK4", "ffffffffffffffffffffffff", "0000000000000000"};
        BlockCipherEncrypt.main(arg2);

        String arg3[] = {"ARK4", "ffffffffffffffffffffffff", "ffffffffffffffff"};
        BlockCipherEncrypt.main(arg3);

    }
}
