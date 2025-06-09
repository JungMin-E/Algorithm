package taskscheduling;

import java.util.*;

public class taskScheduling {
    
    static class Machine {
        int id;
        List<Integer> tasks;
        int totalTime;
        
        public Machine(int id) {
            this.id = id;
            this.tasks = new ArrayList<>();
            this.totalTime = 0;
        }
        
        public void addTask(int taskTime) {
            tasks.add(taskTime);
            totalTime += taskTime;
        }
        
        @Override
        public String toString() {
            return "머신" + (id + 1) + ": " + tasks.toString().replace("[", "").replace("]", "");
        }
    }
    
    static class SchedulingResult {
        Machine[] machines;
        int makespan;  //최종 종료시간
        String method;
        
        public SchedulingResult(Machine[] machines, String method) {
            this.machines = machines;
            this.method = method;
            this.makespan = Arrays.stream(machines).mapToInt(m -> m.totalTime).max().orElse(0);
        }
        
        public void printResult() {
            System.out.println("" + method + " 결과");
            for (Machine machine : machines) {
                System.out.println(machine);
            }
            System.out.println("종료시간: " + makespan);
            System.out.println();
        }
        
        public double getApproximationRatio(int optimalTime) {
            return (double) makespan / optimalTime;
        }
    }
    
    //LPT 알고리즘 구현
    public static SchedulingResult scheduleLPT(int[] tasks, int numMachines) {
        Machine[] machines = new Machine[numMachines];
        for (int i = 0; i < numMachines; i++) {
            machines[i] = new Machine(i);
        }
        
        //작업을 내림차순으로 정렬
        Integer[] sortedTasks = new Integer[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            sortedTasks[i] = tasks[i];
        }
        Arrays.sort(sortedTasks, Collections.reverseOrder());
        
        //각 작업을 가장 여유있는 머신에 할당
        for (int task : sortedTasks) {
            //현재 가장 적게 할당된 머신 찾기
            Machine minMachine = machines[0];
            for (int i = 1; i < machines.length; i++) {
                if (machines[i].totalTime < minMachine.totalTime) {
                    minMachine = machines[i];
                }
            }
            minMachine.addTask(task);
        }
        
        return new SchedulingResult(machines, "LPT (Longest Processing Time first)");
    }
    
    //List Scheduling 알고리즘 구현
    public static SchedulingResult scheduleList(int[] tasks, int numMachines) {
        Machine[] machines = new Machine[numMachines];
        for (int i = 0; i < numMachines; i++) {
            machines[i] = new Machine(i);
        }
        
        //주어진 순서대로 각 작업을 가장 여유있는 머신에 할당
        for (int task : tasks) {
            //현재 가장 적게 할당된 머신 찾기
            Machine minMachine = machines[0];
            for (int i = 1; i < machines.length; i++) {
                if (machines[i].totalTime < minMachine.totalTime) {
                    minMachine = machines[i];
                }
            }
            minMachine.addTask(task);
        }
        
        return new SchedulingResult(machines, "List Scheduling (순서대로)");
    }
    
    public static int calculateLowerBound(int[] tasks, int numMachines) {
        int totalTime = Arrays.stream(tasks).sum();
        int maxTask = Arrays.stream(tasks).max().orElse(0);
        
        return Math.max((int) Math.ceil((double) totalTime / numMachines), maxTask);
    }
    
    public static void main(String[] args) {
    
        int[] tasks = {2, 5, 1, 7, 3, 4};  //작업시간
        int numMachines = 3;  //머신 수
        
        System.out.println("Task Scheduling 문제");
        System.out.println("작업 수 n = " + tasks.length + " (작업시간: " + Arrays.toString(tasks) + ")");
        System.out.println("머신 수 m = " + numMachines);
        System.out.println();
        
        //각 방식으로 스케줄링 수행
        SchedulingResult lptResult = scheduleLPT(tasks, numMachines);
        SchedulingResult listResult = scheduleList(tasks, numMachines);
        
        //결과 출력
        lptResult.printResult();
        listResult.printResult();
        
        //오차율(approximation ratio) 계산
        int lowerBound = calculateLowerBound(tasks, numMachines);
        
        System.out.println("오차율 (Approximation Ratio)");
        System.out.println();
        
        double lptRatio = lptResult.getApproximationRatio(lowerBound);
        double listRatio = listResult.getApproximationRatio(lowerBound);
        
        System.out.printf("LPT 오차율: %.3f\n", lptRatio);
        System.out.printf("List Scheduling 오차율: %.3f\n", listRatio);
        System.out.println();
        
    }
}
