package me.devcode.utilities.mysql;

public class TimeMeter {

    /*
    Testing MySQL Async
     */
	 
    private final String name;
    private long start;
    private long last;
 
    public TimeMeter(String name) {
        long now = System.currentTimeMillis();
        start = now;
        last = now;
        this.name = name;
        System.out.println("TimeMeter " + name);
    }
 
    public void test(Object o) {
        long now = System.currentTimeMillis();
        System.out.println("TimeMeter " + name + " " + o + " " + (now - last) + " / " + (now - start));
        last = now;
    }
}
