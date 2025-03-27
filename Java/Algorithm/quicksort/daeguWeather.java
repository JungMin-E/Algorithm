package quicksort;

import java.io.*;
import java.util.*;

public class daeguWeather {
    private static int pivotCount = 0;
    
    //날씨 데이터를 저장할 클래스 mergeSort과제 부분과 동일
    static class WeatherData {
        String date;
        int temperature;
        
        public WeatherData(String date, int temperature) {
            this.date = date;
            this.temperature = temperature;
        }
        
        @Override
        public String toString() {
            return date + " " + temperature;
        }
    }
    
    //3-way Partitioning QuickSort(pivot보다 작은 값, 큰 값, 같은 값)
    public static void quickSort3Way(WeatherData[] arr, int low, int high) {
        if (low >= high) return;
        
        pivotCount++;
        
        //피벗 선택 (중간 요소)
        int mid = low + (high - low) / 2;
        WeatherData pivot = arr[mid];
        
        //3-way partition(lt: pivot보다 작은 값, i: 현재 포인터, gt: pivot보다 큰 값)
        int lt = low, i = low, gt = high;
        while (i <= gt) {
            //현재 요소가 pivot보다 작을 경우
            if (arr[i].temperature < pivot.temperature) {
                swap(arr, lt++, i++); //lt와 i를 교환하고 lt와 i를 증가
            }
            //현재 요소가 pivot보다 클 경우 
            else if (arr[i].temperature > pivot.temperature) {
                swap(arr, i, gt--); //i와 gt를 교환하고 gt를 감소
            } 
            //현재 요소가 pivot과 같을 경우
            else {
                i++; //i만 오른쪽으로 이동
            }
        }
        
        //재귀적으로 분할 정렬
        quickSort3Way(arr, low, lt - 1);
        quickSort3Way(arr, gt + 1, high);
    }
    
    //배열의 두 요소를 교환하는 메소드
    private static void swap(WeatherData[] arr, int i, int j) {
        WeatherData temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    //파일에서 날씨 데이터 읽기
    private static WeatherData[] readWeatherData(String filePath) {
        List<WeatherData> dataList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    String date = parts[0];
                    int temperature = Integer.parseInt(parts[1]);
                    dataList.add(new WeatherData(date, temperature));
                }
            }
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        }
        
        return dataList.toArray(new WeatherData[0]);
    }
    
    //정렬된 결과를 파일에 저장
    private static void saveToFile(WeatherData[] sortedData, String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (WeatherData data : sortedData) {
                writer.write(data.toString());
                writer.newLine();
            }
            System.out.println("정렬된 데이터가 '" + outputPath + "'에 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("파일 쓰기 오류: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        //파일 경로 설정
        String inputPath = "/Users/ijeongmin/Downloads/daegu_weather_2024 (1).txt";
        String outputPath = "/Users/ijeongmin/Downloads/sorted_daegu_weather_2024.txt";
        
        //데이터 읽기
        WeatherData[] weatherData = readWeatherData(inputPath);
        
        if (weatherData.length == 0) {
            System.err.println("데이터를 읽지 못했거나 파일이 비어 있습니다.");
            return;
        }
        
        //3-way QuickSort 수행
        System.out.println("3-way Partitioning QuickSort 수행");
        quickSort3Way(weatherData, 0, weatherData.length - 1);
        
        //정렬된 데이터 저장
        saveToFile(weatherData, outputPath);
        
        //피벗 선택 횟수 출력
        System.out.println("3-way Partitioning에서의 피벗 선택 횟수: " + pivotCount);
    }
}
