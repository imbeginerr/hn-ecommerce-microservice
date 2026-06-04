package vn.hn.hnorderservice.dao.service;

import vn.hn.hnorderservice.dao.model.CustomerOrder;

import java.util.List;
import java.util.Optional;

public interface CustomerOrderService {
	CustomerOrder save(CustomerOrder order);
	
	List<CustomerOrder> findByDeletedFalseOrderByIdDesc();
	
	Optional<CustomerOrder> findByIdAndDeletedFalse(Long id);
	
	Optional<CustomerOrder> findById(Long id);
}
