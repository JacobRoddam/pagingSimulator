
public class Job {
    int jobNum;
    int runTime;
    int memory;
    boolean started;


    public Job(int jobNum, int runTime, int memory) {
        this.jobNum = jobNum;
        this.runTime = runTime;
        this.memory = memory;
        this.started = false;

    }

    public int getJobNum() {
        return jobNum;
    }

    public void setJobNum(int jobNum) {
        this.jobNum = jobNum;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public boolean hasFinished(){
         return (getRunTime() == 0);
    }

    public void runJob(){
        setRunTime(runTime - 1);
    }

    public boolean hasStarted(){
        return started;
    }

    public void startJob(){
        this.started = true;
    }







}
