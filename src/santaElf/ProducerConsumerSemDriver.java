package santaElf;

public class ProducerConsumerSemDriver {

    public ProducerConsumerSemDriver() {
    }

    public static void main(String[] args) {
        
        System.out.println("Starting Up");
        
        Clock clock = Clock.getInstance();

        Sleigh b = new Sleigh(120); //Constructor passes in the max number of gifts in the sleigh 

        SimpleSanta c = new SimpleSanta("Raj", b);
        SimpleSanta c2 = new SimpleSanta("Li", b);
        SimpleSanta c3 = new SimpleSanta("Gareth", b);
        
        SimpleElf p = new SimpleElf("Nick", b);
        SimpleElf p2 = new SimpleElf("Chris", b);
        SimpleElf p3 = new SimpleElf("Leslie", b);
        SimpleElf p4 = new SimpleElf("Niki", b);
        SimpleElf p5 = new SimpleElf("Laurent", b);

        c.start();
        c2.start();
        c3.start();
        
        p.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
        
        clock.start();

        System.out.println("Running");

        try {
            p.join();
            p2.join();
            p3.join();
            p4.join();
            p5.join();
            
            c.join();
            c2.join();
            c3.join();
        } catch (InterruptedException ex) {

        }
        
        p.output();
        p2.output();
        p3.output();
        p4.output();
        p5.output();
        
        c.output();
        c2.output();
        c3.output();
        
        System.out.println("Toys left in sleigh: " + b.numofToys());
        System.out.println("Stopped");
    }
}
