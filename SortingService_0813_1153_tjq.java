// 代码生成时间: 2025-08-13 11:53:19
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortingService {

    /**
     * Sorts the list using Java's built-in sort functionality.
     *
     * @param list The list to sort.
     * @return The sorted list.
     * @throws IllegalArgumentException if the list is null.
     */
    public List<Integer> sortList(List<Integer> list) {
        if (list == null) {
            throw new IllegalArgumentException("List cannot be null");
        }

        Collections.sort(list);
        return list;
    }

    /**
     * Sorts the list using a custom bubble sort algorithm.
     *
     * @param list The list to sort.
     * @return The sorted list.
     * @throws IllegalArgumentException if the list is null.
     */
    public List<Integer> bubbleSortList(List<Integer> list) {
        if (list == null) {
            throw new IllegalArgumentException("List cannot be null");
        }

        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    // Swap temp and list[i]
                    int temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }

        return list;
    }

    public static void main(String[] args) {
        SortingService sortingService = new SortingService();

        List<Integer> numbers = Arrays.asList(5, 3, 8, 4, 2);
        System.out.println("Original List: " + numbers);

        try {
            List<Integer> sortedNumbers = sortingService.sortList(numbers);
            System.out.println("Sorted List (Using Java's sort): " + sortedNumbers);

            List<Integer> bubbleSortedNumbers = sortingService.bubbleSortList(new ArrayList<>(numbers));
            System.out.println("Sorted List (Using Bubble Sort): " + bubbleSortedNumbers);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}