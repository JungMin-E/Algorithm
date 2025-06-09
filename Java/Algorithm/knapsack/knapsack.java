package knapsack;

public class knapsack {
    
    //아이템 클래스
    static class Item {
        int weight;
        int value;
        double ratio; //가치/무게 비율
        
        public Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
            this.ratio = (double) value / weight;
        }
        
        @Override
        public String toString() {
            return String.format("무게: %dkg, 가치: %d만원, 비율: %.2f", weight, value, ratio);
        }
    }
    
    public static int knapsack01(Item[] items, int capacity) {
        int n = items.length;
        int[][] dp = new int[n + 1][capacity + 1];
        
        //DP 테이블 채우기
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                //현재 아이템의 무게가 배낭 용량보다 큰 경우
                if (items[i-1].weight > w) {
                    dp[i][w] = dp[i-1][w]; //이전 아이템까지의 최대 가치
                } else {
                    //현재 아이템을 포함하는 경우와 포함하지 않는 경우 중 최대값
                    dp[i][w] = Math.max(
                        dp[i-1][w], // 포함하지 않는 경우
                        dp[i-1][w - items[i-1].weight] + items[i-1].value //포함하는 경우
                    );
                }
            }
        }
        
        System.out.println("\n1. 최대 가치 조합");
        printSelectedItems(dp, items, capacity);
        
        return dp[n][capacity];
    }
    

    private static void printSelectedItems(int[][] dp, Item[] items, int capacity) {
        int w = capacity;
        int totalWeight = 0;
        int totalValue = 0;
        
        System.out.println("선택된 아이템들:");
        for (int i = items.length; i > 0 && w > 0; i--) {
            if (dp[i][w] != dp[i-1][w]) {
                System.out.printf("Item %d: %s\n", i, items[i-1]);
                totalWeight += items[i-1].weight;
                totalValue += items[i-1].value;
                w -= items[i-1].weight;
            }
        }
        System.out.printf("총 무게: %dkg, 총 가치: %d만원\n", totalWeight, totalValue);
    }
    
    public static void minimizeWeightWithValueConstraint(Item[] items, int maxValue) {
        System.out.println("\n2. 응용문제: 가치 80만원 이하에서 최소 무게");
        
        int n = items.length;
        // dp[i][v] = i번째 아이템까지 고려했을 때 가치가 정확히 v인 경우의 최소 무게
        int[][] dp = new int[n + 1][maxValue + 1];
        
        //초기화
        for (int i = 0; i <= n; i++) {
            for (int v = 1; v <= maxValue; v++) {
                dp[i][v] = Integer.MAX_VALUE;
            }
        }
        
        //가치가 0인 경우 무게도 0
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 0;
        }
        
        //DP 테이블 채우기
        for (int i = 1; i <= n; i++) {
            for (int v = 0; v <= maxValue; v++) {
                //현재 아이템을 포함하지 않는 경우
                dp[i][v] = dp[i-1][v];
                
                //현재 아이템을 포함하는 경우
                if (v >= items[i-1].value && dp[i-1][v - items[i-1].value] != Integer.MAX_VALUE) {
                    dp[i][v] = Math.min(dp[i][v], dp[i-1][v - items[i-1].value] + items[i-1].weight);
                }
            }
        }
        
        //가치 80만원 이하에서 최소 무게 찾기
        int minWeight = Integer.MAX_VALUE;
        int bestValue = 0;
        
        for (int v = 0; v <= maxValue; v++) {
            if (dp[n][v] < minWeight) {
                minWeight = dp[n][v];
                bestValue = v;
            }
        }
        
        System.out.printf("최소 무게: %dkg, 가치: %d만원\n", minWeight, bestValue);
        
        printSelectedItemsForMinWeight(dp, items, bestValue, maxValue);
    }
    
    private static void printSelectedItemsForMinWeight(int[][] dp, Item[] items, int targetValue, int maxValue) {
        int v = targetValue;
        int totalWeight = 0;
        int totalValue = 0;
        
        System.out.println("선택된 아이템들:");
        for (int i = items.length; i > 0 && v > 0; i--) {
            if (v >= items[i-1].value && 
                dp[i-1][v - items[i-1].value] != Integer.MAX_VALUE &&
                dp[i][v] == dp[i-1][v - items[i-1].value] + items[i-1].weight) {
                System.out.printf("Item %d: %s\n", i, items[i-1]);
                totalWeight += items[i-1].weight;
                totalValue += items[i-1].value;
                v -= items[i-1].value;
            }
        }
        System.out.printf("총 무게: %dkg, 총 가치: %d만원\n", totalWeight, totalValue);
    }
    
    public static void findLightestCombination(Item[] items, int maxValue) {
        System.out.println("\n예산 안에서 가장 가벼운 아이템 조합");
        
        //가치/무게(ratio) 비율로 정렬하여 효율적인 아이템 우선 선택
        Item[] sortedItems = items.clone();
        java.util.Arrays.sort(sortedItems, (a, b) -> Double.compare(b.ratio, a.ratio));
        
        int totalWeight = 0;
        int totalValue = 0;
        
        System.out.println("선택된 아이템들 (가치/무게 비율 순):");
        for (Item item : sortedItems) {
            if (totalValue + item.value <= maxValue) {
                System.out.printf("선택: %s\n", item);
                totalWeight += item.weight;
                totalValue += item.value;
            }
        }
        
        System.out.printf("총 무게: %dkg, 총 가치: %d만원\n", totalWeight, totalValue);
    }
    
    public static void main(String[] args) {
        // 주어진 아이템들
        Item[] items = {
            new Item(5, 10),  //Item 1: 5kg, 10만원
            new Item(4, 40),  //Item 2: 4kg, 40만원
            new Item(6, 30),  //Item 3: 6kg, 30만원
            new Item(3, 50)   //Item 4: 3kg, 50만원
        };
        
        int capacity = 10; //총 배낭 용량: 10kg
        int maxValue = 80; //최대 가치 제한: 80만원
        
        System.out.println("주어진 아이템들");
        for (int i = 0; i < items.length; i++) {
            System.out.printf("Item %d: %s\n", i + 1, items[i]);
        }
        System.out.printf("총 배낭 용량: %dkg\n", capacity);
        
        //1. 기본 문제: 최대 가치 구하기
        int maxPossibleValue = knapsack01(items, capacity);
        System.out.printf("최대 가치: %d만원\n", maxPossibleValue);
        
        //2. 응용 문제: 가치 80만원 이하에서 최소 무게
        minimizeWeightWithValueConstraint(items, maxValue);
        
        //추가: 단순한 방법으로 가벼운 조합 찾기
        findLightestCombination(items, maxValue);
    }
}
