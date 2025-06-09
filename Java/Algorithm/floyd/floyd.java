package floyd;

import java.util.*;

public class floyd {
    static final int INF = Integer.MAX_VALUE;
    static final String[] cities = {"서울", "원주", "천안", "논산", "대전", "강릉", "광주", "대구", "포항", "부산"};
    
    //도시 그래프 초기화
    public static int[][] initializeCityGraph() {
        int[][] graph = {
           //서울  원주  천안  논산  대전  강릉  광주  대구  포항  부산
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
    
    //랜덤 그래프 생성 (연결된 방향성 가중치 그래프)
    public static int[][] generateConnectedGraph(int vertices) {
        int[][] graph = new int[vertices][vertices];
        Random random = new Random();
        
        //초기화
        for (int i = 0; i < vertices; i++) {
            Arrays.fill(graph[i], INF);
            graph[i][i] = 0;
        }
        
        //MST를 생성하여 그래프가 연결되도록 보장
        for (int i = 1; i < vertices; i++) {
            int j = random.nextInt(i);
            int weight = random.nextInt(30) + 1;
            graph[j][i] = weight;
            graph[i][j] = weight;
        }
        
        //추가 간선 생성
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i != j && graph[i][j] == INF && random.nextDouble() < 0.3) {
                    graph[i][j] = random.nextInt(30) + 1;
                }
            }
        }
        
        return graph;
    }
    
    //Dijkstra 알고리즘
    public static int[] dijkstra(int[][] graph, int start) {
        int vertices = graph.length;
        int[] dist = new int[vertices];
        boolean[] visited = new boolean[vertices];
        int[] prev = new int[vertices];
        
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
    
    //최소 거리를 가진 정점 찾기
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
    
    //Dijkstra 경로 출력
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
            System.out.print(path.get(i));
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
    
    //Floyd 알고리즘
    public static int[][][] floyd(int[][] graph) {
        int vertices = graph.length;
        int[][] dist = new int[vertices][vertices];
        int[][] next = new int[vertices][vertices];
        
        //초기화
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                dist[i][j] = graph[i][j];
                if (graph[i][j] != INF) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }
        
        //Floyd 알고리즘 수행
        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF && 
                        dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
        
        //결과 반환 (거리 행렬과 다음 정점 행렬)
        int[][][] result = new int[2][][];
        result[0] = dist;
        result[1] = next;
        return result;
    }
    
    //Floyd 경로 출력
    public static void printFloydPath(int[][] next, int start, int end) {
        if (next[start][end] == -1) {
            System.out.println("경로가 존재하지 않습니다.");
            return;
        }
        
        System.out.print("Floyd 경로: ");
        System.out.print(start);
        while (start != end) {
            start = next[start][end];
            System.out.print(" -> " + start);
        }
        System.out.println();
    }
    
    //모든 쌍 최단 경로 출력
    public static void printAllPairShortestPaths(int[][] dist, int[][] next, int vertices) {
        System.out.println("\n=== 모든 쌍 최단 경로 ===");
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i != j) {
                    System.out.print(i + "에서 " + j + "까지: ");
                    if (dist[i][j] == INF) {
                        System.out.println("경로가 존재하지 않습니다.");
                    } else {
                        System.out.print("거리 = " + dist[i][j] + ", 경로 = ");
                        printFloydPath(next, i, j);
                    }
                }
            }
        }
    }
    
    //거리 행렬 출력
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
            System.out.println("\n1. Dijkstra 알고리즘 시간 측정");
            System.out.println("2. Floyd 알고리즘 시간 측정");
            System.out.println("3. 경로 추적 테스트");
            System.out.println("4. 종료");
            System.out.print("선택: ");
            
            int choice = scanner.nextInt();
            
            if (choice == 1) {
                //Dijkstra 알고리즘 시간 측정
                for (int vertices = 10; vertices <= 20; vertices += 10) {
                    System.out.println("\n=== " + vertices + "개 정점 그래프 ===");
                    
                    //랜덤 그래프 생성
                    int[][] graph = generateConnectedGraph(vertices);
                    
                    //모든 시작점에 대해 Dijkstra 알고리즘 실행
                    long startTime = System.nanoTime();
                    for (int start = 0; start < vertices; start++) {
                        System.out.println("\n시작점 " + start + "에서의 최단 경로:");
                        int[] prev = dijkstra(graph, start);
                        //각 시작점에서 다른 모든 정점까지의 경로 출력
                        for (int end = 0; end < vertices; end++) {
                            if (start != end) {
                                System.out.print(start + "에서 " + end + "까지: ");
                                printDijkstraPath(prev, start, end);
                            }
                        }
                    }
                    long endTime = System.nanoTime();
                    
                    System.out.println("\nDijkstra 알고리즘 실행 시간: " + 
                                     (endTime - startTime) / 1000000.0 + "ms");
                    
                    //거리 행렬 출력
                    if (vertices == 10) {
                        printDistanceMatrix(graph);
                    }
                }
            } else if (choice == 2) {
                //Floyd 알고리즘 시간 측정
                for (int vertices = 10; vertices <= 20; vertices += 10) {
                    System.out.println("\n=== " + vertices + "개 정점 그래프 ===");
                    
                    //랜덤 그래프 생성
                    int[][] graph = generateConnectedGraph(vertices);
                    
                    //Floyd 알고리즘 실행
                    long startTime = System.nanoTime();
                    int[][][] result = floyd(graph);
                    int[][] dist = result[0];
                    int[][] next = result[1];
                    long endTime = System.nanoTime();
                    
                    System.out.println("Floyd 알고리즘 실행 시간: " + 
                                     (endTime - startTime) / 1000000.0 + "ms");
                    
                    //거리 행렬 출력 (10개 정점인 경우에만)
                    if (vertices == 10) {
                        printDistanceMatrix(dist);
                        // 모든 쌍 최단 경로 출력
                        printAllPairShortestPaths(dist, next, vertices);
                    }
                }
            } else if (choice == 3) {
                //경로 추적 테스트
                System.out.print("정점 개수 입력 (예: 5): ");
                int vertices = scanner.nextInt();
                
                int[][] graph = generateConnectedGraph(vertices);
                System.out.println("\n생성된 그래프의 거리 행렬:");
                printDistanceMatrix(graph);
                
                System.out.println("\n경로 추적 예시:");
                System.out.println("시작점,도착점 형식으로 입력 (예: 1,3)");
                System.out.println("-1,-1 입력 시 종료");
                
                scanner.nextLine(); //버퍼 비우기
                
                while (true) {
                    System.out.print("\n경로를 입력하세요: ");
                    String input = scanner.nextLine().trim();
                    
                    if (input.equals("-1,-1")) break;
                    
                    //쉼표로 분리하고 공백 제거
                    String[] parts = input.split(",");
                    if (parts.length != 2) {
                        System.out.println("잘못된 입력 형식입니다. '시작점,도착점' 형식으로 입력하세요.");
                        continue;
                    }
                    
                    try {
                        int start = Integer.parseInt(parts[0].trim());
                        int end = Integer.parseInt(parts[1].trim());
                        
                        if (start >= 0 && start < vertices && end >= 0 && end < vertices) {
                            System.out.println("\n" + start + "에서 " + end + "까지의 최단 경로:");
                            
                            //Dijkstra 알고리즘
                            int[] prev = dijkstra(graph, start);
                            printDijkstraPath(prev, start, end);
                            
                            //Floyd 알고리즘
                            int[][][] result = floyd(graph);
                            int[][] next = result[1];
                            printFloydPath(next, start, end);
                        } else {
                            System.out.println("잘못된 정점 번호입니다.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("숫자 형식이 잘못되었습니다.");
                    }
                }
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
        
        scanner.close();
    }
}
