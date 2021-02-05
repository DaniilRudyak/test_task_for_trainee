public class Main {
    public static void main(String[] args) {
        int nb = Integer.parseInt(args[0]);
        String base = args[1];

        String nb1 = args[2];
        String baseSrc = args[3];
        String baseDst = args[4];



        //System.out.println(itoBase("101", "012345678"));
        // System.out.println(itoBase("200", "0123456789", "0123456789abcdef"));

//        System.out.println(ConvertSyst.itoBase("400", "0123456789", "0123456789abcdef"));

        System.out.println(ConvertSyst.itoBase(nb, base));

        System.out.println(ConvertSyst.itoBase(nb1, baseSrc, baseDst));


    }
}
