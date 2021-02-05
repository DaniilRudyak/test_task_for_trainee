import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String t1 = null; //args[1];
        String t2 = null; //args[2];
        ZonedDateTime zonedDateTime1 = null;
        ZonedDateTime zonedDateTime2 = null;
        if (args.length == 3) {
            t1 = args[1];
            t2 = args[2];
            zonedDateTime1 = ZonedDateTime.parse(t1);
            zonedDateTime2 = ZonedDateTime.parse(t2);
        }

        int beginV = 0;
        boolean flagBegin = false;
        int numberOfAttemptsTopUp = 0;
        int numberOfAttemptsScoop = 0;

        int numberOfErrorAttemptsTopUp = 0;
        int numberOfErrorAttemptsScoop = 0;

        int litrTopUp = 0;//налито воды
        int litrScoop = 0;// забрано воды

        int litrErrorTopUp = 0;//не налито воды
        int litrErrorScoop = 0;// не забрано воды


        int V = 0;
        int curV = 0;

        DateTimeFormatter changetimeParser_Z = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.000Z");
        try {

            File file = new File(new File("").getAbsolutePath() + "\\" + args[0]);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                line = new String(line.getBytes("windows-1251"), "UTF-8");
                if (i == 0)
                    V = Integer.parseInt(line);
                else if (i == 1)
                    curV = Integer.parseInt(line);

                else {
                    StringBuilder stringBuilder = new StringBuilder(line);


                    boolean flagError = false;


                    line = line.replace(" ", "");
                    int ind1 = line.indexOf('[');
                    int ind2 = line.indexOf(']');
                    String strDate = line.substring(0, ind1 - 1);

                    ZonedDateTime time = ZonedDateTime.parse(strDate);
                    String username = line.substring(ind1 + 1, ind2);
                    String action = line.substring(ind2 + 2);
                    action = action.replace("l", "");
                    int l = 0;
                    if (action.toUpperCase().contains(("wannatopup").toUpperCase())) {
                        l = Integer.parseInt(action.replace("wannatopup", ""));

                        if (l > V - curV)
                            flagError = true;
                        else
                            curV += l;

                    } else if (action.toUpperCase().contains(("wannascoop").toUpperCase())) {
                        l = Integer.parseInt(action.replace("wannascoop", "")) * (-1);
                        if (Math.abs(l) > curV)
                            flagError = true;
                        else
                            curV += l;
                    }


                    if ((args.length == 1) || (time.isAfter(zonedDateTime1) && time.isBefore(zonedDateTime2))) {
                        if (!flagBegin) {
                            beginV = curV;
                            flagBegin = true;
                        }
                        if (l > 0) {
                            ++numberOfAttemptsTopUp;
                            if (!flagError) {
                                litrTopUp += l;
                            } else {
                                ++numberOfErrorAttemptsTopUp;
                                litrErrorTopUp += l;
                            }
                        } else if (l < 0) {
                            ++numberOfAttemptsScoop;
                            if (!flagError) {
                                litrScoop += l;
                            } else {
                                ++numberOfErrorAttemptsScoop;
                                litrErrorScoop += l;
                            }
                        }
                    }
                }

                ++i;
                line = reader.readLine();
            }

            double percentError = (((double) (numberOfErrorAttemptsTopUp + numberOfErrorAttemptsScoop) / (double) (numberOfAttemptsTopUp + numberOfAttemptsScoop))) * 100;

            String csv = "data.csv";
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(csv), "Windows-1251"), ';');

            String[] record = {"какое количество попыток налить воду в бочку было за указанный период?",
                    "какое количество попыток забрать воду из бочки было за указанный период?",
                    "какой процент ошибок был допущен за указанный период?",
                    "какой объем воды был налит в бочку за указанный период?",
                    "какой объем воды был не налит в бочку за указанный период?",
                    "какой объем воды был забран из бочки за указанный период?",
                    "какой объем воды был не забран из бочки за указанный период?",
                    "какой объем воды был в бочке в начале указанного периода?",
                    "Какой в конце указанного периода?"

            };

            writer.writeNext(record);
            String[] record1 = {Integer.valueOf(numberOfAttemptsTopUp).toString(),
                    Integer.valueOf(numberOfAttemptsScoop).toString(),
                    Double.valueOf(percentError).toString(),
                    Integer.valueOf(litrTopUp).toString(),
                    Integer.valueOf(litrErrorTopUp).toString(),
                    Integer.valueOf((-1) * litrScoop).toString(),
                    Integer.valueOf((-1) * litrErrorScoop).toString(),
                    Integer.valueOf(beginV).toString(),
                    Integer.valueOf(curV).toString()
            };
            writer.writeNext(record1);

            writer.close();


        } catch (FileNotFoundException e) {
            System.out.println("USAGE");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("USAGE");
            e.printStackTrace();
        }
    }
}
