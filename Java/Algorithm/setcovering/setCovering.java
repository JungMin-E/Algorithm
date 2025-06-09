package setcovering;

import java.util.*;


//Set Covering 문제를 그리디 알고리즘으로 해결하는 클래스
public class setCovering {
    
    
    //기본 그리디 알고리즘 - 커버되는 원소 수 기준으로 부분집합 선택
    public static List<Integer> greedySetCovering(Set<Integer> universe, List<Set<Integer>> subsets) {
        List<Integer> result = new ArrayList<>();  //선택된 부분집합 인덱스
        Set<Integer> uncovered = new HashSet<>(universe);  //미커버 원소
        
        //모든 원소가 커버될 때까지 반복
        while (!uncovered.isEmpty()) {
            int bestIndex = -1;
            int maxCovered = 0;
            
            //가장 많은 커버 되지 않은 원소를 포함하는 부분집합 선택
            for (int i = 0; i < subsets.size(); i++) {
                if (result.contains(i)) continue;  //이미 선택된 집합은 넘기기
                
                //현재 부분집합이 커버할 수 있는 추가 원소 수 계산
                Set<Integer> intersection = new HashSet<>(subsets.get(i));
                intersection.retainAll(uncovered);
                
                if (intersection.size() > maxCovered) {
                    maxCovered = intersection.size();
                    bestIndex = i;
                }
            }
            
            if (bestIndex == -1) break;  //더 이상 커버 x
            
            result.add(bestIndex);  //최선의 부분집합 선택
            uncovered.removeAll(subsets.get(bestIndex));  //커버된 원소 제거
        }
        
        return result;
    }
    
    //비용 효율적 그리디 알고리즘 - 단위 비용당 커버되는 원소 수 기준으로 선택
    public static List<Integer> costEfficientGreedy(Set<Integer> universe, List<Set<Integer>> subsets, int[] costs) {
        List<Integer> result = new ArrayList<>();  //선택된 부분집합 인덱스
        Set<Integer> uncovered = new HashSet<>(universe);  //미커버 원소
        
        //모든 원소가 커버될 때까지 반복
        while (!uncovered.isEmpty()) {
            int bestIndex = -1;
            double bestEfficiency = 0;
            
            //가장 비용 효율적인 부분집합 선택 (커버 원소 수 / 비용)
            for (int i = 0; i < subsets.size(); i++) {
                if (result.contains(i)) continue;  //이미 선택된 집합은 넘어가기
                
                //현재 부분집합이 커버할 수 있는 추가 원소 수 계산
                Set<Integer> intersection = new HashSet<>(subsets.get(i));
                intersection.retainAll(uncovered);
                
                if (!intersection.isEmpty()) {
                    double efficiency = (double) intersection.size() / costs[i];
                    if (efficiency > bestEfficiency) {
                        bestEfficiency = efficiency;
                        bestIndex = i;
                    }
                }
            }
            
            if (bestIndex == -1) break;  //더 이상 커버 x
            
            result.add(bestIndex);  //최선의 부분집합 선택
            uncovered.removeAll(subsets.get(bestIndex));  //커버된 원소 제거
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        //문제 설정
        Set<Integer> universe = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        
        List<Set<Integer>> subsets = new ArrayList<>();
        subsets.add(new HashSet<>(Arrays.asList(1, 2, 3, 8)));           //S1
        subsets.add(new HashSet<>(Arrays.asList(1, 2, 3, 4, 8)));        //S2
        subsets.add(new HashSet<>(Arrays.asList(1, 2, 3, 4)));           //S3
        subsets.add(new HashSet<>(Arrays.asList(2, 3, 4, 5, 7, 8)));     //S4
        subsets.add(new HashSet<>(Arrays.asList(4, 5, 6, 7)));           //S5
        subsets.add(new HashSet<>(Arrays.asList(5, 6, 7, 9, 10)));       //S6
        subsets.add(new HashSet<>(Arrays.asList(4, 5, 6, 7)));           //S7
        subsets.add(new HashSet<>(Arrays.asList(1, 2, 4, 8)));           //S8
        subsets.add(new HashSet<>(Arrays.asList(6, 9)));                 //S9
        subsets.add(new HashSet<>(Arrays.asList(6, 10)));                //S10
        
        int[] costs = {6, 10, 4, 12, 4, 8, 4, 4, 3, 4};
        
        //1. 기본 그리디 알고리즘 실행
        System.out.println("1. 위의 예제를 입력으로 하는 Set Covering Algorithm:");
        List<Integer> result1 = greedySetCovering(universe, subsets);
        
        for (int i : result1) {
            System.out.println("S" + (i + 1) + ": " + subsets.get(i));
        }
        
        int cost1 = 0;
        for (int i : result1) cost1 += costs[i];
        System.out.println("선택된 집합 수: " + result1.size() + ", 총 비용: " + cost1);
        
        //2. 비용 효율적 그리디 알고리즘 실행
        System.out.println("\n2. 최소한의 비용으로 전체 U를 커버하는 조합을 찾는 문제:");
        List<Integer> result2 = costEfficientGreedy(universe, subsets, costs);
        
        for (int i : result2) {
            System.out.println("S" + (i + 1) + ": " + subsets.get(i) + " => Cost " + costs[i]);
        }
        
        int cost2 = 0;
        for (int i : result2) cost2 += costs[i];
        System.out.println("선택된 집합 수: " + result2.size() + ", 총 비용: " + cost2);
        
    }
}
