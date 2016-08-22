package santaElf;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class SimpleElf extends Thread {

    Clock clock = Clock.getInstance();

    Sleigh buf;

    String id;
    int number;
    int ticksWaiting = 0;
    int toyCount = 0;

    int hourlyCount = 0;
    int hourlyTicks = 0;
    int reportsMade = 0;

    private final String[] toys = {"train", "doll", "dinosaur", "whistle", "fake tattoo", "bracelet"};
    private final int RANDOMMULTIPLIER = 200;

    PrintWriter writer;

    public SimpleElf(String id, Sleigh b) {
        this.id = id;
        buf = b;

        try {
            writer = new PrintWriter("Elf-" + id + "-log.txt", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println("Error in writing to file; elf" + id);
        }
    }

    private void produce(Gift g) {
        buf.insert(g);
    }

    @Override
    public void run() {
        while (!clock.isStopped()) {
            report();

            try { //Picking Toy
                sleep((int) (Math.random() * RANDOMMULTIPLIER));
            } catch (InterruptedException ex) {
            }
            report();

            String toy = toys[(int) (Math.random() * toys.length)];

            boolean forMale;
            forMale = Math.random() < 0.5;
            
            writer.println(clock.toString() + "Elf " + id + ":      Selected toy " + toy + ", " +(forMale?"boy":"girl"));

            try { //Wrapping Toy
                sleep((int) (Math.random() * RANDOMMULTIPLIER));
            } catch (InterruptedException ex) {
            }
            report();
                        
            Gift g = new Gift(toy, forMale);

            writer.println(clock.toString() + "Elf " + id + ":      Wrapped toy " + toy + " in "+ (forMale?"blue":"pink") +" wrapping paper");

            boolean isFull = buf.isFull();
            int diff = 0;
            if (isFull) {
                diff = clock.getTick();
            }

            produce(g);

            if (isFull) {
                diff = (clock.getTick() - diff);
                ticksWaiting += diff;
                hourlyTicks += diff;
            }

            writer.println(clock.toString() + "Elf " + id + ":      Placed toy " + toy + " on sleigh");
            toyCount++;
            hourlyCount++;
            report();

        }
        writeReport();
        writer.close();
    }

    public void output() {
        System.out.println("Elf " + id + ":       Number of gifts wrapped: " + toyCount);
        System.out.println("Elf " + id + ":       Time Spent waiting: " + ticksWaiting);
    }

    public void report() {
        if (clock.getTick() > 60 * (reportsMade + 1)) {

            writeReport();
            
            hourlyTicks = 0;
            hourlyCount = 0;
            reportsMade++;
        }
    }

    private void writeReport() {
        writer.println();
        writer.println("---Hourly Report---");
        writer.println("Elf " + id + ":       Number of gifts wrapped this hour: " + hourlyCount);
        writer.println("Elf " + id + ":       Time Spent waiting: " + hourlyTicks);
        writer.println("-------------------");
        writer.println();
    }
}
