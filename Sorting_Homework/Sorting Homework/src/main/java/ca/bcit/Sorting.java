package ca.bcit;
import edu.princeton.cs.algs4.Stopwatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Homework - Sorting
 * Sort the list of doubles in the fastest possible way.
 * The only method you can change is the sort() method.
 * You can add additional methods if needed, without changing the load() and test() methods.
 */
public class Sorting {

    protected List list = new ArrayList<Integer>();

    /**
     * Loading the text files with double numbers
     */
    protected void load(){
        try (Stream<String> stream = Files.lines(Paths.get("numbers.txt"))) {
            stream.forEach(x -> list.add(Integer.parseInt(x)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Testing of your solution, using 100 shuffled examples
     * @return execution time
     */
    protected double test(){
        Stopwatch watch = new Stopwatch();
        for (int i=0; i<100; i++){
            Collections.shuffle(list, new Random(100));
            sort(list);
        }
        return watch.elapsedTime();
    }
    /**
     * Sorting method - add your code in here
     * @param list - list to be sorted
     */
    private void sort(List list){
        radixSort(list);
    }

    // Methods below are implementing the quick sort algorithm
    private void quickSort(List<Integer> list){
        quickSort(list, 0, list.size()-1);
    }
    private void quickSort(List<Integer> list, int low, int high){

        if (low<high){
            int partitionIx = partition(list, low, high);
            quickSort(list, low, partitionIx-1);
            quickSort(list, partitionIx+1, high);
        }

    }
    private int partition(List<Integer> list, int low, int high){
        // Choosing the pivot as the last element
        //Note: I tried choosing the pivot as the first element and it gave almost same result
        int pivot =list.get(high);
        int partitionIndex = low;
        for (int i=low; i<high; i++){
            if(list.get(i) < pivot) {
                swap(list, i, partitionIndex);
                partitionIndex++;
            }
        }
        swap(list, partitionIndex, high);
        return partitionIndex;
    }
    private void swap(List<Integer> list, int ix1, int ix2){
        /*
        Swaps two elements in the array. This operation happens a lot in quick sort.
         */
        int temp = list.get(ix1);
        list.set(ix1, list.get(ix2));
        list.set(ix2, temp);
    }


    // Methods below are implementing the merge sort algorithm
    private void mergeSort(List list){
        Object[] objectArray = list.toArray();
        Integer[] integerArray = Arrays.copyOf(objectArray,
                objectArray.length,
                Integer[].class);
        List tempList = mergeSort(integerArray, new Integer[integerArray.length], 0, integerArray.length-1);
        Collections.copy(list, tempList);
    }
    private List mergeSort(Integer[] array, Integer[] temp, int low, int high){
        if(low < high){
            int midPoint = (high + low) / 2;
            mergeSort(array, temp, low, midPoint);
            mergeSort(array, temp, midPoint+1, high);
            mergeHalves(array, temp, low, high);
        }
        return Arrays.asList(array);
    }
    private void mergeHalves(Integer[] array, Integer[] temp, int low, int high) {
        int leftHigh = (high + low) / 2;
        int rightLow = leftHigh + 1;

        int left = low;
        int right = rightLow;
        int index = low;

        while(left <= leftHigh && right <= high){
            if(array[left] <= array[right]){
                temp[index] = array[left];
                left++;
            }else{
                temp[index] = array[right];
                right++;
            }
            index++;
        }
        System.arraycopy(array, left, temp, index, leftHigh - left + 1);
        System.arraycopy(array, right, temp, index, high - right + 1);
        System.arraycopy(temp, low, array, low, high - low + 1);
    }


    // Methods below are implementing the radix sort algorithm
    private int getMax(int[] arr, int n) {
        int mx = arr[0];
        for (int i = 1; i < n; i++)
            if (arr[i] > mx)
                mx = arr[i];
        return mx;
    }
    private void countSort(int[] arr, int n, int exp) {
        int[] output = new int[n]; // output array
        int i;
        int[] count = new int[10];
        Arrays.fill(count, 0);

        for (i = 0; i < n; i++)
            count[(arr[i] / exp) % 10]++;


        for (i = 1; i < 10; i++)
            count[i] += count[i - 1];


        for (i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }


        for (i = 0; i < n; i++)
            arr[i] = output[i];
    }
    private void radixSort(List<Integer> list){
        int n = list.size();
        int[] arr = new int[n];
        for (int i=0; i<n; i++)
            arr[i] = list.get(i);
        int[] sortedArray = radixSort(arr, n);
        for(int i=0;i<n;i++)
            list.set(i, sortedArray[i]);
    }
    private int[] radixSort(int[] arr, int n) {
        int m = getMax(arr, n);

        for (int exp = 1; m / exp > 0; exp *= 10)
            countSort(arr, n, exp);
        return arr;
    }

    // Java's dual pivot quicksort test
    private void javaSortPrimitives(List<Integer> list){
        int n = list.size();
        int[] arr = new int[n];
        for (int i=0; i<n; i++)
            arr[i] = list.get(i);

        Arrays.sort(arr);

        for(int i=0; i<n; i++)
            list.set(i, arr[i]);
    }
}