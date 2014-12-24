package com.aerofs.codingchallenge;

public class ComparisonSearch {

	public static enum SearchType {
		LESS_THAN, LESS_THAN_EQUALS, EQUALS, GREATER_THAN_EQUALS, GREATER_THAN
	}
	
	public static enum ResultType {
		NOT_FOUND, FOUND_EXACT, FOUND_GREATER, FOUND_LESS
	}
	
	/**
	 * Search finds the element that most closely matches the search criteria.
	 */
	public static SearchResult Search(int[] items, Boolean ascending, int key, SearchType type) {
		// Iterate ascending for GREATER_THAN(_EQUALS). Descending otherwise.  
		int index, endIndex, increment;
		
		// Set start index
		if(type==SearchType.GREATER_THAN || type==SearchType.GREATER_THAN_EQUALS) {
			index = (ascending) ? 0 : items.length - 1;
		}
		else {
			index = (ascending) ? items.length-1 : 0;
		}
		
		// Set up endIndex and increment value
		if(index == 0 ) {
			endIndex = items.length;
			increment = 1;
		}
		else {
			endIndex = -1;
			increment = -1;
		}
		
		SearchResult result = null;
		// Iterate through array until first match found.
		while(result==null && index!=endIndex) {
			if(items[index]==key && (type==SearchType.LESS_THAN_EQUALS || type==SearchType.EQUALS || type==SearchType.GREATER_THAN_EQUALS)) {
				result = new SearchResult(index, ResultType.FOUND_EXACT);
			}
			if(items[index]<key && (type==SearchType.LESS_THAN || type==SearchType.LESS_THAN_EQUALS)){
				result = new SearchResult(index, ResultType.FOUND_LESS);
			}
			if(items[index]>key && (type==SearchType.GREATER_THAN || type==SearchType.GREATER_THAN_EQUALS)){
				result = new SearchResult(index, ResultType.FOUND_GREATER);
			}
			index += increment;
		}
		
		// Return search result
		if(result==null) result = new SearchResult(-1, ResultType.NOT_FOUND);
		return result;
		
	}


	
}