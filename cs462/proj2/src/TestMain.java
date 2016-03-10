/**
 * Created by Crystal on 3/9/2016.
 */
public class TestMain {
    public static void main(String args[]) throws Exception{
        /*
        String arg[] = {"ARK4", "000000000000000000000000", "0000000000000000"};
        BlockCipherEncrypt.main(arg);

        String arg1[] = {"ARK4", "000000000000000000000000", "ffffffffffffffff"};
        BlockCipherEncrypt.main(arg1);

        String arg2[] = {"ARK4", "ffffffffffffffffffffffff", "0000000000000000"};
        BlockCipherEncrypt.main(arg2);

        String arg3[] = {"ARK4", "ffffffffffffffffffffffff", "ffffffffffffffff"};
        BlockCipherEncrypt.main(arg3);
        */
        String arg[] = {"ARK4", "000000000000000000000000", "9d09d8843fe7e0a0"};
        BlockCipherDecrypt.main(arg);

        String arg1[] = {"ARK4", "000000000000000000000000", "38851b97c41d8f0a"};
        BlockCipherDecrypt.main(arg1);

        String arg2[] = {"ARK4", "ffffffffffffffffffffffff", "f9af365d1df2059e"};
        BlockCipherDecrypt.main(arg2);

        String arg3[] = {"ARK4", "ffffffffffffffffffffffff", "0847fbde8d775b7a"};
        BlockCipherDecrypt.main(arg3);
    }
}
