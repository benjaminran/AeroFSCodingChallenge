package test;

import static org.junit.Assert.*;
import java.lang.management.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.junit.Test;
import com.aerofs.codingchallenge.ComparisonSearch;
import com.aerofs.codingchallenge.SearchResult;
import com.aerofs.codingchallenge.ComparisonSearch.ResultType;
import com.aerofs.codingchallenge.ComparisonSearch.SearchType;

public class DynamicTest {
	private static final int[] ARRAY_SIZES = new int[]{10,100,1000,10000,100000,1000000};
	private static final int NUM_TRIALS = 10;
	private long[] performanceData;
	private static Random random;

	/*
	 * Tests the performance of search given different-sized input arrays
	 */
	@Test
	public void dynamicTest() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean(); // bean used for capturing cpu time.
		performanceData = new long[ARRAY_SIZES.length];
		Boolean cpuTimeSupported = bean.isCurrentThreadCpuTimeSupported();
		random = new Random();
		int[] items;
		for(int x=0; x<NUM_TRIALS; x++) {
			for(int size : ARRAY_SIZES) {
				// Generate array
				items = generateArray(size);
				
				for(Boolean ascending : new Boolean[]{true,false}) {
					if(!ascending) items = reverseArray(items);
					
					long startTime = (cpuTimeSupported) ? bean.getCurrentThreadCpuTime() : 0;
					
					/* Key is element in the array */
					
					// Select key
					int keyIndex = random.nextInt(items.length);
					int key = items[keyIndex];
					SearchResult expectedResult = new SearchResult(keyIndex, ResultType.FOUND_EXACT);
					SearchResult searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.LESS_THAN_EQUALS);
					assertTrue(searchResult.equals(expectedResult));
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.EQUALS);
					assertTrue(searchResult.equals(expectedResult));
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.GREATER_THAN_EQUALS);
					assertTrue(searchResult.equals(expectedResult));
					
					SearchType searchType = (ascending) ? SearchType.LESS_THAN : SearchType.GREATER_THAN;
					ResultType resultType = (ascending) ? ResultType.FOUND_LESS : ResultType.FOUND_GREATER;
					
					searchResult = ComparisonSearch.Search(items, ascending, key, searchType);
					if(keyIndex!=0) {
						expectedResult = new SearchResult(keyIndex-1, resultType);
						assertTrue(searchResult.equals(expectedResult));
					}
					else {
						expectedResult = new SearchResult(-1, ResultType.NOT_FOUND);
						assertTrue(searchResult.equals(expectedResult));
					}
	
					searchType = (ascending) ? SearchType.GREATER_THAN : SearchType.LESS_THAN;
					resultType = (ascending) ? ResultType.FOUND_GREATER : ResultType.FOUND_LESS;
					
					searchResult = ComparisonSearch.Search(items, ascending, key, searchType);
					if(keyIndex!=items.length-1) {
						expectedResult = new SearchResult(keyIndex+1, resultType);
						assertTrue(searchResult.equals(expectedResult));
					}
					else {
						expectedResult = new SearchResult(-1, ResultType.NOT_FOUND);
						assertTrue(searchResult.equals(expectedResult));
					}
					
					
					/* Key is not element in the array */
					
					// Select key within array borders
					int leftIndex = 0, rightIndex = 0;
					key = 0;
					while(key==0) {
						leftIndex = random.nextInt(items.length-1);
						rightIndex = leftIndex+1;
						if(Math.abs(items[rightIndex]-items[leftIndex])>1) {
							if(ascending) key = items[leftIndex] + random.nextInt(items[rightIndex]-items[leftIndex]-1) + 1;
							else key = items[rightIndex] + random.nextInt(items[leftIndex]-items[rightIndex]-1) + 1;
						}
					}
					
					searchType = (ascending) ? SearchType.GREATER_THAN : SearchType.LESS_THAN;
					resultType = (ascending) ? ResultType.FOUND_GREATER : ResultType.FOUND_LESS;
					
					expectedResult = new SearchResult(rightIndex, resultType);
					searchResult = ComparisonSearch.Search(items, ascending, key, searchType);
					assertTrue(searchResult.equals(expectedResult));
					
					searchType = (ascending) ? SearchType.GREATER_THAN_EQUALS : SearchType.LESS_THAN_EQUALS;
					searchResult = ComparisonSearch.Search(items, ascending, key, searchType);
					assertTrue(searchResult.equals(expectedResult)); // expect same result as without EQUALS
					
					searchType = (ascending) ? SearchType.LESS_THAN : SearchType.GREATER_THAN;
					resultType = (ascending) ? ResultType.FOUND_LESS : ResultType.FOUND_GREATER;
					
					expectedResult = new SearchResult(leftIndex, resultType);
					searchResult = ComparisonSearch.Search(items, ascending, key, searchType);
					assertTrue(searchResult.equals(expectedResult));
					
					searchType = (ascending) ? SearchType.LESS_THAN_EQUALS : SearchType.GREATER_THAN_EQUALS;
					searchResult = ComparisonSearch.Search(items, ascending, key, searchType);
					assertTrue(searchResult.equals(expectedResult)); // expect same result as without EQUALS
					
					// Searching for EQUALS should always return NOT_FOUND
					expectedResult = new SearchResult(-1, ResultType.NOT_FOUND);
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.EQUALS);
					assertTrue(searchResult.equals(expectedResult));
					
					// Select key greater than all elements in array
					key = (ascending) ? items[items.length-1] + random.nextInt(Integer.MAX_VALUE-items[items.length-1]) : items[0] + random.nextInt(Integer.MAX_VALUE-items[0]);
					
					// All searches should return NOT_FOUND except for LESS_THAN(_EQUALS)
					expectedResult = new SearchResult(-1, ResultType.NOT_FOUND);
					
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.GREATER_THAN);
					assertTrue(searchResult.equals(expectedResult));
					
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.GREATER_THAN_EQUALS);
					assertTrue(searchResult.equals(expectedResult));
					
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.EQUALS);
					assertTrue(searchResult.equals(expectedResult));
					
					// Searches for LESS_THAN(_EQUALS) should return largest element 
					int maxIndex = (ascending) ? items.length - 1 : 0;
					expectedResult = new SearchResult(maxIndex, ResultType.FOUND_LESS);
					
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.LESS_THAN);
					assertTrue(searchResult.equals(expectedResult));
					
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.LESS_THAN_EQUALS);
					assertTrue(searchResult.equals(expectedResult));
					
					
					// Select key less than all elements in array
					key = (ascending) ? items[0] - random.nextInt(Integer.MAX_VALUE) : items[items.length-1] - random.nextInt(Integer.MAX_VALUE); // Breaks in some cases if items has negative numbers
					
					// All searches should return NOT_FOUND except for GREATER_THAN(_EQUALS)
					expectedResult = new SearchResult(-1, ResultType.NOT_FOUND);
					
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.LESS_THAN);
					assertTrue(searchResult.equals(expectedResult));
					
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.LESS_THAN_EQUALS);
					assertTrue(searchResult.equals(expectedResult));
					
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.EQUALS);
					assertTrue(searchResult.equals(expectedResult));
					
					// Searches for GREATER_THAN(_EQUALS) should return largest element 
					int lowestIndex = (ascending) ? 0 : items.length - 1;
					expectedResult = new SearchResult(lowestIndex, ResultType.FOUND_GREATER);
					
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.GREATER_THAN);
					assertTrue(searchResult.equals(expectedResult));
					
					searchResult = ComparisonSearch.Search(items, ascending, key, SearchType.GREATER_THAN_EQUALS);
					assertTrue(searchResult.equals(expectedResult));
					
					
					long endTime = (cpuTimeSupported) ? bean.getCurrentThreadCpuTime() : 0;
					if(cpuTimeSupported) {
						performanceData[Arrays.binarySearch(ARRAY_SIZES, size)] = endTime-startTime;
					}
				}
			}
		}
			
		if(cpuTimeSupported) {
			for(int i=0; i< ARRAY_SIZES.length; i++) {
				System.out.println("Average (" + NUM_TRIALS + " iterations) cpu time spent searching each " + ARRAY_SIZES[i] + "-element array: " + performanceData[i]/NUM_TRIALS/1000000000.0 + " seconds.");
			}
		}
	}

	/*
	 * Generates an ascending array of size n with random distances (from 1 to 10) between adjacent elements
	 */
	private static int[] generateArray(int size) {
		int[] items = new int[size];
		// Populate array
		items[0] = random.nextInt(10);
		for(int i=1;i<items.length;i++) items[i] = items[i-1]+random.nextInt(10)+1;
		return items;
	}
	
	/*
	 * Reverses an array of size n
	 */
	private static int[] reverseArray(int[] items) {
		int[] reversed = new int[items.length];
		int temp;
		int lastIndex = items.length - 1;
		for(int i = 0; i < items.length; i++) {
			reversed[i] = items[lastIndex-i];
		}
		return reversed;
	}
	
	private static boolean contains(final int[] array, final int key) {
		for (final int i : array) {
			if (i == key) {
				return true;
			}
		}
		return false;
	}
}
