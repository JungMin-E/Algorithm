package quicksort;

public class quickSorting {
    private static int pivotCount = 0; //피벗 횟수 count
    
    //배열의 두 요소를 교환하는 메소드
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    //배열 출력 메소드
    private static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
    
    //QuickSort 메인 메소드
    public static void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int pivot = partition(arr, left, right);
            //피벗을 기준으로 왼쪽 부분 정렬
            quickSort(arr, left, pivot - 1);
            //피벗을 기준으로 오른쪽 부분 정렬
            quickSort(arr, pivot + 1, right);
        }
    }
    
    //파티션 메소드
    private static int partition(int[] arr, int left, int right) {
        pivotCount++; 

        //처음으로 pivot을 8로 설정
        if(pivotCount == 1) {
            for(int i = left; i <= right; i++) {
                if(arr[i] == 8) {
                    swap(arr, left, i);
                    break;
                }
            }
        }
        int pivot = arr[left]; //첫 번째 요소를 피벗으로 선택
        int i = left + 1;
        int j = right;
        
        System.out.println("\n피벗 선택 #" + pivotCount + ": " + pivot);
        System.out.print("현재 부분 배열: ");
        for (int k = left; k <= right; k++) {
            System.out.print(arr[k] + " ");
        }
        System.out.println();
        
        while (i < j) {
            //피벗보다 큰 값을 찾을 때까지 i 증가
            while (i <= right && arr[i] <= pivot) {
                i++;
            }
            //피벗보다 작은 값을 찾을 때까지 j 감소
            while (j >= left + 1 && arr[j] > pivot) {
                j--;
            }
            
            //i와 j가 교차하지 않았다면 교환
            if (i < j) {
                swap(arr, i, j);
                System.out.print("교환 후: ");
                printArray(arr);
            }
        }
        
        //피벗을 올바른 위치로 이동
        if (arr[j] < pivot) {
            swap(arr, left, j);
            System.out.print("피벗 교환 후: ");
            printArray(arr);
        }
        
        return j;
    }
    
    public static void main(String[] args) {
        int[] arr = {6, 3, 11, 9, 12, 2, 8, 15, 18, 10, 7, 14};
        
        System.out.println("초기 배열:");
        printArray(arr);
        
        quickSort(arr, 0, arr.length - 1);
        
        System.out.println("\n최종 정렬된 배열:");
        printArray(arr);
        System.out.println("\n총 피벗 선택 횟수: " + pivotCount);
    }
}
