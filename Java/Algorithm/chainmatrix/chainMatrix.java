package chainmatrix;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;

public class chainMatrix extends JFrame {
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static ArrayList<Integer> matrixSizes = new ArrayList<>();
    private static ArrayList<Integer> operationCounts = new ArrayList<>();
    
    public chainMatrix() {
        setTitle("행렬 체인 곱셈 연산량 분석");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    //행렬 체인 곱셈을 위한 최적의 순서를 계산하는 메소드
    public static int matrixChainOrder(int[] dimensions) {
        int n = dimensions.length - 1; //행렬의 개수
        int[][] m = new int[n+1][n+1]; //최소 연산 횟수를 저장할 테이블
        int[][] s = new int[n+1][n+1]; //최적 분할 위치를 저장할 테이블
        
        //대각선 요소는 0으로 설정
        for (int i = 1; i <= n; i++) {
            m[i][i] = 0;
        }
        
        //연쇄 길이에 따라 계산
        for (int l = 2; l <= n; l++) {
            for (int i = 1; i <= n - l + 1; i++) {
                int j = i + l - 1;
                m[i][j] = Integer.MAX_VALUE;
                
                //모든 가능한 분할 위치에 대해 최소값 찾기
                for (int k = i; k < j; k++) {
                    int cost = m[i][k] + m[k+1][j] + dimensions[i-1] * dimensions[k] * dimensions[j];
                    if (cost < m[i][j]) {
                        m[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }
        
        //결과
        printMatrixChainMultiplication(m, s, dimensions, n);
        return m[1][n]; // 최소 연산 횟수 반환
    }
    
    //결과 출력 메소드
    public static void printMatrixChainMultiplication(int[][] m, int[][] s, int[] dimensions, int n) {
        System.out.println("Chained Matrix Multiplication");
        System.out.printf("%-10s", "M");
        
        for (int i = 1; i <= n; i++) {
            System.out.printf("%-10d", i);
        }
        System.out.println();
        
        for (int i = 1; i <= n; i++) {
            System.out.printf("%-10d", i);
            for (int j = 1; j <= n; j++) {
                if (j < i) {
                    System.out.printf("%-10d", 0);
                } else {
                    System.out.printf("%-10d", m[i][j]);
                }
            }
            System.out.println();
        }
        
        System.out.println("\nFinal Solution : " + m[1][n]);
        
        System.out.print("\nImplicit Order for Matrix Multiplication : ");
        printOptimalParenthesis(s, 1, n);
        System.out.println();
    }
    
    //최적의 괄호 배치를 출력하는 메소드
    public static void printOptimalParenthesis(int[][] s, int i, int j) {
        if (i == j) {
            System.out.print("A" + i);
        } else {
            System.out.print("(");
            printOptimalParenthesis(s, i, s[i][j]);
            System.out.print(" x ");
            printOptimalParenthesis(s, s[i][j] + 1, j);
            System.out.print(")");
        }
    }
    
    //랜덤 행렬 크기 생성 메소드
    public static int[] generateRandomMatrixDimensions(int n) {
        Random random = new Random();
        int[] dimensions = new int[n + 1];
        
        for (int i = 0; i <= n; i++) {
            dimensions[i] = random.nextInt(91) + 10; // 10~100 사이의 랜덤 크기
        }
        
        return dimensions;
    }
    
    //연산량 분석을 위한 메소드
    public static void analyzeOperationsByMatrixSize() {
        System.out.println("행렬 크기에 따른 연산량 분석");
        matrixSizes.clear();
        operationCounts.clear();
        
        for (int n = 5; n <= 10; n++) {
            System.out.println("\n** " + n + "개 행렬의 체인 곱셈 **");
            int[] dimensions = generateRandomMatrixDimensions(n);
            
            System.out.print("행렬 크기: ");
            for (int i = 0; i < n; i++) {
                System.out.print("A" + (i+1) + "(" + dimensions[i] + "x" + dimensions[i+1] + ") ");
            }
            System.out.println();
            
            int operations = matrixChainOrder(dimensions);
            
            matrixSizes.add(n);
            operationCounts.add(operations);
            
            System.out.println("최소 연산 횟수: " + operations);
        }
    }
    
    //그래프 패널 클래스
    class GraphPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int padding = 50;
            int labelPadding = 20;
            
            int w = getWidth() - 2 * padding;
            int h = getHeight() - 2 * padding;
            
            //x축과 y축 그리기
            g2.setColor(Color.BLACK);
            g2.drawLine(padding, getHeight() - padding, padding, padding);
            g2.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding);
            
            //데이터의 최대값 찾기
            int maxOperations = Collections.max(operationCounts);
            
            //축 라벨 그리기
            g2.drawString("행렬 개수", getWidth() / 2, getHeight() - padding + 30);
            
            //y축 회전을 위한 설정
            AffineTransform originalTransform = g2.getTransform();
            AffineTransform at = new AffineTransform();
            at.setToRotation(-Math.PI / 2);
            g2.setTransform(at);
            g2.drawString("연산 횟수", -getHeight() / 2 - 50, padding - 30);
            g2.setTransform(originalTransform);
            
            //x축 눈금 그리기
            for (int i = 0; i < matrixSizes.size(); i++) {
                int x = padding + i * (w / (matrixSizes.size() - 1));
                g2.drawLine(x, getHeight() - padding, x, getHeight() - padding + 5);
                g2.drawString(String.valueOf(matrixSizes.get(i)), x - 5, getHeight() - padding + 20);
            }
            
            //y축 눈금 그리기
            for (int i = 0; i <= 10; i++) {
                int y = getHeight() - padding - i * (h / 10);
                int value = i * maxOperations / 10;
                g2.drawLine(padding - 5, y, padding, y);
                g2.drawString(String.valueOf(value), padding - 35, y + 5);
            }
            
            //그래프 그리기
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            
            int x1 = padding;
            int y1 = getHeight() - padding - (int)((double)operationCounts.get(0) / maxOperations * h);
            
            for (int i = 1; i < matrixSizes.size(); i++) {
                int x2 = padding + i * (w / (matrixSizes.size() - 1));
                int y2 = getHeight() - padding - (int)((double)operationCounts.get(i) / maxOperations * h);
                
                g2.drawLine(x1, y1, x2, y2);
                
                x1 = x2;
                y1 = y2;
            }
            
            //포인트 표시
            g2.setColor(Color.RED);
            for (int i = 0; i < matrixSizes.size(); i++) {
                int x = padding + i * (w / (matrixSizes.size() - 1));
                int y = getHeight() - padding - (int)((double)operationCounts.get(i) / maxOperations * h);
                
                g2.fillOval(x - 4, y - 4, 8, 8);
                g2.drawString(String.valueOf(operationCounts.get(i)), x + 5, y - 5);
            }
        }
    }
    
    public static void main(String[] args) {
        //A1(10x20), A2(20x5), A3(5x15), A4(15x30)
        int[] dimensions = {10, 20, 5, 15, 30};
        matrixChainOrder(dimensions);
        
        //행렬 크기에 따른 연산량 분석
        analyzeOperationsByMatrixSize();
        
        //그래프 표시
        SwingUtilities.invokeLater(() -> {
            chainMatrix app = new chainMatrix();
            app.add(app.new GraphPanel());
            app.setVisible(true);
        });
    }
}
