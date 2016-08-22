package santaElf;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleSanta extends Thread {

    Clock clock = Clock.getInstance();

    Sleigh buf;
    String id;

    List<Gift> sack;

    private final int RANDOMMULTIPLIER = 200;
    private final int SANTASACKSIZE = 10;

    int toyCount = 0;
    
    int ticksWaiting = 0;
    int reportsMade = 0;

    int hourlyTicks = 0;
    int hourlyCount = 0;

    PrintWriter writer;

    public SimpleSanta(String id, Sleigh b) {
        this.sack = new ArrayList<>();
        buf = b;
        this.id = id;

        try {
            writer = new PrintWriter("Santa-" + id + "-log.txt", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println("Error in writing to file; elf" + id);
        }
    }

    public Gift consume() {
        Gift nextItem = buf.extract();
        return nextItem;
    }

    private boolean isSackEmpty() { //Use this later to check gender etc..
        return sack.isEmpty();
    }

    @Override
    public void run() {
        while (!clock.isStopped()) {
            report();
            while(sack.size() < SANTASACKSIZE) {

                boolean isEmpty = buf.isEmpty();
                int diff = 0;
                if (isEmpty) {
                    diff = clock.getTick();
                }

                Gift g = consume();

                if (isEmpty) {
                    diff = (clock.getTick() - diff);
                    ticksWaiting += diff;
                    hourlyTicks += diff;
                }

                writer.println(clock.toString() + "Santa " + id + ":    Took toy " + g + "from sleigh.");
                sack.add(g);
                
                if(sack.size() >= 6 && buf.isEmpty()){
                    break;
                }
            }

            try { //Walks back;
                sleep((int) (Math.random() * RANDOMMULTIPLIER));
            } catch (InterruptedException ex) {
            }
            report();

            writer.println(clock.toString() + "Santa " + id + ":    Finished walking back");

            for (Iterator<Gift> i = sack.iterator(); i.hasNext() && !clock.isStopped();) {
                try { //Random time with child.
                    sleep((int) (Math.random() * RANDOMMULTIPLIER));
                } catch (InterruptedException ex) {
                }

                report();

                Gift g = i.next();

                writer.println(clock.toString() + "Santa " + id + ":    Giving toy" + g);
                i.remove();
                toyCount++;
                hourlyCount++;
            }
        }
        writeReport();
        writer.close();
    }

    public void report() {
        if (clock.getTick() > 60 * (reportsMade + 1)) {

            writeReport();

            hourlyTicks = 0;
            hourlyCount = 0;
            reportsMade++;
        }
    }

    public void output() {
        System.out.println("Santa " + id + ":       Number of gifts gave: " + toyCount);
        System.out.println("Santa " + id + ":       Time Spent waiting: " + ticksWaiting);
        System.out.println("Santa " + id + ":       Toys left in bag: " + sack.size());
    }

    private void writeReport() {
        writer.println();
        writer.println("---Hourly Report---");
        writer.println("Santa " + id + ":       Number of gifts given this hour: " + hourlyCount);
        writer.println("Santa " + id + ":       Time Spent waiting this hour: " + hourlyTicks);
        writer.println("-------------------");
        writer.println();
    }
}
