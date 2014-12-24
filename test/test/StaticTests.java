package test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.aerofs.codingchallenge.ComparisonSearch;
import com.aerofs.codingchallenge.SearchResult;
import com.aerofs.codingchallenge.ComparisonSearch.ResultType;
import com.aerofs.codingchallenge.ComparisonSearch.SearchType;

/**
 * Basic functional test from example data training set
 */
public class StaticTests {

	@Test
	public void LessThanTest() {
		int[] items = new int[]{0, 2, 4, 6, 8};
		SearchResult result = ComparisonSearch.Search(items, true, 0,SearchType.LESS_THAN);
		assertTrue("Search did not return NOT_FOUND for LESS_THAN 0 in {0, 2, 4, 6, 8}", result.getType()==ResultType.NOT_FOUND);
		
		items = new int[]{8, 6, 4, 2, 0};
		result = ComparisonSearch.Search(items, false, 0, SearchType.LESS_THAN);
		assertTrue("Search did not return NOT_FOUND for LESS_THAN 0 in {8, 6, 4, 2, 0}", result.getType()==ResultType.NOT_FOUND);
	}
	
	@Test
	public void LessThanEqualsTest() {
		int[] items = new int[]{0, 2, 4, 6, 8};
		SearchResult result = ComparisonSearch.Search(items, true, -1, SearchType.LESS_THAN_EQUALS);
		assertTrue("Search did not return NOT_FOUND for -1 in {0, 2, 4, 6, 8}", result.getType()==ResultType.NOT_FOUND);
		
		items = new int[]{8, 6, 4, 2, 0};
		result = ComparisonSearch.Search(items, false, 4, SearchType.LESS_THAN_EQUALS);
		assertTrue("Search did not return FOUND_EXACT (2) for LESS_THAN_EQUALS 4 in {8, 6, 4, 2, 0}", result.getType()==ResultType.FOUND_EXACT && result.getIndex()==2);
	}
	
	@Test
	public void equalsTest() {
		int[] items = new int[]{0, 2, 4, 6, 8};
		// Test a value not in the array
		SearchResult result = ComparisonSearch.Search(items, true, 1,SearchType.EQUALS);
		assertTrue("Search did not return NOT_FOUND for 1 in {0, 2, 4, 6, 8}", result.getType()==ResultType.NOT_FOUND);
		
		// Test a value in the array
		result = ComparisonSearch.Search(items, true, 0, SearchType.EQUALS);
		assertTrue("Search did not return FOUND_EXACT (0) for 0 in {0, 2, 4, 6, 8}", result.getType()==ResultType.FOUND_EXACT && result.getIndex()==0);
	}
	
	@Test
	public void greaterThanEqualsTest() {
		int[] items = new int[]{0, 2, 4, 6, 8};
		SearchResult result = ComparisonSearch.Search(items, true, 2,SearchType.GREATER_THAN_EQUALS);
		assertTrue("Search did not return FOUND_EXACT (1) for GREATER_THAN_EQUALS 2 in {0, 2, 4, 6, 8}", result.getType()==ResultType.FOUND_EXACT && result.getIndex()==1);
		
		items = new int[]{8, 6, 4, 2, 0};
		result = ComparisonSearch.Search(items, false, 2, SearchType.GREATER_THAN_EQUALS);
		assertTrue("Search did not return FOUND_EXACT (3) for GREATER_THAN_EQUALS 2 in {8, 6, 4, 2, 0}", result.getType()==ResultType.FOUND_EXACT && result.getIndex()==3);
	}
	
	@Test
	public void greaterThanTest() {
		int[] items = new int[]{0, 2, 4, 6, 8};
		SearchResult result = ComparisonSearch.Search(items, true, 2, SearchType.GREATER_THAN);
		assertTrue("Search did not return FOUND_GREATER (2) for GREATER_THAN 2 in {0, 2, 4, 6, 8}", result.getType()==ResultType.FOUND_GREATER && result.getIndex()==2);
		
		items = new int[]{8, 6, 4, 2, 0};
		result = ComparisonSearch.Search(items, false, 8, SearchType.GREATER_THAN);
		assertTrue("Search did not return NOT_FOUND for GREATER_THAN 8 in {8, 6, 4, 2, 0}", result.getType()==ResultType.NOT_FOUND);
	}

}
