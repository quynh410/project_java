package ra.edu.business.service.customer;

import ra.edu.business.dao.customer.CustomerDAO;
import ra.edu.business.dao.customer.CustomerDAOImp;
import ra.edu.business.model.Customer;

import java.util.List;

public class CustomerServiceImp implements CustomerService {
    private CustomerDAO customerDAO = new CustomerDAOImp();

    @Override
    public boolean displayAllCustomers() {
        List<Customer> list = customerDAO.findAll();
        for (Customer c : list) {
            System.out.println(c);
        }
        return false;
    }

    @Override
    public boolean addCustomers(Customer customer) {
        customerDAO.add(customer);
        return false;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        customerDAO.update(customer);
        return false;
    }

    @Override
    public boolean deleteCustomer(int id) {
        Customer customer = findCustomerById(id);
        if (customer != null && !customer.isStatus()) {
            return customerDAO.delete(id);
        } else if (customer != null && customer.isStatus()) {
            System.err.println("Không thể xóa khách hàng có trạng thái hoạt động (true).");
            return false;
        }
        return false;
    }

    @Override
    public List<Customer> findAllCustomers() {
        return customerDAO.findAll();
    }

    @Override
    public Customer findCustomerById(int id) {
        return customerDAO.findById(id);
    }

    @Override
    public boolean createCustomer(Customer customer) {
        customerDAO.add(customer);
        return false;
    }

    @Override
    public boolean modifyCustomer(Customer customer) {
        customerDAO.update(customer);
        return false;
    }
}

