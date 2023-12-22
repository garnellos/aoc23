import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day3
{
    public static void main(String[] args)
    {
        String file = ".\\input.txt";
        try {
            Scanner in = new Scanner(new File(file));

            int counter = 0;

            ArrayList<String> lines = new ArrayList<>();

            while (in.hasNextLine())
                lines.add(in.nextLine());
            
            in.close();

            char[][] charmap = new char[lines.size()][lines.get(0).length()];
            
            for (int i = 0; i < lines.size(); ++i)
                charmap[i] = lines.get(i).toCharArray();
            
            Map map = new Map(charmap);
            
            // Teil I //
            for (PartID p : map.parts)
                counter += p.value;
            
            System.out.println("Ergebnis Teil 1: " + counter);
            
            // Teil II //
            counter = 0;
            for (int i = 0; i < charmap.length; ++i)
                for (int j = 0; j < charmap[i].length; ++j)
                    if (Character.toString(charmap[i][j]).equals("*")) {
                        // gear detected
                        // search for adjacent part ids
                        ArrayList<PartID> pl1 = new ArrayList<>();
                        for (int k = i-1 < 0? 0 : i-1; k <= (i+1 >= charmap.length? charmap.length-1 : i+1); ++k)
                            for (int l = j-1 < 0? 0 : j-1; l <= (j+1 >= charmap[i].length? charmap[i].length-1 : j+1); ++l) {
                                PartID p1 = map.getPartAt(k, l);
                                if (p1 != null) pl1.add(p1);
                            }
                        
                        // filter values to contain no duplicates
                        ArrayList<PartID> pl2 = new ArrayList<>();
                        if (!pl1.isEmpty())
                            for (PartID p : pl1)
                                if (!pl2.contains(p)) pl2.add(p);
                        
                        // check values, if there are exactly 2, multiply and add them to the counter
                        int prod = 1;
                        if (pl2.size() == 2) {
                            for (PartID p : pl2)
                                prod *= p.value;
                            counter += prod;
                        }
                    }

            System.out.println("Ergebnis Teil 2: " + counter);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file);
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    /**
     * @param c The char to be checked.
     * @return true if c is any digit (0-9), false otherwise.
     */
    static boolean isNumber(char c)
    {
        return (c >= 48 && c <= 57);
    }
}

class Map
{
    char[][] rawData;
    List<PartID> parts;
    Point size;

    Map(char[][] dat)
    {
        rawData = dat;

        LinkedList<PartID> ptList = new LinkedList<>();

        boolean obtainingNumber = false,
                isPart = false;
        Point newNumberOrigin = new Point(-1, -1);
        StringBuffer numberString = new StringBuffer();

        for (int i = 0; i < dat.length; ++i) {
            for (int j = 0; j < dat[i].length; ++j) {
                if (obtainingNumber && Day3.isNumber(dat[i][j])) {
                    // continue reading number
                    numberString.append(dat[i][j]);
                } else if (obtainingNumber && !Day3.isNumber(dat[i][j])) {
                    // end of number reached
                    obtainingNumber = false;
                    isPart = false;
                    
                    // finding out if it is a part number
                    for (int k = ((i-1) < 0? 0 : i-1); k <= ((i+1) >= dat.length? dat.length-1 : i+1); ++k)
                        for (int l = ((((int)newNumberOrigin.getY())-1) < 0? 0 : ((int)newNumberOrigin.getY())-1); l <= j; ++l)
                            if (Character.toString(dat[k][l]).matches("[^0-9.]"))
                                isPart = true;
                    

                    if (isPart)
                        ptList.add(new PartID(Integer.parseInt(numberString.toString()),
                                (int) newNumberOrigin.getX(),
                                (int) newNumberOrigin.getY()));

                    // reset buffers and flags
                    numberString = new StringBuffer();
                    isPart = false;
                } else if (Day3.isNumber(dat[i][j])) {
                    // this char is a number
                    obtainingNumber = true;

                    // starting to read number
                    numberString.append(dat[i][j]);

                    // saving number origin
                    newNumberOrigin = new Point(i, j);
                } else {
                    ; // this char is no number and may be skipped
                }
            }

            if (obtainingNumber) {
                // end of number reached
                obtainingNumber = false;
                isPart = false;
                
                // finding out if it is a part number
                for (int k = ((i-1) < 0? 0 : i-1); k <= ((i+1) >= dat.length? dat.length-1 : i+1); ++k)
                    for (int l = ((((int)newNumberOrigin.getY())-1) < 0? 0 : ((int)newNumberOrigin.getY())-1); l <= dat[i].length-1; ++l)
                        if (Character.toString(dat[k][l]).matches("[^0-9.]"))
                            isPart = true;

                if (isPart)
                    ptList.add(new PartID(Integer.parseInt(numberString.toString()),
                            (int) newNumberOrigin.getX(),
                            (int) newNumberOrigin.getY()));

                // reset buffers and flags
                numberString = new StringBuffer();
                isPart = false;
            }
        }

        parts = ptList;
    }

    /**
     * @return The part ID located at the (x/y) coordinate or <code>null</code>, if there is none.
     */
    public PartID getPartAt(int x, int y)
    {
        PartID re = null;
        for (PartID p : parts) {
            if ((  x == (int) p.position.getX())
                && y >= (int) p.position.getY()
                && y <  (int) p.position.getY() + p.length
                ) {
                re = p;
                break;
            }
        }
        return re;
    }
}

class PartID
{
    int value;
    Point position;
    int length;

    PartID(int v, int x, int y)
    {
        this.value = v;
        this.length = Integer.toString(value).length();
        this.position = new Point(x, y);
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}
