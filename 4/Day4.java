import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day4
{
    public static void main(String[] args)
    {
        // this is a comment
        String file = ".\\input.txt";
        try {
            Scanner in = new Scanner(new File(file));

            ArrayList<Card> cards = new ArrayList<>();
            int[] cardCount;

            int counter = 0;
            while (in.hasNextLine()) {
                Card c = new Card(in.nextLine());
                cards.add(c);

                // Teil I //
                counter += c.value();
            }
            
            System.out.println("Ergebnis für Teil 1: " + counter);

            // Teil II //
            counter = 0;
            
            cardCount = new int[cards.size()];
            for (int i = 0; i < cardCount.length; ++i)
                cardCount[i] = 1;
            
            for (Card c : cards) {
                for (int i = c.id; i < c.id + c.winningNumbers(); ++i) {
                    for (int j = 0; j < cardCount[c.id-1]; ++j) {
                        if (i >= cardCount.length) break;
                        cardCount[i] += 1;
                    }
                }
            }

            for (int i : cardCount) counter += i;

            System.out.println("Ergebnis für Teil 2: " + counter);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file);
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}

class Card
{
    int id;
    int[] winners, numbers;

    Card(String data)
    {
        String[] parts = new String[3];
        parts[0] = data.split(":")[0].strip();
        parts[1] = data.split(":")[1].split("\\|")[0].strip();
        parts[2] = data.split(":")[1].split("\\|")[1].strip();

        id = Integer.parseInt(parts[0].replace("Card", "").strip());

        ArrayList<Integer> wal = new ArrayList<>(), nal = new ArrayList<>();
        for (String s : parts[1].split(" ")) {
            if (s.strip().isEmpty()) continue;
            wal.add(Integer.valueOf(s.strip()));
        }

        for (String s : parts[2].split(" ")) {
            if (s.strip().isEmpty()) continue;
            nal.add(Integer.valueOf(s.strip()));
        }

        winners = new int[wal.size()];
        numbers = new int[nal.size()];

        for (int i = 0; i < winners.length; ++i)
            winners[i] = wal.get(i);
        
        for (int i = 0; i < numbers.length; ++i)
            numbers[i] = nal.get(i);
    }

    /**
     * @return The value of the given card.
     */
    int value()
    {
        if (winningNumbers() == 0) return 0;
        return (int) Math.pow(2, winningNumbers() - 1);
    }

    int winningNumbers()
    {
        int n = 0;

        for (int i : winners)
            for (int j : numbers)
                if (i == j)
                    ++n;
        
        return n;
    }
}