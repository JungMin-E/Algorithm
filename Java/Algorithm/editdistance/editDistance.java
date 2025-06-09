package editdistance;

public class editDistance {
    
    public static int minDistance(String str1, String str2) {
        int m = str1.length(); //str1의 길이
        int n = str2.length(); //str2의 길이
        
        //dp 테이블 초기화
        int[][] dp = new int[m + 1][n + 1];
        
        //첫 번째 문자열이 비어 있는 경우, 모든 문자를 삽입해야 함
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        
        //두 번째 문자열이 비어 있는 경우, 모든 문자를 삭제해야 함
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        
        //편집 거리 계산
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                //같은 문자인 경우 비용 없음
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 세 가지 연산 중 최소값을 선택
                    // 1. 삽입 (j-1에서 문자 하나 추가)
                    // 2. 삭제 (i-1에서 문자 하나 삭제)
                    // 3. 치환 (i-1과 j-1에서 문자 하나 변경)
                    dp[i][j] = 1 + Math.min(
                            Math.min(dp[i][j - 1], dp[i - 1][j]), 
                            dp[i - 1][j - 1]);
                }
            }
        }
        
        //테이블 출력
        printEditDistanceTable(dp, str1, str2);
        
        return dp[m][n];
    }
    
    private static void printEditDistanceTable(int[][] dp, String str1, String str2) {
        System.out.println("Edit Distance Table:");
        
        //열 인덱스 출력
        System.out.print("   |   | ");
        //
        for (int j = 0; j < str2.length(); j++) {
            System.out.print(str2.charAt(j) + " | ");
        }
        System.out.println();
        
        //테이블 출력
        for (int i = 0; i <= str1.length(); i++) {
            if (i == 0) {
                System.out.print("   | ");
            } else {
                System.out.print(" " + str1.charAt(i - 1) + " | ");
            }
            
            for (int j = 0; j <= str2.length(); j++) {
                System.out.print(dp[i][j] + " | ");
            }
            System.out.println();
        }
    }
    
    public static String findClosestWord(String input, String[] dictionary) {
        String closestWord = "";
        int minDistance = Integer.MAX_VALUE;
        
        for (String word : dictionary) {
            int distance = minDistance(input, word);
            if (distance < minDistance) {
                minDistance = distance;
                closestWord = word;
            }
        }
        
        return "추천: \"" + closestWord + "\" (edit distance " + minDistance + ")";
    }
    
    public static void main(String[] args) {
        //두 문자열 간의 편집 거리
        String str1 = "kitty";
        String str2 = "pitty";
        
        System.out.println("str1: " + str1);
        System.out.println("str2: " + str2);
        int distance = minDistance(str1, str2);
        System.out.println("최소 편집 거리: " + distance);
        System.out.println();
        
        //사전에서 가장 가까운 단어 찾기
        String input = "definately";
        String[] dictionary = {"definitely", "defiantly", "define"};
        
        System.out.println("사용자 입력: \"" + input + "\"");
        System.out.print("사전 단어: [");
        for (int i = 0; i < dictionary.length; i++) {
            System.out.print("\"" + dictionary[i] + "\"");
            if (i < dictionary.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        
        String result = findClosestWord(input, dictionary);
        System.out.println(result);
    }
}
