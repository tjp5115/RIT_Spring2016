import java.io.IOException;

/**
 * Created by Crystal on 4/18/2016.
 */
public class main {
    public static void main(String args[]) throws IOException {
        CrackSpeck cs = new CrackSpeck();
        String test = "1c850e0fa43d d26831aa8c86 7fac79896210 02cb653c2f03 a2976e878422 52f3b95ea06b ffb73bc05464 6500fde94935 5c9e538407c7 1b9d654f6848 ace24db310af a6522a786eff 6a0672b6ea22 561ab8ece7b5 642884f1bd25 b3bf47b961c4 5c960ce58413 14c67b778d5c 47568384153f 46f1cb5e8fcb 998fd321d75b 7617f09fa035";
        /*
        String []test1 = test.split(" ");
        cs. main(test1);
        System.out.println();

        test = "a0a73c56b441 1b37cdaca186 b595990fe06c 1833cffdb306 3850975362c1 9fe2a65107aa cde22dfe7de7 76ddda616693 f4196a7914e0 805f417ecea9 dfd514539328 87cef8578e88 d7a5444a4e7d 0ace07d8ab53 401fa4d4e662 30f4ed0fcbb2 59a9f8f71ee1 9859c0ce7f99 09ddb1ba7e4b 72c3aedb11cc";
        String []test2 = test.split(" ");
        cs.main(test2);
        System.out.println();

        test = "29535d55c7f2 1180f4d4a8f9 ba40b4dad2fb 8d70af05dd66 e75950b05050 618b203a21e4 eac97ecb4d95 ee28fe56ba70 f9e4ccdf2981 c3d86423f9cc a09e54b7b4fe f455bc41992b 949ad44e6613 d801fbe8b2bd 2a6f5042f1ad 7ab5253c9b1b cff5572fa13c 764ccae91126 2df24cf94c05 e55c2ae34bda";
        String []test3 = test.split(" ");
        cs.main(test3);
        System.out.println();

        test = "485636c985a1 6f5b1939bb79 6668b5142704 e96781d4104a 5003607c930d 33146924d7c9 92fe50f21aea cb08e72b966b 43176394ab9c f396c1978fb2 4cccfec82f16 d9896ccacc6a 12177e3e0980 2c33f5aa6b52 c6c0db6d333d e8b2ecdeeca4 f847ecc47ffa d2f56f494b58 978b2b198d77 240ccd721f2a";
        String []test4 = test.split(" ");
        cs.main(test4);
        System.out.println();

        test = "7ffd73db30d0 de5fabe644ed 6cb7380e21f1 2097180a1aee 50c9152eb3e6 073471d0bea6 9ec568463a02 1d40b3d2ace7 2d116f486954 c88636bd0b3c 49e6b6fb818f edcd0497b37d 21d7aea69452 abb4e4d36d21 16f41abda65c e76ad4a9b157 7aa370f7d77f a4a6db82eca8 a8d7681dc398 187021f01f19";
        String []test5 = test.split(" ");
        cs.main(test5);
        System.out.println();

        test = "0fc1521d5ee2 06d31d08d981";
        String []test6 = test.split(" ");
        cs.main(test6);
        System.out.println();

        test = "c5c37fd0c83f ca900c81a114 0b2f0461b753 2683aadf4ee3";
        String []test7 = test.split(" ");
        cs.main(test7);
        System.out.println();

        test = "3b68997c5db4 88189f456584 7eb54b00d1af 77feb277ebf7 944edab88e1a 91c8c5f8534f";
        String []test8 = test.split(" ");
        cs.main(test8);
        System.out.println();

        test = "4d9de8fb4245 cbe9e2068b38 d9cdf399f0ad 6b0804fa10c0 329ed0ef1224 e466b234af4e 7fc21d79f7d0 f875d9d420fd";
        String []test9 = test.split(" ");
        cs.main(test9);
        System.out.println();

        test = "3df498711718 56b8a24a338b 6965667f1710 06b588fba026 27f795c3c532 dc1ab1fb9069 8c54abdaaf10 7f57c13bb315 f950ed749932 6c749c90818a";
        String []test10= test.split(" ");
        cs.main(test10);
        System.out.println();

        test = "1bbc66517028 acee938aeebe 3c93b2e148b6 c815de2231ed 42d16d17327e 1981cee9551f b3ed9786ee01 1db249566090 4e39cf1e6d9c 68001e7ae8e5 b3bc1cf7f04d c2d5e77baae1";
        String []test11= test.split(" ");
        cs.main(test11);
        System.out.println();

        test = "0cbead6811f6 8fd30e28bc1c f0a44d722aa4 d19b7f8e7ea7 26b7373cbe52 c6f15749c756 0a8ca3a4908e d35a96f94a62 50e5231ed8d6 2995399d414c 29abc3c0a206 c38faa30de23 cdbcebfaf1e5 a863d5b21d5d";
        String []test12= test.split(" ");
        cs.main(test12);
        System.out.println();

        test = "e1ea737c641b 371887884086 34775ad0238c e68ff366bbd1 05866ed12897 26e8d3074997 bcf313ef030b 137aea539e92 e1f00b1fbacd 7f212a5478b6 b2ff7031b24f 19ed59f4b2d4 c4d880cceb94 52d05a306e27 00f5e70431e6 e9df4bd1b633";
        String []test13= test.split(" ");
        cs.main(test13);
        System.out.println();

        test = "db79bb1d7502 d115a48e86b8 c2adc177b15c 72966763a87b 10c7ff16ee59 5dbd346dd81a bf2d8d2a5b14 c054d5a032b4 7bcfeb51b1c5 fdc2d230e19d 10e06f48fd64 c6f1499c035c 74397a620254 4782c41c07d8 8dff98e74e31 6fe45f7f65d9 1db527c7fd36 5c1128301728";
        String []test14= test.split(" ");
        cs.main(test14);
        System.out.println();

        test = "523a05934310 36808786ea90 365871bfb606 4a4e8ba71d4d 2d8dd7443905 60db265d5761 5558c98d17b6 03f7d82482e3 44ae10d8248a 29314b1bffb7 c132c53fbd43 934c8c29721c 2625f8ca32f1 af081478da12 0f0e70eb7919 32bbfc05f9c2 8d6fa699f06c cb33d8874664 fcc1aa3dd5c9 b8fd084cdb88";
        String []test15= test.split(" ");
        cs.main(test15);
        System.out.println();
        */

        test = "";
        String []test16= test.split(" ");
        //cs.main(test16);
        System.out.println();

        test = "0123456789 0123456789";
        String []test17= test.split(" ");
        //cs.main(test17);
        System.out.println();

        test = "nothex nothex";
        String []test18= test.split(" ");
        //cs.main(test18);
        System.out.println();

        test = "0123456789ab 0123456789ab 0123456789ab";
        String []test19= test.split(" ");
        //cs.main(test19);
        System.out.println();

        test = "1c850e0fa43d d26831aa8c86";
        String []test20= test.split(" ");
        cs.main(test20);
        System.out.println();
    }
}
