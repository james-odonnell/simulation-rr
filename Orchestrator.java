package com.aaidevstudio.RRTerminal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class Orchestrator {
    private ArrayList<Job> sysJobList;
    private Queue<Job> processingQueue;
    private ArrayList<Double> timeInQueue = new ArrayList<>();
    private double quantum;
    private int jobsConsumed;

    public Orchestrator() {
        this.sysJobList = new ArrayList<Job>();
        this.quantum = 5;
        this.processingQueue = new LinkedList<>();
    }

    public Orchestrator(Terminal... terminals) {
        this.sysJobList = new ArrayList<Job>();
        this.quantum = 5;
        this.processingQueue = new LinkedList<>();

        for (Terminal terminal : terminals) {
            sysJobList.addAll(terminal.getJobQueue());
        }

        Collections.sort(sysJobList);
    }

    public Orchestrator(double quantum, Terminal... terminals) {
        this.sysJobList = new ArrayList<Job>();
        this.quantum = quantum;
        this.processingQueue = new LinkedList<>();

        for (Terminal terminal : terminals) {
            sysJobList.addAll(terminal.getJobQueue());
        }

        Collections.sort(sysJobList);
    }

    public Orchestrator(ArrayList<Terminal> terminals) {
        sysJobList = new ArrayList<Job>();
        this.quantum = 5;
        this.processingQueue = new LinkedList<>();

        for (Terminal terminal : terminals) {
            sysJobList.addAll(terminal.getJobQueue());
        }

        Collections.sort(sysJobList);
    }

    public Orchestrator(double quantum, ArrayList<Terminal> terminals) {
        this.sysJobList = new ArrayList<Job>();
        this.quantum = quantum;
        this.processingQueue = new LinkedList<>();

        for (Terminal terminal : terminals) {
            sysJobList.addAll(terminal.getJobQueue());
        }

        Collections.sort(sysJobList);
    }

    public void consumeJobs() {
        double timeElapsed = 0;
        boolean stillProcessing = true;
        double nextJobArrival = 0.0;
        int pos = 0;
        int counter = 0;

        while(stillProcessing) {

            System.out.println("Iteration " + counter + " Time Elapsed: " + timeElapsed + " Jobs Consumed " + jobsConsumed);

            System.out.println("At the beginning of this iteration: ");
            this.printProccessingQueue();

            while(timeElapsed>=nextJobArrival && pos < sysJobList.size()){
                processingQueue.add(sysJobList.get(pos));
                pos++;
                if(pos >= sysJobList.size()) { break; }
                nextJobArrival = sysJobList.get(pos).getArrivalTime();
            }



            if(quantum >= processingQueue.peek().getRemaining()) {
                double arrival = processingQueue.peek().getArrivalTime();
                timeElapsed += processingQueue.poll().getRemaining();

                System.out.println(arrival);
                System.out.println(timeElapsed);

                timeInQueue.add(timeElapsed - arrival);

                jobsConsumed ++;
            } else {
                timeElapsed += quantum;
                processingQueue.peek().processes(quantum);
                processingQueue.add(processingQueue.poll());
            }

            System.out.println("At the end of this iteration: ");
            System.out.println("Time Elapsed: " + timeElapsed + " Jobs Consumed " + jobsConsumed);

            if(pos >= sysJobList.size() && processingQueue.isEmpty()) {
                stillProcessing = false;
                System.out.println("All Jobs Processed");
            } else {
                this.printProccessingQueue();
            }
            counter++;
        }
    }

    public void printElem() {
        System.out.println("The prospective jobs are as follows: \n\n\n");
        sysJobList.forEach(x -> {
            System.out.println(x);
        });
        System.out.println("\n\n\n");
    }

    public  void printProccessingQueue() {
        System.out.println("At this stage the processing queue is as follows: \n\n\n");
        processingQueue.forEach(x -> {
            System.out.println(x);
        });
        System.out.println("\n\n\n");
    }

    public void printTimeInQueueStats() {
        double median = 0;
        double total = 0;
        for (int i = 0; i < sysJobList.size(); i++) {
            Job curr = sysJobList.get(i);
            System.out.println("{Job ID: " + curr.getJid() + ", Job length: " + curr.getLength() + ", Time to Complete: " + timeInQueue.get(i).toString() + " }" );

            total += timeInQueue.get(i);
        }

        Collections.sort(timeInQueue);
        median = timeInQueue.get(timeInQueue.size()/2);

        System.out.println("Median time spent in Queue: " + median);
        System.out.println("Average time spent in Queue: " + total/sysJobList.size());
    }

}
