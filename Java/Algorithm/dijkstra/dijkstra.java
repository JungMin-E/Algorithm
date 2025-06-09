package dijkstra;

import java.util.*;

public class dijkstra {
    static final int INF = Integer.MAX_VALUE;
    static final String[] cities = {"서울", "원주", "천안", "논산", "대전", "강릉", "광주", "대구", "포항", "부산"};
    
    // 도시 그래프 초기화
    public static int[][] initializeCityGraph() {
        int[][] graph = {
            { 0,  15,  12, INF, INF, INF, INF, INF, INF, INF }, //서울
            { 15,  0, INF,  INF, INF, 21, INF, 7, INF, INF }, // 원주
            { 12,  INF, 0, 4, 10, INF, INF, INF, INF, INF }, //천안
            { INF, INF, 4,   0, 3, INF, 13, INF, INF, INF }, //논산
            { INF, INF, 10, 3, 0, INF, INF, 10, INF, INF }, //대전
            { INF, 21, INF, INF, INF, 0, INF, INF, 25, INF }, //강릉
            { INF, INF, INF, 13, INF, INF, 0, INF, INF, 15 }, //광주
            { INF, 7, INF, INF, 10, INF, INF, 0, 19, 9 }, //대구
            { INF, INF, INF, INF, INF, 25, INF, 19, 0, 5 }, //포항
            { INF, INF, INF, INF, INF, INF, 15, 9, 5, 0 }  //부산
        };
        return graph;
    }
    
    // 랜덤 그래프 생성
    public static int[][] generateRandomGraph(int vertices) {
        int[][] graph = new int[vertices][vertices];
        Random random = new Random();
        
        // 대각선은 0으로 초기화
        for (int i = 0; i < vertices; i++) {
            graph[i][i] = 0;
        }
        
        // 랜덤 가중치 할당 (연결 확률 70%)
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i != j) {
                    if (random.nextDouble() < 0.7) {
                        graph[i][j] = random.nextInt(30) + 1; // 1~30 사이의 가중치
                    } else {
                        graph[i][j] = INF;
                    }
                }
            }
        }
        
        return graph;
    }
    
    // Dijkstra 알고리즘
    public static int[] dijkstra(int[][] graph, int start) {
        int vertices = graph.length;
        int[] dist = new int[vertices];
        boolean[] visited = new boolean[vertices];
        int[] prev = new int[vertices]; // 경로 추적을 위한 배열
        
        Arrays.fill(dist, INF);
        Arrays.fill(prev, -1);
        dist[start] = 0;
        
        for (int count = 0; count < vertices - 1; count++) {
            int u = minDistance(dist, visited);
            visited[u] = true;
            
            for (int v = 0; v < vertices; v++) {
                if (!visited[v] && graph[u][v] != INF && 
                    dist[u] != INF && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                    prev[v] = u;
                }
            }
        }
        
        return prev;
    }
    
    // 최소 거리를 가진 정점 찾기
    private static int minDistance(int[] dist, boolean[] visited) {
        int min = INF;
        int minIndex = -1;
        
        for (int v = 0; v < dist.length; v++) {
            if (!visited[v] && dist[v] < min) {
                min = dist[v];
                minIndex = v;
            }
        }
        
        return minIndex;
    }
    
    // Dijkstra 경로 출력
    public static void printDijkstraPath(int[] prev, int start, int end) {
        if (prev[end] == -1) {
            System.out.println("경로가 존재하지 않습니다.");
            return;
        }
        
        List<Integer> path = new ArrayList<>();
        for (int v = end; v != -1; v = prev[v]) {
            path.add(v);
        }
        Collections.reverse(path);
        
        System.out.print("Dijkstra 경로: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(cities[path.get(i)]);
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
    
    // 거리 행렬 출력
    public static void printDistanceMatrix(int[][] graph) {
        System.out.println("\n거리 행렬:");
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j] == INF) {
                    System.out.print("INF\t");
                } else {
                    System.out.print(graph[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n1. 도시 그래프 테스트");
            System.out.println("2. 랜덤 그래프 시간 측정");
            System.out.println("3. 종료");
            System.out.print("선택: ");
            
            int choice = scanner.nextInt();
            
            if (choice == 1) {
                int[][] cityGraph = initializeCityGraph();
                System.out.println("\n도시 목록:");
                for (int i = 0; i < cities.length; i++) {
                    System.out.println(i + ": " + cities[i]);
                }
                
                System.out.print("출발 도시 번호: ");
                int start = scanner.nextInt();
                System.out.print("도착 도시 번호: ");
                int end = scanner.nextInt();
                
                if (start >= 0 && start < cities.length && end >= 0 && end < cities.length) {
                    int[] prev = dijkstra(cityGraph, start);
                    System.out.println("\n" + cities[start] + "에서 " + cities[end] + "까지의 최단 경로:");
                    printDijkstraPath(prev, start, end);
                } else {
                    System.out.println("잘못된 도시 번호입니다.");
                }
            } else if (choice == 2) {
                for (int vertices = 10; vertices <= 20; vertices += 10) {
                    System.out.println("\n=== " + vertices + "개 정점 그래프 ===");
                    
                    // 랜덤 그래프 생성
                    int[][] graph = generateRandomGraph(vertices);
                    
                    // Dijkstra 알고리즘 시간 측정
                    long startTime = System.nanoTime();
                    for (int start = 0; start < vertices; start++) {
                        int[] prev = dijkstra(graph, start);
                    }
                    long endTime = System.nanoTime();
                    
                    System.out.println("Dijkstra 알고리즘 실행 시간: " + 
                                     (endTime - startTime) / 1000000.0 + "ms");
                    
                    // 거리 행렬 출력 (10개 정점인 경우에만)
                    if (vertices == 10) {
                        printDistanceMatrix(graph);
                    }
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
        
        scanner.close();
    }
}
