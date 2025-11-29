package vn.hn.hncoreservice.data.criteria;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.hn.hncommonservice.entity.SearchCriteria;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleSearchCriteria extends SearchCriteria {
	private String name;
}
