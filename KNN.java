import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class KNN {
    // int[] label = { 25, 18, 19, 14 };
    int[] label = { 20, 17, 23, 15 };

    public KNN() {

    }

    public Boolean getMode(int pilihan) {
        Boolean result;
        if (pilihan == 1)
            result = true;
        else
            result = false;
        return result;
    }

    public int[][] readFile(String name) {
        int[][] intArray = new int[75][3];
        String[] stringArray = new String[3];
        String strLine = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(name));
            int count = 0;
            while (strLine != null) {
                strLine = br.readLine();
                if (strLine == null)
                    break;
                stringArray = strLine.split(",");
                for (int i = 0; i < stringArray.length; i++)
                    intArray[count][i] = Integer.valueOf(stringArray[i]);
                count++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return intArray;
    }

    public void printData(int[][] dataset) {
        for (int i = 0; i < 75; i++) {
            for (int j = 0; j < 3; j++)
                System.out.print(dataset[i][j] + " ");
            System.out.print("\n");
        }
    }

    public int[][] readTrainingData(int[][] dataSet, int splitratio, int total) {
        int trainingData[][] = new int[total][3];
        int index = 0;
        int ratio[] = new int[4];
        ratio[0] = (splitratio * label[0]) / 100;
        ratio[1] = label[0] + ((splitratio * label[1]) / 100) + 1;
        ratio[2] = label[0] + label[1] + ((splitratio * label[2]) / 100);
        ratio[3] = label[0] + label[1] + label[2] + ((splitratio * label[3]) / 100);
        for (int i = 0; i < dataSet.length; i++) {
            boolean status = false;
            if (i < ratio[0])
                status = true;
            else if (i >= label[0] && i < ratio[1])
                status = true;
            else if (i >= label[0] + label[1] && i < ratio[2])
                status = true;
            else if (i >= label[0] + label[1] + label[2] && i < ratio[3])
                status = true;
            if (status) {
                for (int j = 0; j < dataSet[1].length; j++)
                    trainingData[index][j] = dataSet[i][j];
                index++;
            }
            if (index == total)
                break;
        }
        return trainingData;
    }

    public int[][] readTestingData(int[][] dataSet, int splitratio, int total) {
        int testingData[][] = new int[total][3];
        int index = 0;
        int ratio[] = new int[4];
        ratio[0] = (label[0] - ((splitratio * label[0]) / 100)) - 1;
        ratio[1] = (label[0] + label[1] - ((splitratio * label[1]) / 100)) - 1;
        ratio[2] = (label[0] + label[1] + label[2] - ((splitratio * label[2]) / 100)) - 1;
        ratio[3] = (label[0] + label[1] + label[2] + label[3] - ((splitratio * label[3]) / 100)) - 1;
        for (int i = 0; i < dataSet.length; i++) {
            boolean status = false;
            if (i > ratio[0] && i < label[0])
                status = true;
            else if (i > ratio[1] && i < label[0] + label[1])
                status = true;
            else if (i >= ratio[2] && i < label[0] + label[1] + label[2])
                status = true;
            else if (i > ratio[3] && i < label[0] + label[1] + label[2] + label[3])
                status = true;
            if (status) {
                for (int j = 0; j < dataSet[1].length; j++)
                    testingData[index][j] = dataSet[i][j];
                index++;
            }
            if (index == total)
                break;
        }
        return testingData;
    }

    public int[] readLabel(int[][] dataset, int total) {
        int[] temp = new int[total];
        for (int i = 0; i < total; i++)
            temp[i] = dataset[i][2];
        return temp;
    }

    public double getDistance(int[] testingdata_i, int[] trainingdata_j) {
        double sum = 0.0;
        for (int i = 0; i < testingdata_i.length; i++)
            sum += Math.pow((Double.valueOf(testingdata_i[i]) - Double.valueOf(trainingdata_j[i])), 2);
        double result = Math.sqrt(sum);
        return result;
    }

    public double[] getAllDistance(int[] testingdata_i, int[][] trainingdata) {
        int total = trainingdata.length;
        double[] alldistance = new double[total];
        for (int i = 0; i < total; i++)
            alldistance[i] = getDistance(testingdata_i, trainingdata[i]);
        return alldistance;
    }

    public double[][] sortData(double[] allDistance, boolean mode) {
        int total = allDistance.length;
        double[][] result = new double[total][2];
        for (int i = 0; i < total; i++) {
            result[i][0] = allDistance[i];
            result[i][1] = (double) i;
        }
        for (int i = 0; i < total; i++) {
            for (int j = i + 1; j < total; j++) {
                if (mode) {
                    if (result[i][0] > result[j][0])
                        result = swap(result, i, j);
                } else {
                    if (result[i][0] < result[j][0])
                        result = swap(result, i, j);
                }
            }
        }
        return result;
    }

    public double[][] swap(double[][] sortAllDistance, int i, int j) {
        double[][] temp = new double[1][2];

        temp[0][0] = sortAllDistance[i][0];
        temp[0][1] = sortAllDistance[i][1];

        sortAllDistance[i][0] = sortAllDistance[j][0];
        sortAllDistance[i][1] = sortAllDistance[j][1];

        sortAllDistance[j][0] = temp[0][0];
        sortAllDistance[j][1] = temp[0][1];

        return sortAllDistance;
    }

    public int getKNN(int k, double[][] sortAllDistance, int[] trainingLabel) {
        int sum1, sum2, sum3, sum4, result;
        sum1 = sum2 = sum3 = sum4 = result = 0;
        for (int i = 0; i < k; i++) {
            if (trainingLabel[(int) sortAllDistance[i][1]] == 1)
                sum1++;
            else if (trainingLabel[(int) sortAllDistance[i][1]] == 2)
                sum2++;
            else if (trainingLabel[(int) sortAllDistance[i][1]] == 3)
                sum3++;
            else if (trainingLabel[(int) sortAllDistance[i][1]] == 4)
                sum4++;
        }
        if (sum1 >= sum2 && sum1 >= sum3 && sum1 >= sum4)
            result = 1;
        else if (sum2 >= sum1 && sum2 >= sum3 && sum2 >= sum4)
            result = 2;
        else if (sum3 >= sum1 && sum3 >= sum2 && sum3 >= sum4)
            result = 3;
        else if (sum4 >= sum1 && sum4 >= sum2 && sum4 >= sum3)
            result = 4;

        return result;
    }

    public double getError(int[] testingLabel, int[] classificationResult) {
        int error = 0;
        int total = testingLabel.length;
        for (int i = 0; i < total; i++) {
            if (testingLabel[i] != classificationResult[i])
                error++;
        }
        double result = ((double) error) * 100 / ((double) total);
        return result;
    }

    public void printLabel(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i] + "\t" + b[i]);
        }
    }
}