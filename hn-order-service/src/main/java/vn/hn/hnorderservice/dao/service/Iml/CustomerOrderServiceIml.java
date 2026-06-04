package vn.hn.hnorderservice.dao.service.Iml;

import org.springframework.stereotype.Service;
import vn.hn.hnorderservice.dao.model.CustomerOrder;
import vn.hn.hnorderservice.dao.service.CustomerOrderRepo;
import vn.hn.hnorderservice.dao.service.CustomerOrderService;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerOrderServiceIml implements CustomerOrderService {
	
	private final CustomerOrderRepo repo;
	
	public CustomerOrderServiceIml(CustomerOrderRepo repo) {
		this.repo = repo;
	}
	
	@Override
	public CustomerOrder save(CustomerOrder order) {
		return repo.save(order);
	}
	
	@Override
	public List<CustomerOrder> findByDeletedFalseOrderByIdDesc() {
		return repo.findByDeletedFalseOrderByIdDesc();
	}
	
	@Override
	public Optional<CustomerOrder> findByIdAndDeletedFalse(Long id) {
		return repo.findByIdAndDeletedFalse(id);
	}
	
	@Override
	public Optional<CustomerOrder> findById(Long id) {
		return repo.findById(id);
	}
}
