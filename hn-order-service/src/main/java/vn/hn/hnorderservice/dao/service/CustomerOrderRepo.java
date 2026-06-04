package vn.hn.hnorderservice.dao.service;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hn.hnorderservice.dao.model.CustomerOrder;

import java.util.List;
import java.util.Optional;

public interface CustomerOrderRepo extends JpaRepository<CustomerOrder, Long> {
	List<CustomerOrder> findByDeletedFalseOrderByIdDesc();
	
	Optional<CustomerOrder> findByIdAndDeletedFalse(Long id);
}
