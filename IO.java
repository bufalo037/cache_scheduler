/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package scheduler;

import java.io.*;

/**
 *
 * @author mihai
 */
public class IO {
    
    final private BufferedWriter out;
    final private BufferedReader in;

    /**
     *
     * @param in BufferedReader pt citire de input
     * @param out BufferedWriter pt scriere in fisier
     * @throws FileNotFoundException
     * @throws IOException
     */
    public IO(String in, String out) throws FileNotFoundException, IOException {
        
        FileReader file = new FileReader(in);
        this.in = new BufferedReader(file);
        this.out = new BufferedWriter(new FileWriter(out));
    }
    
    /**
     *
     * @param name tipul de cache ca string
     * @param nrline numarul de linii
     * @return 
     */
    static public Cache createCache(String name,int nrline){
        switch(name){
            case "LruCache" :
            {
                return new LruCache(nrline);
            }
            case "LfuCache" :
            {
                return new LfuCache(nrline);
            }
            default:
            {
                return null;
            }
        }
    }
    
    /**
     * Se creaza scheduler-ul
     * @param schedname numele scheduler-ului
     * @param CacheT tipul cache-ului
     * @param NrOfEvents numarul de procese diferite
     * @param process numele proceselor diferite 
     * @param weight greutatea pe care o are fiecare proces in parte
     * @param nrProc numarul efectiv de procese carora li se vor atribui valori, adica numrul de numere citit din fisier
     * @param num numerele mentionate de nrProc
     * @param out BufferedWriter
     * @return
     */
    static public Scheduler createScheduler(String schedname, Cache CacheT,
            int NrOfEvents, String[] process,int[] weight,int nrProc,
            int[] num,BufferedWriter out){
        switch(schedname){
            case "RandomScheduler":
            {
                return new RandomScheduler(CacheT, NrOfEvents, process,
                        weight, nrProc, num, out);
            }
            case "RoundRobinScheduler":
            {
                return new RoundRobinScheduler(CacheT, NrOfEvents, process,
                        weight, nrProc, num, out );
            }
            case "WeightedScheduler":
            {
                return new WeightedScheduler(CacheT, NrOfEvents, process,
                        weight, nrProc, num, out);
            }    
        }
        System.out.println("Eroare citire type scheduler, aveti grije");
        return null;
        
    }
    
    /**
     *
     * @param args calea catre fisierele de intrare si de iesire
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException{
        
        System.out.println(args[0] + " " + args[1]);
        IO io = new IO(args[0], args[1]);
        int CacheLines;
        String aux;
        String aux2[];
        String CacheType;
        int NumberOfEvents;
        String SchedulerType;
        String[] Process;
        int[] Weight;
        int numberofnumbers;
        int[] numbers;
        int counter;
        
        Cache CacheT;
        Scheduler sched;

        //incepe citirea din fisier
        
        CacheType = io.in.readLine();
        
        aux = io.in.readLine();
        CacheLines = Integer.decode(aux);
        
        SchedulerType = io.in.readLine();
        
        aux = io.in.readLine();
        NumberOfEvents = Integer.decode(aux);
        
        Process = new String[NumberOfEvents];
        
        Weight = new int[NumberOfEvents];
        
        counter = NumberOfEvents;
        
        for(;counter > 0; counter--){
            aux = io.in.readLine();
            aux2 = aux.split(" ");
            Process[NumberOfEvents - counter] = aux2[0];
            Weight[NumberOfEvents - counter] = Integer.decode(aux2[1]);   
        }
        
        aux = io.in.readLine();
        numberofnumbers = Integer.decode(aux);
        
        counter = numberofnumbers;
        numbers = new int[numberofnumbers];
        
        for(;counter > 0;counter--){
            aux = io.in.readLine();
            numbers[numberofnumbers - counter]= Integer.decode(aux);
        }
        
        //se termina citirea din fisier
        //incepe crearea cache-ului daca se poate si a scheduler-ului
        
        CacheT = createCache(CacheType,CacheLines);
        sched = createScheduler(SchedulerType, CacheT, NumberOfEvents, Process,
                Weight, numberofnumbers, numbers,io.out);
        sched.run();
        
        io.in.close();
        io.out.close();
    }
}
