import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

public class Day1
{
    /**
     * @param args Der Pfad zur Eingabedatei.
     */
    public static void main(String[] args)
    {
        // prüfen, ob (genau ein) Argument übergeben wurde
        if (args.length != 1) {
            // kein Pfad wurde auf der Kommandozeile übergeben
            System.out.println("Bitte beim Aufruf (genau) einen Pfad übergeben. Zum Beispiel:");
            System.out.println(" $ java Day1 ./input.txt");
            System.exit(-1);
        }
        
        File file = new File(args[0]);

        try {
            
            Scanner in = new Scanner(file);

            String line = "";
            int cali = 0;

            while (in.hasNextLine()) {
                // Zeile einlesen ... und parsen
                line = textToDigits(in.nextLine());

                // Variablen für Zifferpositionen definieren
                int dig1 = -1,
                    dig2 = -1;
                    
                // Algorithmus I : Positionen finden
                /*

                // Buffer für Suche
                int v = -1;
                
                // erste Ziffer suchen
                for (int i = 48; i <= 57; ++i) {
                    v = line.indexOf(i);
                    if (dig1 == -1 || (v < dig1 && v > -1)) dig1 = v;
                }

                // letzte Ziffer suchen
                for (int i = 48; i <= 57; ++i) {
                    v = line.lastIndexOf(i);
                    if (dig2 == -1 || v > dig2) dig2 = v;
                }
                */

                // Algorithmus II: Buchstaben entfernen
                line = line.replaceAll("[a-z]", "");
                dig1 = 0;
                dig2 = line.length() - 1;

                // Zahl basteln
                int addtAmount = Integer.parseInt(
                    Character.toString(line.charAt(dig1))
                            .concat(Character.toString(line.charAt(dig2)))
                );

                System.out.print("+" + addtAmount);

                if (addtAmount < 0) {
                    System.err.println("Fehler in while (in.hasNextLine()) {...}");
                    System.exit(-1);
                }

                // Zahl addieren
                cali += addtAmount;
            }
            
            System.out.println("Ergebnis: " + cali);

            in.close();
            
        } catch (StringIndexOutOfBoundsException e) {
            System.err.println("Zeile konnte nicht verarbeitet werden.");
            e.printStackTrace();
            System.exit(-1);
        } catch (FileNotFoundException e) {
            System.err.println("Datei nicht gefunden.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Ersetzt im String <i>s</i> wörtlich ausgeschriebene Zahlenwerte in englischer Sprache (z.B. "one") durch die entsprechende Ziffer (in diesem Fall "1").
     * @param s Der zu bearbeitende String.
     * @return Der bearbeitete String.
     */
    static String textToDigits(String s)
    {
        return s.replaceAll("one", "one1one")
            .replaceAll("two", "two2two")
            .replaceAll("three", "three3three")
            .replaceAll("four", "four4four")
            .replaceAll("five", "five5five")
            .replaceAll("six", "six6six")
            .replaceAll("seven", "seven7seven")
            .replaceAll("eight", "eight8eight")
            .replaceAll("nine", "nine9nine")
            .replaceAll("zero", "zero0zero");
    }
}
