public class ConvertSyst {
    static String itoBase(String nb, String baseSrc, String baseDst) {
        StringBuilder answer = new StringBuilder();

        if (baseDst.length()!=10) {
            nb = itoBase(nb, baseSrc, "0123456789");


            answer.append(itoBase(Integer.parseInt(nb), baseDst));
        } else {

            int answerNum = 0;

            for (int i = 0; i < nb.length(); ++i) {
                answerNum += (int) (Integer.parseInt(String.valueOf(nb.charAt(i))) * Math.pow(baseSrc.length(), nb.length() - i - 1));
            }
            answer.append(answerNum);
        }
        return answer.toString();
    }

    static String itoBase(int nb, String base) {
        StringBuilder answer = new StringBuilder();

        int syst = base.length();
        int number = Integer.valueOf(nb);
        int cur = number;


        while (cur != 0) {
            answer.append(base.charAt(cur % syst));
            cur = cur / syst;
        }
        answer.reverse();
        return answer.toString();
    }
}
