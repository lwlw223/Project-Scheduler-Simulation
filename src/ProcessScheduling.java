import net.datastructures.Entry;
import net.datastructures.HeapAdaptablePriorityQueue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ProcessScheduling {

    public static HeapAdaptablePriorityQueue<Integer, MyProcessObject> getAllProcesses()throws IOException{
        /**
         * Function: Get all processes from inputFile to D_pq
         * Input: None
         * Output: HeapAdaptablePriorityQueue-- D_pq
         */
        HeapAdaptablePriorityQueue<Integer, MyProcessObject> D_pq = new HeapAdaptablePriorityQueue<>();

        File inputFile = new File("src\\process_scheduling_input.txt");
        FileWriter outputFile = new FileWriter("src\\process_scheduling_output.txt");
        Scanner myScanner = new Scanner(inputFile);
        while (myScanner.hasNextLine()){
            String lineInput = myScanner.nextLine(); // id, priority, duration, arrival ---- 1 4 25 10
            String[] lineSplit = lineInput.split(" "); // Get string list from each line
            int id = Integer.parseInt(lineSplit[0]);
            int priority = Integer.parseInt(lineSplit[1]);
            int duration = Integer.parseInt(lineSplit[2]);
            int arrivalTime = Integer.parseInt(lineSplit[3]);
            MyProcessObject temp = new MyProcessObject(id, priority, duration, arrivalTime);
            D_pq.insert(temp.getArrival(),temp);
        }
        myScanner.close();
        return D_pq;
    }

    public static void printQueue(HeapAdaptablePriorityQueue<Integer, MyProcessObject> D_pq){
        /**
         * Function: Print all process existing in D_pq
         * Input: None
         * Output: None
         */
        for (Entry<Integer, MyProcessObject> value : D_pq) {
            System.out.println(value.getValue());
        }
    }

    public static void main(String[] args) throws IOException {
        HeapAdaptablePriorityQueue<Integer, MyProcessObject> COM_pq = new HeapAdaptablePriorityQueue<>(); // Dict of priority and ProcessObject
        HeapAdaptablePriorityQueue<Integer, MyProcessObject> D_pq = getAllProcesses(); // Dict of arr and ProcessObject
        Entry<Integer, MyProcessObject> prevMinPriorEntry = null;
        ArrayList waitTimeList = new ArrayList<>();
        FileWriter outputFile = new FileWriter("src\\process_scheduling_output.txt");

        int currtime = 0;
        int maxWaitTime = 30;

        System.out.println("\nAll processes received:"); printQueue(D_pq); outputFile.write("\nAll processes received:");
        for (Entry<Integer, MyProcessObject> value : D_pq) {  // write D_pq to outputFile
            outputFile.write("\n" + String.valueOf(value.getValue()));
        }

        System.out.println("Maximum wait time = "+ maxWaitTime + "\n"); outputFile.write("\n\nMaximum wait time = "+ maxWaitTime + "\n");
        while (true){
            if (!D_pq.isEmpty()){ // if D is not empty
                /*---------- Move process to COM_pq when time comes ----------*/
                if (D_pq.min().getKey() == currtime) {
                    Entry<Integer, MyProcessObject> minArrEntry = D_pq.min(); // Get process from D that has the earliest arrival time
                    MyProcessObject minArrObj = minArrEntry.getValue(); // Cast entry value to minArrObj
                    D_pq.remove(minArrEntry); // remove process from D
                    COM_pq.insert(minArrObj.getPriority(), minArrObj); // insert process to COM_pq
                }
            }

            if (!COM_pq.isEmpty()){ // if Q is not empty
                /*---------- Execute lowest priority process ----------*/
                Entry<Integer, MyProcessObject> minPriorEntry = COM_pq.min();  // // Find ProcessObj with the least priority
                MyProcessObject minPriorObj = minPriorEntry.getValue();  // Cast entry value to minPriorObj

                if (minPriorEntry != prevMinPriorEntry){ // If process is diff from prev process
                    System.out.println("\nNow running " + minPriorObj.toString_all() + "\n at time " + currtime);
                    outputFile.write("\nNow running " + minPriorObj.toString_all() + "\n at time " + currtime);
                }
                prevMinPriorEntry = minPriorEntry; // Set prevEntry to currEntry

                /*---------- Update runtime ----------*/
                minPriorObj.setRuntimeLeft ( minPriorObj.getRuntimeLeft() - 1 ); // Reduce runtime by 1
                COM_pq.replaceValue(minPriorEntry, minPriorObj); // Update runtime in COM_pq
                System.out.println("Executed process ID:" + minPriorObj.getId()+ ", at time " + currtime + " Remaining:" + minPriorObj.getRuntimeLeft());
                outputFile.write("\nExecuted process ID:" + minPriorObj.getId() + ", at time " + currtime + " Remaining:" + minPriorObj.getRuntimeLeft());

                /*---------- Remove if reach runtime == 0 ----------*/
                Entry<Integer, MyProcessObject> currEntry = COM_pq.min();
                MyProcessObject currProcess = currEntry.getValue();
                if (currProcess.getRuntimeLeft() == 0){
                    COM_pq.remove(currEntry);
                    System.out.println("Finished running "+ currProcess.toString_all() + "\n at time "+ currtime);
                    outputFile.write("\nFinished running "+ currProcess.toString_all() + "\n at time "+ currtime);
                    waitTimeList.add(currProcess.getTotalWaitTime());
                }

                /*---------- Update the wait times of all processes in Q ----------*/
                for (Entry<Integer, MyProcessObject> kv: COM_pq){
                    MyProcessObject kvObj = kv.getValue();
                    if (kvObj.getId() != currProcess.getId()){ // Add wait time to all entry in COM_pq
                        kvObj.setWaitTime(kvObj.getWaitTime()+1);
                        kvObj.setTotalWaitTime(kvObj.getTotalWaitTime()+1);
                    }

                    if (kvObj.getWaitTime() >= maxWaitTime){  // Update priorities of processes that have been waiting longer than max. wait time
                        kvObj.setWaitTime(0);
                        int currPrior = kvObj.getPriority();;
                        int newPrior = currPrior -1;
                        kvObj.setPriority(newPrior);
                        COM_pq.replaceKey(kv,newPrior);
                        System.out.println("Process "+ kvObj.getId()+ " reached maximum wait time... decreasing priority to "+ kvObj.getPriority());
                        outputFile.write("\nProcess "+ kvObj.getId()+ " reached maximum wait time... decreasing priority to "+ kvObj.getPriority());
                    }
                }
            }

            if (D_pq.isEmpty() && COM_pq.isEmpty()){ break;}
            currtime++; // increment time in each iteration
        }

        System.out.println("Finished running all processes at time " + currtime); outputFile.write("\nFinished running all processes at time " + currtime);

        /*---------- Calculate avg wait time ----------*/
        float sum = 0.00F;
        float avg = 0.00F;
        for (int i = 0; i < waitTimeList.size();i++){
            sum = sum + (int) waitTimeList.get(i);
            avg = sum / waitTimeList.size();
        }
        System.out.println("Average wait time: " + avg ); outputFile.write("\nAverage wait time: " + avg );
        outputFile.close();
    }
}


