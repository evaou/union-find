import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class UnionFind {
    int numOfObjects;
    int[] componentOfObject;
    int count = 0;
    File file;

    public UnionFind() {
        System.out.println("=== Union Find ===");
    }

    void setNumOfObjects(int i) {
        numOfObjects = i;
        componentOfObject = new int[numOfObjects];
        System.out.printf("There are %d objects.\n", numOfObjects);
    }

    boolean connected(int p, int q) {
        boolean isConnected;
        int result = componentOfObject[p] * componentOfObject[q];

        if (result == 0) {
            isConnected = false;
        } else if (componentOfObject[p] == componentOfObject[q]) {
            isConnected = true;
        } else {
            isConnected = false;
        }

        return isConnected;
    }

    void union(int p, int q) {
        if (componentOfObject[p] == 0) {
            if (componentOfObject[q] == 0) {
                count++;
                componentOfObject[p] = count;
                componentOfObject[q] = count;
            } else {
                componentOfObject[p] = componentOfObject[q];
            }
        } else {
            if (componentOfObject[q] == 0) {
                componentOfObject[q] = componentOfObject[p];
            } else {
                int oldIndex, newIndex;
                if (componentOfObject[p] <= componentOfObject[q]) {
                    newIndex = componentOfObject[p];
                    oldIndex = componentOfObject[q];
                } else {
                    newIndex = componentOfObject[q];
                    oldIndex = componentOfObject[p];
                }

                for (int i = 0; i < numOfObjects; i++) {
                    if (componentOfObject[i] == oldIndex) {
                        componentOfObject[i] = newIndex;
                    }
                }

                if (newIndex != oldIndex) {
                    count--;
                }
            }
        }
    }

    void listConnectedComponent() {
        System.out.printf("\nThere are %d connected components.\n", count);

        for (int i = 1; i <= count; i ++) {
            System.out.print("{");
            boolean isFirst = true;
            for (int j = 0; j < numOfObjects; j++) {
                if (componentOfObject[j] == i) {
                    if (!isFirst) {
                        System.out.printf(", ");
                    }
                    System.out.printf("%d", j);
                    isFirst = false;
                }
            }
            System.out.println("}");
        }
    }

    void getFile(String fileName) {
        URL fileUrl;

        fileUrl = ClassLoader.getSystemResource(fileName);
        if (fileUrl == null) {
            System.out.printf("File (%s) doesn't exist!\n", fileName);
            System.exit(0);
        }
        file = new File(fileUrl.getFile());
    }

    void runUnionFind() {
        FileReader fileReader;
        BufferedReader bufferedReader;
        String line;
        String[] pairOfIntegers;
        int p, q;

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            setNumOfObjects(Integer.parseInt(bufferedReader.readLine()));

            while ((line = bufferedReader.readLine()) != null) {
                pairOfIntegers = line.trim().split("\\s+");
                if (pairOfIntegers.length != 2) {
                    System.out.println("Pair should have two objects.");
                    System.exit(0);
                }

                p = Integer.parseInt(pairOfIntegers[0]);
                q = Integer.parseInt(pairOfIntegers[1]);
                System.out.printf("\nconnected(%d, %d)\n", p, q);
                if (!connected(p, q)) {
                    union(p, q);
                    System.out.printf("Not connected!\nunion(%d, %d)\n", p, q);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please specify input file.");
            System.exit(0);
        }

        String fileName = args[0];

        UnionFind uf = new UnionFind();

        uf.getFile(fileName);
        uf.runUnionFind();
        uf.listConnectedComponent();
    }
}
