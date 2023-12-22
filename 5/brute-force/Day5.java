import java.io.*;
import java.util.*;

public class Day5
{
    public static void main(String[] args)
    {
        String file = ".\\input.txt";
        try {
            Scanner in = new Scanner(new File(file));

            ArrayList<Long> seedData = new ArrayList<>();
            SeedStream seeds = null;
            ArrayList<ArrayList<Range>> ranges = new ArrayList<>();

            int dim = -1;
            while (in.hasNextLine()) {
                String line = in.nextLine();

                if (line.strip().isEmpty()) {
                    // any new map starts next line
                    ++dim;
                } else if (line.startsWith("seeds:")) {
                    // seeds
                    String[] seedStrings = line.split(":")[1].strip().split(" ");

                    // dim should be negative 1
                    if (dim != -1)
                        throw new RuntimeException("dim != -1 at ln. 27");

                    for (String s : seedStrings)
                        seedData.add(Long.parseLong(s));
                    
                    long[] seedHelperArray = new long[seedData.size()];
                    for (int i = 0; i < seedHelperArray.length; ++i)
                        seedHelperArray[i] = seedData.get(i);
                    seeds = new SeedStream(seedHelperArray);

                } else if (line.matches("^[a-z-: ]+$")) {
                    // any new map
                    ranges.add(dim, new ArrayList<>());
                } else {
                    // line contains map data
                    String[] valueStrings = line.split(" ");
                    long[] vals = {
                        Long.parseLong(valueStrings[0]),
                        Long.parseLong(valueStrings[1]),
                        Long.parseLong(valueStrings[2])
                    };

                    ranges.get(dim).add(new Range(vals[0], vals[1], vals[2]));
                }
            }

            ArrayList<Long> destinations = new ArrayList<>();
            long nextDest = 0, value = -1;
            while (seeds.hasNext()) {
                dim = 0;
                nextDest = value = seeds.next();
                if (value % 1000000 == 0) System.out.println("calculating seed " + value);
                
                for (ArrayList<Range> al : ranges) {
                    ranges:for (Range r : al) {
                        if (r.inSourceRange(value)) {
                            value = nextDest = r.destination(value);
                            break ranges;
                        }
                    }
                }
                
                //System.out.println("Resolved location for seed " + l + ": " + nextDest);
                destinations.add(nextDest);
            }

            long min = -1;
            for (long de : destinations)
                if (de < min || min < 0)
                    min = de;

            System.out.println("Ergebnis f\u00fcr Teil 1: " + min);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file);
            e.printStackTrace(System.err);
            System.exit(-1);
        } catch (RuntimeException e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}

enum Maps
{
    SEEDS(-1), SEED_SOIL(0), SOIL_FERTIL(1), FERTIL_WATER(2), WATER_LIGHT(3), LIGHT_TEMP(4), TEMP_HUMID(5), HUMID_LOCATION(6);

    int id;
    
    Maps(int i)
    {
        this.id = i;
    }

    static Maps parseIndex(int i)
    {
        switch (i) {
            case -1: return SEEDS;
            case 0: return SEED_SOIL;
            case 1: return SOIL_FERTIL;
            case 2: return FERTIL_WATER;
            case 3: return WATER_LIGHT;
            case 4: return LIGHT_TEMP;
            case 5: return TEMP_HUMID;
            case 6: return HUMID_LOCATION;
        }
        return null;
    }
}

class Range
{
    long firstDest, firstSource, size;

    Range(long fd, long fs, long s)
    {
        this.firstDest = fd;
        this.firstSource = fs;
        this.size = Math.abs(s);
    }

    boolean inSourceRange(long i)
    {
        return (i >= firstSource && i < firstSource + size);
    }

    long destination(long i)
        throws IndexOutOfBoundsException
    {
        if (inSourceRange(i))
            return firstDest + Math.abs(i - firstSource);
        else throw new IndexOutOfBoundsException();
    }
}

class SeedRange
{
    long start, size;
    SeedRange(long st, long sz)
    {
        this.start = st;
        this.size = sz;
    }

    boolean inRange(long i)
    {
        return (i >= start && i < start + size);
    }
}

class SeedStream implements Iterator<Long>
{
    int mapIndex = 0;
    long index;
    long[] map;
    ArrayList<SeedRange> ranges;

    SeedStream(long[] in)
    {
        if (in.length < 2 || in.length % 2 != 0)
            throw new IllegalArgumentException("unsuitable input format");
        this.map = in;

        this.ranges = new ArrayList<>();
        for (int i = 1; i < map.length; i += 2) {
            this.ranges.add(new SeedRange(map[i-1], map[i]));
        }
        index = map[0];
    }

    @Override
    public boolean hasNext() {
        //throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
        if (ranges.get(mapIndex).inRange(index)) {
            return true;
        } else if (mapIndex + 1 < ranges.size()) {
            mapIndex++;
            index = ranges.get(mapIndex).start;
            return true;
        }
        return false;
    }

    @Override
    public Long next() {
        if (!hasNext())
            throw new UnsupportedOperationException("Unimplemented method 'next'");
        return index++;
    }
}