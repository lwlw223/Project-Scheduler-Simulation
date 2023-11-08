
public class MyProcessObject {
    private int id;
    private int priority;
    private int duration;
    private int arrival;

    private int waitTime;
    private int totalWaitTime;
    private int runtimeLeft;

    public MyProcessObject(int id, int priority, int duration, int arrival){
        this.id = id;
        this.priority = priority;
        this.duration = duration;
        this.arrival = arrival;
        this.waitTime = 0;
        this.totalWaitTime = 0;
        this.runtimeLeft = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getArrival() {
        return arrival;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public int getWaitTime() {return waitTime;}

    public void setWaitTime(int waitTime) {this.waitTime = waitTime;}

    public int getRuntimeLeft() {return runtimeLeft; }

    public void setRuntimeLeft(int runtimeLeft) { this.runtimeLeft = runtimeLeft;}

    public int getTotalWaitTime() {return totalWaitTime;}

    public void setTotalWaitTime(int totalWaitTime) {this.totalWaitTime = totalWaitTime;}

    public String toString(){
        String c =
                "\tid = " + id +
                        "\tpriority = " + priority +
                        "\tduration = " + duration +
                        "\tarrival time = " + arrival
                ;
        return c;
    }

    public String waitTimeToString(){
        String c = "ID: " + id + " ---Wait time: " + waitTime;
        return c;
    }

    public String toString_all(){
        String c = "Process id = " + id
                + "\nArrival = " + arrival
                + "\nDuration = " + duration
                + "\nRun time left = " + runtimeLeft;
        return c;
    }
}
