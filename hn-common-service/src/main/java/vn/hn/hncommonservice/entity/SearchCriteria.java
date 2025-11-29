package vn.hn.hncommonservice.entity;

import lombok.Data;

/**
 * Base search criteria cho pagination và sorting Tất cả search requests đều extend class này
 */
@Data
public abstract class SearchCriteria {
	
	/**
	 * Page number (0-based)
	 */
	private Integer page = 0;
	
	/**
	 * Page size (số records mỗi page)
	 */
	private Integer size = 20;
	
	/**
	 * Sort field (tên field để sort)
	 */
	private String sortBy = "id";
	
	/**
	 * Sort direction (ASC hoặc DESC)
	 */
	private String sortDir = "DESC";
	
	/**
	 * Keyword search (tìm kiếm chung)
	 */
	private String keyword;
}
