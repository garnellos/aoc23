import java.util.Scanner;

import java.io.File;

public class Day2
{
    public static void main(String[] args)
    {
        File file = new File(".\\input.txt");

        try {
            Scanner in = new Scanner(file);

            int counter = 0;

            Game g;
            while (in.hasNextLine()) {
                g = new Game(in.nextLine());

                /* Pt. 1
                if (g.maxCount(Game.Colors.RED) <= 12
                        && g.maxCount(Game.Colors.GREEN) <= 13
                        && g.maxCount(Game.Colors.BLUE) <= 14) {
                    counter += g.id;
                }
                */

                // Pt. 2
                counter += g.maxCount(Game.Colors.RED)
                         * g.maxCount(Game.Colors.GREEN)
                         * g.maxCount(Game.Colors.BLUE);
            }

            in.close();

            System.out.println("Ergebnis: " + counter);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

class Game
{
    int id;
    Hand[] hands;

    /**
     * Erstellt ein Game aus dem String <code>s</code>.
     * @param s
     */
    Game(String s)
    {
        String[] ids = s.split(":");
        id = Integer.parseInt(ids[0].replaceAll("Game", "").strip());
        String[] lHands = ids[1].split(";");
        hands = new Hand[lHands.length];

        for (int i = 0; i < hands.length; ++i) {
            String[] cols = lHands[i].split(",");
            hands[i] = new Hand();
            for (String col : cols) {
                if (col.contains("red"))
                    hands[i].red   = Integer.parseInt(col.replace("red", "").strip());
                else if (col.contains("green"))
                    hands[i].green = Integer.parseInt(col.replace("green", "").strip());
                else if (col.contains("blue"))
                    hands[i].blue  = Integer.parseInt(col.replace("blue", "").strip());
            }
        }
    }

    /**
     * Gibt die maximale Anzahl an Würfeln einer Farbe basierend auf den im Spiel vorhandenen Hands an.
     * @param c Die zu ermittelnde Farbe als Wert aus {@link Game#Colors}.
     */
    int maxCount(Colors c)
    {
        int max = -1;

        for (Hand h : hands)
            if (h.getCount(c) > max) max = h.getCount(c);

        return max;
    }

    class Hand
    {
        int red, green, blue;

        Hand() {}

        Hand(int r, int g, int b)
        {
            this.red = r;
            this.green = g;
            this.blue = b;
        }

        int getCount(Game.Colors c)
        {
            if (c == Colors.RED) return red;
            if (c == Colors.GREEN) return green;
            if (c == Colors.BLUE) return blue;
            return -1;
        }
    }

    enum Colors
    {
        RED, GREEN, BLUE;
    }
}
