package quicksort;

import java.util.Random;

public class quickPivot {
    private static int randomCount = 0;
    private static int medianCount = 0;
    
    //배열의 두 요소를 교환하는 메소드
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    //Random Pivot QuickSort
    public static void quickSortRandom(int[] arr, int left, int right) {
        if (left < right) {
            int pivot = partitionRandom(arr, left, right);
            quickSortRandom(arr, left, pivot - 1);
            quickSortRandom(arr, pivot + 1, right);
        }
    }
    
    //Random Pivot Partition
    private static int partitionRandom(int[] arr, int left, int right) {
        randomCount++;
        
        //랜덤하게 피벗 선택
        Random rand = new Random();
        int randomIndex = rand.nextInt(right - left + 1) + left; //랜덤 인덱스 생성
        swap(arr, left, randomIndex);
        
        int pivot = arr[left];
        int i = left + 1;
        int j = right;
        
        while (i < j) {
            while (i <= right && arr[i] <= pivot) i++;
            while (j >= left + 1 && arr[j] > pivot) j--;
            
            if (i < j) {
                swap(arr, i, j);
            }
        }
        
        if (arr[j] < pivot) {
            swap(arr, left, j);
        }
        
        return j;
    }
    
    // Median QuickSort
    public static void quickSortMedian(int[] arr, int left, int right) {
        if (left < right) {
            int pivot = partitionMedian(arr, left, right);
            quickSortMedian(arr, left, pivot - 1);
            quickSortMedian(arr, pivot + 1, right);
        }
    }
    
    //Median Pivot Partition
    private static int partitionMedian(int[] arr, int left, int right) {
        medianCount++;
        
        //중앙값을 피벗으로 선택
        int mid = left + (right - left) / 2;
        int[] candidates = {arr[left], arr[mid], arr[right]};
        java.util.Arrays.sort(candidates);
        int medianValue = candidates[1];
        
        //중앙값이 중간(mid)에 있을 경우
        if (arr[mid] == medianValue) {
            //중앙값을 첫 번째 위치로 이동
            swap(arr, left, mid);
        } 
        //중앙값이 마지막(right)에 있을 경우
        else if (arr[right] == medianValue) {
            //중앙값을 첫 번째 위치로 이동
            swap(arr, left, right);
        }

        int pivot = arr[left]; //pivot을 첫 번째로 설정
        int i = left + 1; //왼쪽에서 오른쪽으로 이동
        int j = right; //오른쪽에서 왼쪽으로 이동
        
        //i, j가 교차할 때까지 반복
        while (i < j) {
            //i가 오른쪽 끝을 넘지 않고 pivot보다 작거나 같은 값을 찾을 때까지 오른쪽으로 이동
            while (i <= right && arr[i] <= pivot) i++;
            //j가 왼쪽 끝을 넘지 않고 pivot보다 큰 값을 찾을 때까지 왼쪽으로 이동
            while (j >= left + 1 && arr[j] > pivot) j--;
            
            //i와 j가 교차하지 않았다면 두 요소를 교환
            if (i < j) {
                swap(arr, i, j);
            }
        }
        
        if (arr[j] < pivot) {
            swap(arr, left, j);
        }
        
        return j;
    }
    
    public static void main(String[] args) {

        int[] arr1 = {6, 3, 11, 9, 12, 2, 8, 15, 10, 7, 14};
        int[] arr2 = {6, 3, 11, 9, 12, 2, 8, 15, 10, 7, 14};
        
        //Random Pivot 테스트
        quickSortRandom(arr1, 0, arr1.length - 1);
        
        //Median-of-Three Pivot 테스트
        quickSortMedian(arr2, 0, arr2.length - 1);
        
        //피벗 선택 횟수 출력
        System.out.println("Random Pivot: " + randomCount);
        System.out.println("Median Pivot: " + medianCount);
    }
}
