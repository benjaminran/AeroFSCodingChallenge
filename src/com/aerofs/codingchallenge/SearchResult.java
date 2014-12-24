package com.aerofs.codingchallenge;

import com.aerofs.codingchallenge.ComparisonSearch.ResultType;

/**
 * Wrapper class for return data from search
 * 
 * index: the matched element's index
 * type: the type of match found
 */
public class SearchResult {
	private int index;
	private ResultType type;
	
	public SearchResult(int index, ResultType type) {
		this.index = index;
		this.type = type;
	}
	
	public SearchResult(int index){ 
		this.index = index;
		type = null;
	}
	
	public void setIndex(int index) { this.index = index; }
	public void setResultType(ResultType type) { this.type = type; }
	
	public int getIndex() { return index; }
	public ResultType getType() { return type; }

	public Boolean equals(SearchResult searchResult) {
		if((type==null || searchResult.getType()==null) && searchResult.getIndex()==index) return true;
		else if(searchResult.getIndex()==index && searchResult.getType()==type) return true;
		else return false;
	}
}
