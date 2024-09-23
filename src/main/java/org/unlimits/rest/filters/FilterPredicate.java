package org.unlimits.rest.filters;

public class FilterPredicate {
	
	private String columnName;

	private Object columnValue;
	
	/**
	 * 
	 */
	public FilterPredicate() {
	}
	
	
	public FilterPredicate(String columnName, Object columnValue) {
		super();
		this.columnName = columnName;
		this.columnValue = columnValue;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Object getColumnValue() {
		if(columnValue==null) {
			columnValue="";
		}
		return columnValue;
	}

	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

	@Override
	public String toString() {
		return "FilterPredicate [columnName=" + columnName + ", columnValue=" + columnValue + "]";
	}

}