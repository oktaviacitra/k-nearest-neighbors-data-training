import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            KNN knn = new KNN();
            String name = "/Users/zakhamaisat/Documents/KNN/ruspini.csv";
            int[][] dataset = knn.readFile(name);
            // knn.printData(dataset);
            Scanner io = new Scanner(System.in);
            System.out.print("Masukkan nilai K\t: ");
            int k = io.nextInt();
            System.out.print("\n1. Ascending\n2. Descending\nMasukkan pilihan mode\t: ");
            Boolean mode = knn.getMode(io.nextInt());
            int[][] trainingData = knn.readTrainingData(dataset, 80, 60);
            int[][] testingData = knn.readTestingData(dataset, 20, 15);
            int[] trainingLabel = knn.readLabel(trainingData, 60);
            int[] testingLabel = knn.readLabel(testingData, 15);
            int total = testingData.length;
            int[] classificationResult = new int[total];
            for (int i = 0; i < total; i++) {
                double[] allDistance = knn.getAllDistance(testingData[i], trainingData);
                double[][] sortAllDistance = knn.sortData(allDistance, mode);
                classificationResult[i] = knn.getKNN(k, sortAllDistance, trainingLabel);
            }
            double result = knn.getError(testingLabel, classificationResult);
            System.out.println("\n\nEror\t\t\t: " + result + "%");
            io.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}