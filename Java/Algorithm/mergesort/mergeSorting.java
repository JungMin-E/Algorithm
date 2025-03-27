package mergesort;

import java.util.Arrays;

public class mergeSorting {

    public static void main(String[] args) {
        int[] arr = {37, 10, 22, 30, 35, 13, 25, 24};
        System.out.println("정렬 전 배열: " + Arrays.toString(arr));
        
        mergeSort(arr, 0, arr.length - 1); //mergeSort 진행
        
        System.out.println("정렬 후 배열: " + Arrays.toString(arr));
    }
    
    //병합정렬 함수
    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            //전체 배열의 중간 지점 찾기
            int mid = (left + right) / 2;
            //왼쪽부분 정렬
            mergeSort(arr, left, mid);
            //오른쪽부분 정렬
            mergeSort(arr, mid + 1, right);
            //정렬된 두 부분 병합
            merge(arr, left, mid, right);
        }
    }
    
    //두 개의 정렬된 배열을 병합하는 함수
    public static void merge(int[] arr, int left, int mid, int right) {
        //지역 변수로 임시 배열 생성
        int[] temp = new int[right - left + 1];
        
        int i = left;      //왼쪽 부분 배열의 시작 인덱스
        int j = mid + 1;   //오른쪽 부분 배열의 시작 인덱스
        int k = 0;         //임시 배열의 인덱스
        
        //왼쪽과 오른쪽 부분 배열을 비교하여 작은 값을 임시 배열에 저장
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        //왼쪽 부분 배열에 남은 요소들을 임시 배열에 복사
        while (i <= mid) {
            temp[k++] = arr[i++];
        }
        
        //오른쪽 부분 배열에 남은 요소들을 임시 배열에 복사
        while (j <= right) {
            temp[k++] = arr[j++];
        }
        //임시 배열의 요소들을 원본 배열에 복사
        for (i = 0; i < temp.length; i++) {
            arr[left + i] = temp[i];
        }
    }
}
