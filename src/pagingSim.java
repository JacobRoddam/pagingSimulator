import java.util.*;


public class pagingSim {

    private int memorySize;
    private int pageSize;
    private int jobs;
    private int minRun;
    private int maxRun;
    private int minMem;
    private int maxMem;
    private Queue<Job> jobQueue;

    public pagingSim(int memorySize, int pageSize, int jobs, int minRun, int maxRun, int minMem, int maxMem) {
        this.memorySize = memorySize;
        this.pageSize = pageSize;
        this.jobs = jobs;
        this.minRun = minRun;
        this.maxRun = maxRun;
        this.minMem = minMem;
        this.maxMem = maxMem;
        this.jobQueue = new LinkedList<Job>();
    }

    public void createJobQueue(){
        Random random = new Random();

        int jobNum;
        int jobRuntime;
        int jobMemory;

        for (int i = 1; i <= jobs; i++){
            jobNum = i;
            jobRuntime = random.nextInt(maxRun - minRun) + minRun;
            jobMemory = random.nextInt(maxMem - minMem) + minMem;
            System.out.println("      " + jobNum + "      " + jobRuntime + "      " + jobMemory);
            Job newJob = new Job(jobNum, jobRuntime, jobMemory);
            jobQueue.add(newJob);
        }

    }

    public static void assignPages(ArrayList<Integer> pageTable, int jobNum, int numPages ){
        int firstFree = pageTable.indexOf(0);
        while ( (firstFree < pageTable.size()) && (firstFree != -1) && (numPages > 0) ){
            pageTable.set(firstFree, jobNum);
            numPages--;
            firstFree = pageTable.indexOf(0);
        }
    }

    public static void clearPages(ArrayList<Integer> pageTable, int jobNum) {
        int firstJobPage = pageTable.indexOf(jobNum);
        while ( (firstJobPage < pageTable.size()) && (firstJobPage != -1)){
            pageTable.set(firstJobPage, 0);

            firstJobPage = pageTable.indexOf(jobNum);
        }
    }

    public static void printPageTable(ArrayList<Integer> pageTable){
        int numPages = pageTable.size();
        int numLines;
        if (numPages % 16 == 0){
            numLines = numPages / 16;
        }
        else{
            numLines = (numPages / 16) + 1;
        }

        for (int line = 0; line < numLines; line++){
            int j;
            for (j = 0; j < 16 ; j++ ){
                int ind = (line * 16) + j;
                if (ind < pageTable.size()){
                    System.out.print(pageTable.get(ind) + " ");
                }
                else{
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }

    public Job dequeue(){
        return jobQueue.poll();
    }

    public void enqueue(Job job){
        jobQueue.add(job);
    }



    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getJobs() {
        return jobs;
    }

    public void setJobs(int jobs) {
        this.jobs = jobs;
    }

    public int getMinRun() {
        return minRun;
    }

    public void setMinRun(int minRun) {
        this.minRun = minRun;
    }

    public int getMaxRun() {
        return maxRun;
    }

    public void setMaxRun(int maxRun) {
        this.maxRun = maxRun;
    }

    public int getMinMem() {
        return minMem;
    }

    public void setMinMem(int minMem) {
        this.minMem = minMem;
    }

    public int getMaxMem() {
        return maxMem;
    }

    public void setMaxMem(int maxMem) {
        this.maxMem = maxMem;
    }

    public Queue<Job> getJobQueue(){
        return jobQueue;
    }

    public static void main(String[] args){
        int arg0 = Integer.parseInt(args[0]);
        int arg1 = Integer.parseInt(args[1]);
        int arg2 = Integer.parseInt(args[2]);
        int arg3 = Integer.parseInt(args[3]);
        int arg4 = Integer.parseInt(args[4]);
        int arg5 = Integer.parseInt(args[5]);
        int arg6 = Integer.parseInt(args[6]);

        // Initialize simulator
        pagingSim simulator = new pagingSim(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        //pagingSim simulator = new pagingSim(48000, 1000, 3, 2, 15, 5000, 25000);
        System.out.println("Simulator Parameters");
        System.out.println("  Memory Size: " + simulator.getMemorySize());
        System.out.println("  Page Size: " + simulator.getPageSize());
        System.out.println("  Number of Jobs: " + simulator.getJobs());
        System.out.println("  Min-Max Runtime: " + simulator.getMinRun() + "-" + simulator.getMaxRun());
        System.out.println("  Min-Max Memory: " + simulator.getMinMem() + "-" + simulator.getMaxMem());

        // Create Job Queue
        System.out.println();
        System.out.println("Job Queue:");
        System.out.println("  Job #   Runtime   Memory");
        simulator.createJobQueue();

        System.out.println();
        System.out.println("Starting Simulator");

        // initialize empty page table
        int pagesAvailable = simulator.getMemorySize() / simulator.getPageSize();
        ArrayList<Integer> pageTable = new ArrayList<Integer>();
        for (int i = 0; i < pagesAvailable; i++){
            pageTable.add(0);
        }

        // initialize arrays to hold start time, end time for jobs
        int[] startTimes = new int[simulator.getJobs()];
        int[] endTimes = new int[simulator.getJobs()];

        // initialize condition for loop, counters for completed jobs and timestep
        boolean allJobsComplete = false;
        int jobsComplete = 0;
        int timeStep = 1;

        while (!allJobsComplete){
            System.out.println("Time Step " + timeStep + ":");
            boolean enoughPagesForJob = true;
            int jobsChecked = 0;
            int jobsLeft = simulator.getJobs();

            // loop for scheduling jobs
            while(enoughPagesForJob && jobsChecked < simulator.getJobs() - jobsComplete){



                Job job = simulator.dequeue();
                //System.out.println("  *Checking to schedule job " + job.getJobNum());
                int numPagesForJob = job.getMemory() / simulator.getPageSize();

                // schedule job
                if ( !job.hasStarted() ){
                    if ((numPagesForJob <= pagesAvailable) ){
                        job.startJob();
                        startTimes[job.getJobNum() - 1] = timeStep;
                        assignPages(pageTable, job.getJobNum(), numPagesForJob);
                        pagesAvailable -= numPagesForJob;
                        System.out.println("  Job " + job.getJobNum() + " Starting");
                    }
                    else {
                        enoughPagesForJob = false;
                    }
                }
                simulator.enqueue(job);
                jobsChecked++;
            }

            
            // Run a job
            Job job = simulator.dequeue();


            while (!job.hasStarted()) {

                simulator.enqueue(job);
                simulator.dequeue();
            }


            if (!job.hasFinished()){
                job.runJob();
                System.out.println("  Job " + job.getJobNum() + " Running");
                if (job.hasFinished()){
                    System.out.println("  Job " + job.getJobNum() + " Completed");
                    clearPages(pageTable, job.getJobNum());
                    endTimes[job.getJobNum() - 1] = timeStep;
                    System.out.println("  Job " + job.getJobNum() + " Finished at time step " + timeStep);
                    jobsComplete++;
                    allJobsComplete = jobsComplete >= simulator.getJobs();
                }
                else{
                    simulator.enqueue(job);
                }
            }
            else {
                allJobsComplete = true;
            }


            //print page table
            printPageTable(pageTable);



            timeStep++;

            System.out.println();

        }


        System.out.println("All jobs completed");
        System.out.println();
        System.out.println("Job Information: ");
        System.out.println("  Job #   Start Time   EndTime");
        for (int i = 0; i < simulator.getJobs(); i++){
            System.out.print("      " + (i + 1) + "      " + startTimes[i] + "      " + endTimes[i]);
            System.out.println();
        }



        System.out.println();
        System.out.println("Simulation Complete");





    }
}
