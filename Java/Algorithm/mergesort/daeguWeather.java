package mergesort;

import java.io.*;
import java.util.*;

public class daeguWeather {
    // 날씨 데이터를 저장할 클래스
    static class WeatherData {
        String date;
        int temperature;
        
        public WeatherData(String date, int temperature) {
            this.date = date;
            this.temperature = temperature;
        }
        
        @Override
        public String toString() {
            return date + ": " + temperature + "°C";
        }
    }
    
    //병합 정렬: 온도를 내림차순으로 정렬
    public static void mergeSort(WeatherData[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            
            // 배열의 왼쪽 절반과 오른쪽 절반을 정렬
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            
            // 정렬된 두 절반을 병합
            merge(arr, left, mid, right);
        }
    }
    
    //병합 함수:온도를 기준으로 내림차순으로 정렬
    public static void merge(WeatherData[] arr, int left, int mid, int right) {
        //임시 배열 크기 설정
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        //임시 배열 생성
        WeatherData[] leftArr = new WeatherData[n1];
        WeatherData[] rightArr = new WeatherData[n2];
        
        //데이터를 임시 배열에 복사
        for (int i = 0; i < n1; i++) {
            leftArr[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArr[j] = arr[mid + 1 + j];
        }
        
        //두 임시 배열 병합
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            //내림차순 정렬
            if (leftArr[i].temperature >= rightArr[j].temperature) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }
        
        //남아있는 요소 복사
        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }
        
        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }
    
    //.txt파일에서 날씨 데이터 읽기
    public static WeatherData[] readWeatherData(String filePath) {
        List<WeatherData> dataList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            //파일 각 줄 마다 읽어오기
            while ((line = br.readLine()) != null) {
                //공백 제거 후 날짜와 온도 분리
                String[] parts = line.trim().split("\\s+");
                //날짜와 온도로 2개의 요소로 분리 0이면 날짜 1이면 온도 
                if (parts.length == 2) {
                    String date = parts[0];
                    int temperature = Integer.parseInt(parts[1]); //문자열인 온도를 정수로 변환
                    dataList.add(new WeatherData(date, temperature));
                }
            }
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("온도 데이터 형식 오류: " + e.getMessage());
        }
        
        return dataList.toArray(new WeatherData[0]);
    }
    
    //2024년 대구 최고 기온 3개를 출력하는 함수 
    public static void printTopTemperatures(WeatherData[] sortedData, int count) {
        if (sortedData.length == 0) {
            System.out.println("출력할 데이터가 없습니다.");
            return;
        }
        
        System.out.println("\n대구 2024년 최고 기온 ");
        
        //최대 count개 또는 배열 길이 중 작은 값만큼만 출력
        int limit = Math.min(count, sortedData.length);
        for (int i = 0; i < limit; i++) {
            System.out.println(sortedData[i]);
        }
    }
    
    //정렬된 결과를 .txt파일에 저장하는 메소드
    public static void saveToFile(WeatherData[] sortedData, String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.newLine();
            for (WeatherData data : sortedData) {
                writer.write(data.date + "," + data.temperature);
                writer.newLine();
            }
            System.out.println("정렬된 데이터가 '" + outputPath + "'에 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("파일 쓰기 오류: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        //파일 경로 지정
        String filePath = "/Users/ijeongmin/Desktop/daegu_weather_2024.txt";
        
        //파일 존재 확인
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("오류: 파일을 찾을 수 없습니다: " + filePath);
            return;
        }
        
        //.txt파일에서 날씨 데이터 읽기
        WeatherData[] weatherData = readWeatherData(filePath);
        
        if (weatherData.length == 0) {
            System.err.println("오류: 데이터를 읽지 못했거나 파일이 비어 있습니다.");
            return;
        }

        mergeSort(weatherData, 0, weatherData.length - 1);
        System.out.println("정렬 완료!");
        
        //정렬된 결과를 파일에 저장
        String outputPath = "/Users/ijeongmin/Desktop/sorted_daegu_weather_2024.txt";
        saveToFile(weatherData, outputPath);
        
        //최고 온도 날짜 3개 출력
        printTopTemperatures(weatherData, 3);
    }
}
