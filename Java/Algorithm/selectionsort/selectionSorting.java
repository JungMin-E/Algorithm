package selectionsort;

import java.util.Random;
import java.util.Scanner;

public class selectionSorting {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        
        //배열 크기 n 입력 받기 (500 ~ 10000 사이)
        System.out.print("배열 크기 n을 입력하세요 (500 ~ 10000): ");
        int n = scanner.nextInt();
        
        if (n < 500 || n > 10000) {
            System.out.println("입력 범위를 벗어났습니다. 500 ~ 10000 사이의 값을 입력하세요.");
            return;
        }
        
        //2차원 배열 생성 및 랜덤값 채우기
        int[][] array = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                array[i][j] = random.nextInt(100000) + 1; // 1 ~ 100000 사이의 랜덤 값
            }
        }
        
        //k값 랜덤 생성 (1 ~ 10000 사이)
        int k = random.nextInt(10000) + 1;
        System.out.println("찾을 k번째 작은 수: " + k);
        
        //2차원 배열을 1차원 배열로 변환
        int[] oneArray = new int[n * n];
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                oneArray[index++] = array[i][j];
            }
        }
        
        
        //방법 1: 전체 배열을 정렬하여 k번째 값 찾기
        System.out.println("\n전체 배열 정렬 후 k번째 값 찾기");
        long startTime1 = System.currentTimeMillis();
        int result1 = findKthSmallestByFullSort(oneArray.clone(), k);
        long endTime1 = System.currentTimeMillis();
        long duration1 = endTime1 - startTime1;

        System.out.println("k번째 작은 수: " + result1);
        
        //방법 2: k번 반복하여 최소값 찾기
        System.out.println("\nk번 반복하여 최소값 찾기");
        long startTime2 = System.currentTimeMillis();
        int result2 = findKthSmallestByKIterations(oneArray.clone(), k);
        long endTime2 = System.currentTimeMillis();
        long duration2 = endTime2 - startTime2;

        System.out.println("k번째 작은 수: " + result2);

        // 소요 시간만 출력
        System.out.println("방법 1 소요 시간: " + duration1 + "ms");
        System.out.println("방법 2 소요 시간: " + duration2 + "ms");
        
        scanner.close();
    }
    
    //방법 1: selection sort로 전체 배열을 정렬한 후 k번째 값 반환
    public static int findKthSmallestByFullSort(int[] arr, int k) {
        int n = arr.length;
        
        //selection sort
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            //모든 요소를 탐색하여 최소값 찾기
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            //최소값을 현재 위치로 교환
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
        
        //k번째 작은 값 반환 
        return arr[k - 1]; //인덱스 0부터 시작
    }
    
    //방법 2: k번 반복하여 매번 최소값 찾기
    public static int findKthSmallestByKIterations(int[] arr, int k) {
        int[] tempArray = arr.clone(); //원본 배열 복사
        int n = tempArray.length; //배열의 크기 
        int result = 0;
        
        //k번 반복하여 최소값 찾기
        for (int iter = 0; iter < k; iter++) {
            int minIdx = 0;
            for (int j = 0; j < n; j++) {
                if (tempArray[j] < tempArray[minIdx]) {
                    minIdx = j;
                }
            }
            result = tempArray[minIdx];
            //찾은 최소값을 무한대로 변경하여 다음 탐색에서 제외
            tempArray[minIdx] = Integer.MAX_VALUE;
        }
        
        return result;
    }
}
