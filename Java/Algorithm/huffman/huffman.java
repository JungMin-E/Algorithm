package huffman;

import java.io.*;
import java.util.*;

//허프만 코딩은 문자 출현 빈도에 따라 가변 길이 코드를 할당하여 데이터를 압축하는 기법
public class huffman {
    //허프만 트리 노드 구현
    //잎 노드 (문자 데이터 저장), 내부 노드 (두 자식 노드를 연결)
    static class Node implements Comparable<Node> {
        char character;      //노드가 나타내는 문자
        int frequency;       
        Node left;           
        Node right;          
        boolean isLeaf;      //잎 노드 여부
        
        //내부 노드용 생성자 - 노드를 연결하는
        public Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.frequency = left.frequency + right.frequency;  //자식 노드들의 빈도 합
            this.isLeaf = false;  //내부 노드는 잎이 아님
        }
        
        //잎 노드용 생성자 - 문자 정보를 저장
        public Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
            this.isLeaf = true;  //문자 데이터를 가진 leaf 노드
        }
        
        //우선순위 큐에서 노드 정렬을 위한 비교 메소드 빈도수가 낮은 노드가 우선순위가 높음
        @Override
        public int compareTo(Node other) {
            return this.frequency - other.frequency;
        }
    }
    
    //파일에서 문자 빈도수 계산
    public static Map<Character, Integer> calculateFrequency(String filename) throws IOException {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int c;
            //파일 끝에 도달할 때까지 문자를 하나씩 읽음
            while ((c = reader.read()) != -1) {
                char character = (char) c;
                //맵에 문자가 있으면 빈도 +1, 없으면 1로 설정
                frequencyMap.put(character, frequencyMap.getOrDefault(character, 0) + 1);
            }
        }
        
        return frequencyMap;
    }
    
    //빈도수 기반으로 허프만 트리 생성
    public static Node buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        //빈도수 기준으로 정렬되는 우선순위 큐 생성
        PriorityQueue<Node> queue = new PriorityQueue<>();
        
        //각 문자에 대한 leaf 노드 생성
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            queue.add(new Node(entry.getKey(), entry.getValue()));
        }
        
        //허프만 트리 구축 (그리디 알고리즘)
        //빈도수가 가장 낮은 두 노드를 합쳐 새 노드 생성 반복
        while (queue.size() > 1) {
            Node left = queue.poll();  //가장 낮은 빈도의 노드
            Node right = queue.poll(); //두 번째로 낮은 빈도의 노드
            queue.add(new Node(left, right)); //두 노드를 자식으로 하는 새 노드 생성
        }
        
        return queue.poll(); //마지막 남은 노드 = 루트 노드
    }
    
    //허프만 트리로부터 코드 테이블 생성
    //트리를 순회하여 각 문자에 해당하는 이진 코드 생성
    public static Map<Character, String> buildCodeTable(Node root) {
        Map<Character, String> codeTable = new HashMap<>();
        buildCodeTableRecursive(root, "", codeTable);
        return codeTable;
    }
    
    //재귀적으로 트리를 순회하며 코드 테이블 구축
    private static void buildCodeTableRecursive(Node node, String code, Map<Character, String> codeTable) {
        if (node.isLeaf) {
            codeTable.put(node.character, code.isEmpty() ? "0" : code); //노드가 하나인 경우 처리
            return;
        }
        
        //왼쪽 자식으로 이동할 때는 코드에 0 추가
        if (node.left != null) {
            buildCodeTableRecursive(node.left, code + "0", codeTable);
        }
        
        //오른쪽 자식으로 이동할 때는 코드에 1 추가
        if (node.right != null) {
            buildCodeTableRecursive(node.right, code + "1", codeTable);
        }
    }
    
    //파일 인코딩 원본 파일을 허프만 코드를 사용하여 압축 파일로 저장
    public static void encodeFile(String inputFile, String outputFile, Map<Character, String> codeTable) throws IOException {
        StringBuilder encodedText = new StringBuilder();
        
        //파일을 읽어 인코딩 각 문자를 해당하는 허프만 코드로 변환
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            int c;
            while ((c = reader.read()) != -1) {
                char character = (char) c;
                encodedText.append(codeTable.get(character)); //문자를 해당 코드로 변환
            }
        }
        
        //인코딩된 텍스트를 바이트로 변환하여 저장
        try (BitOutputStream writer = new BitOutputStream(new FileOutputStream(outputFile))) {
            //허프만 코드 테이블 저장 테이블 크기 저장
            writer.writeInt(codeTable.size());
            //각 문자와 해당 코드 정보를 저장
            for (Map.Entry<Character, String> entry : codeTable.entrySet()) {
                writer.writeChar(entry.getKey());                //문자
                writer.writeInt(entry.getValue().length());      //코드 길이
                writer.writeString(entry.getValue());            //코드 문자열
            }
            
            //인코딩된 텍스트의 길이 저장
            writer.writeInt(encodedText.length());
            
            //인코딩된 텍스트 저장
            for (int i = 0; i < encodedText.length(); i++) {
                writer.writeBit(encodedText.charAt(i) == '1');  //1이면 true, 0이면 false
            }
        }
    }
    
    //파일 디코딩
    public static void decodeFile(String inputFile, String outputFile) throws IOException {
        try (BitInputStream reader = new BitInputStream(new FileInputStream(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            
            //코드 테이블 읽기
            int tableSize = reader.readInt();  //테이블 크기
            Map<String, Character> reverseCodeTable = new HashMap<>();
            
            //코드 테이블의 모든 항목 읽기
            for (int i = 0; i < tableSize; i++) {
                char character = reader.readChar();        //문자
                int codeLength = reader.readInt();         //코드 길이
                String code = reader.readString(codeLength); //코드 문자열
                reverseCodeTable.put(code, character);     //역방향 매핑 저장
            }
            
            //인코딩된 텍스트의 길이 읽기
            int bitCount = reader.readInt();
            
            //인코딩된 텍스트 디코딩
            StringBuilder currentCode = new StringBuilder();  //현재 처리 중인 코드
            for (int i = 0; i < bitCount; i++) {
                currentCode.append(reader.readBit() ? '1' : '0');  //비트를 문자로 변환하여 추가
                
                //현재 코드가 테이블에 있으면 해당 문자 출력
                if (reverseCodeTable.containsKey(currentCode.toString())) {
                    writer.write(reverseCodeTable.get(currentCode.toString()));  //문자 출력
                    currentCode.setLength(0);  //현재 코드 초기화 (다음 코드 처리 준비)
                }
            }
        }
    }
    
    //파일 일치 확인 두 파일이 내용상 동일한지 비교
    public static boolean compareFiles(String file1, String file2) throws IOException {
        try (BufferedReader reader1 = new BufferedReader(new FileReader(file1));
             BufferedReader reader2 = new BufferedReader(new FileReader(file2))) {
            
            int c1, c2;
            //파일에서 문자를 하나씩 비교
            while ((c1 = reader1.read()) != -1 && (c2 = reader2.read()) != -1) {
                if (c1 != c2) {
                    return false;  //문자가 다르면 false
                }
            }
            
            return reader1.read() == -1 && reader2.read() == -1;
        }
    }
    
    //압축률 계산
    public static double calculateCompressionRatio(String originalFile, String compressedFile) throws IOException {
        File original = new File(originalFile);
        File compressed = new File(compressedFile);
        
        long originalSize = original.length() * 8;  //바이트를 비트로 변환
        long compressedSize = compressed.length() * 8;
        
        return (1 - (double) compressedSize / originalSize) * 100;
    }
    
    //허프만 코드 확인 및 출력
    public static void printHuffmanCodes(Map<Character, String> codeTable) {
        System.out.println("Huffman Codes:");
        for (Map.Entry<Character, String> entry : codeTable.entrySet()) {
            char c = entry.getKey();
            //특수 문자 표시를 위한 처리 (개행, 캐리지 리턴, 공백)
            String display = c == '\n' ? "\\n" : c == '\r' ? "\\r" : c == ' ' ? "space" : String.valueOf(c);
            System.out.println("'" + display + "': " + entry.getValue());
        }
    }
    
    //빈도수 출력
    public static void printFrequency(Map<Character, Integer> frequencyMap) {
        System.out.println("Character Frequencies:");
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            char c = entry.getKey();
            //특수 문자 표시를 위한 처리
            String display = c == '\n' ? "\\n" : c == '\r' ? "\\r" : c == ' ' ? "space" : String.valueOf(c);
            System.out.println("'" + display + "': " + entry.getValue());
        }
    }
    
    //메인 메소드 - 전체 허프만 압축/해제 프로세스 실행
    public static void main(String[] args) {
        try {
            //파일 경로 설정
            String inputFile = "/Users/ijeongmin/Desktop/sample.txt";
            String encodedFile = "/Users/ijeongmin/Desktop/sample.enc";
            String decodedFile = "/Users/ijeongmin/Desktop/sample.dec";
            
            //문자 빈도수
            Map<Character, Integer> frequencyMap = calculateFrequency(inputFile);
            printFrequency(frequencyMap);
            
            //허프만 트리 생성
            Node huffmanTree = buildHuffmanTree(frequencyMap);
            Map<Character, String> codeTable = buildCodeTable(huffmanTree);
            printHuffmanCodes(codeTable);
            
            //파일 인코딩
            encodeFile(inputFile, encodedFile, codeTable);
            System.out.println("\n파일 인코딩 완료: " + encodedFile);
            
            //파일 디코딩
            decodeFile(encodedFile, decodedFile);
            System.out.println("파일 디코딩 완료: " + decodedFile);
            
            //원본과 디코딩된 파일 비교
            boolean filesMatch = compareFiles(inputFile, decodedFile);
            System.out.println("원본 파일과 디코딩된 파일 일치: " + filesMatch);
            
            //압축률 계산
            double compressionRatio = calculateCompressionRatio(inputFile, encodedFile);
            System.out.println("압축률: " + String.format("%.2f", compressionRatio) + "%");
            
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
}


 //비트 단위로 파일에 쓰는 유틸리티 클래스
class BitOutputStream implements Closeable {
    private OutputStream output;    
    private int currentByte;        
    private int numBitsFilled;      
    
    //생성자
    public BitOutputStream(OutputStream out) {
        output = out;
        currentByte = 0;
        numBitsFilled = 0;
    }
    
    public void writeBit(boolean bit) throws IOException {
        if (bit) {
            currentByte = (currentByte << 1) | 1;  
        } else {
            currentByte = currentByte << 1;       
        }
        
        numBitsFilled++;
        
        if (numBitsFilled == 8) {
            output.write(currentByte);
            currentByte = 0;
            numBitsFilled = 0;
        }
    }
    
    public void writeChar(char c) throws IOException {
        output.write(c);
    }
    
    public void writeInt(int value) throws IOException {
        output.write((value >> 24) & 0xFF);  
        output.write((value >> 16) & 0xFF);  
        output.write((value >> 8) & 0xFF);   
        output.write(value & 0xFF);         
    }
    
    //문자열 쓰기
    public void writeString(String s) throws IOException {
        for (char c : s.toCharArray()) {
            output.write(c);
        }
    }
    
    //스트림 닫기
    @Override
    public void close() throws IOException {
        while (numBitsFilled > 0 && numBitsFilled < 8) {
            writeBit(false);  // 패딩 (0 비트 추가)
        }
        output.close();
    }
}

class BitInputStream implements Closeable {
    private InputStream input;        
    private int currentByte;          
    private int numBitsRemaining;     
    
    //생성자
    public BitInputStream(InputStream in) {
        input = in;
        currentByte = 0;
        numBitsRemaining = 0;
    }
    
    public boolean readBit() throws IOException {
        if (numBitsRemaining == 0) {
            currentByte = input.read();
            if (currentByte == -1) {
                throw new EOFException("End of file reached");
            }
            numBitsRemaining = 8;
        }
        
        boolean result = ((currentByte >> (numBitsRemaining - 1)) & 1) == 1;
        numBitsRemaining--;
        return result;
    }
    
    public char readChar() throws IOException {
        int result = input.read();
        if (result == -1) {
            throw new EOFException("End of file reached");
        }
        return (char) result;
    }
    
    public int readInt() throws IOException {
        int b1 = input.read();  
        int b2 = input.read();  
        int b3 = input.read();  
        int b4 = input.read();  
        
        if (b1 == -1 || b2 == -1 || b3 == -1 || b4 == -1) {
            throw new EOFException("End of file reached");
        }
        
        return (b1 << 24) | (b2 << 16) | (b3 << 8) | b4;
    }
    
    public String readString(int length) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(readChar());
        }
        return result.toString();
    }
    
    @Override
    public void close() throws IOException {
        input.close();
    }
}
