package ra.edu.presentation;

import ra.edu.business.model.Invoice;
import ra.edu.business.model.InvoiceDetail;
import ra.edu.business.model.Product;
import ra.edu.business.service.invoice.InvoiceService;
import ra.edu.business.service.invoice.InvoiceServiceImp;
import ra.edu.business.service.product.ProductService;
import ra.edu.business.service.product.ProductServiceImp;
import ra.edu.business.dao.invoiceDetail.InvoiceDetailDAO;
import ra.edu.business.dao.invoiceDetail.InvoiceDetailDAOImp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class InvoiceDetailUI {
    private final InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAOImp();
    private final InvoiceService invoiceService = new InvoiceServiceImp();
    private final InvoiceServiceImp invoiceServiceImp = new InvoiceServiceImp();
    private final ProductService productService = new ProductServiceImp();
    private final Scanner scanner = new Scanner(System.in);

    public void menuInvoiceDetail() {
        int choice;
        do {
            System.out.println("\u001B[34m========== QUẢN LÝ CHI TIẾT HÓA ĐƠN ==========\u001B[0m");
            System.out.println("|1. Thêm chi tiết hóa đơn                   |");
            System.out.println("|2. Hiển thị chi tiết hóa đơn theo hóa đơn  |");
            System.out.println("|0. Quay lại                                |");
            System.out.println("=============================================");
            System.out.print("Lựa chọn của bạn: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    addInvoiceDetail();
                    break;
                case 2:
                    displayInvoiceDetailsByInvoice();
                    break;
                case 0:
                    break;
                default:
                    System.err.println("Lựa chọn không hợp lệ.");
                    break;
            }
        } while (choice != 0);
    }

    private void addInvoiceDetail() {
        try {
            System.out.println("\u001B[34m========= THÊM CHI TIẾT HÓA ĐƠN =========\u001B[0m");
            // Display all invoices
            List<Invoice> invoices = invoiceService.getAllInvoices();
            if (invoices.isEmpty()) {
                System.err.println("Chưa có hóa đơn nào trong hệ thống.");
                return;
            }

            System.out.println("\u001B[36m=================== DANH SÁCH HÓA ĐƠN ===================\u001B[0m");
            System.out.printf("\u001B[36m| %-5s | %-20s | %-12s | %-10s |\u001B[0m%n",
                    "ID", "Khách hàng", "Tổng tiền", "Trạng thái");
            System.out.println("\u001B[36m----------------------------------------------------\u001B[0m");
            for (Invoice invoice : invoices) {
                System.out.printf("\u001B[32m| %-5d | %-20s | %-12.2f | %-10s |\u001B[0m%n",
                        invoice.getInvoiceId(), invoice.getCustomerName(), invoice.getTotalAmount(), invoice.getStatus());
            }

            System.out.print("Nhập ID hóa đơn: ");
            int invoiceId;
            try {
                invoiceId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("ID hóa đơn không hợp lệ.");
                return;
            }

            Invoice invoice = invoiceService.getInvoiceById(invoiceId);
            if (invoice == null) {
                System.err.println("Không tìm thấy hóa đơn với ID " + invoiceId);
                return;
            }

            List<Product> products = productService.findAllProducts();
            if (products.isEmpty()) {
                System.err.println("Chưa có sản phẩm nào trong hệ thống.");
                return;
            }

            System.out.println("\u001B[36m======================== DANH SÁCH SẢN PHẨM ========================\u001B[0m");
            System.out.printf("\u001B[36m| %-5s | %-30s | %-10s | %-10s |\u001B[0m%n", "ID", "Tên sản phẩm", "Giá", "Tồn kho");
            System.out.println("\u001B[36m--------------------------------------------------------------------\u001B[0m");
            for (Product p : products) {
                System.out.printf("\u001B[32m| %-5d | %-30s | %-10.2f | %-10d |\u001B[0m%n",
                        p.getProId(), p.getProName(), p.getProPrice(), p.getStock());
            }

            System.out.print("Nhập ID sản phẩm: ");
            int productId;
            try {
                productId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("ID sản phẩm không hợp lệ.");
                return;
            }

            Product selectedProduct = productService.findProductById(productId);
            if (selectedProduct == null) {
                System.err.println("Không tìm thấy sản phẩm với ID " + productId);
                return;
            }

            if (selectedProduct.getStock() <= 0) {
                System.err.println("Sản phẩm đã hết hàng.");
                return;
            }

            if (invoiceDetailDAO.checkProductExistsInInvoice(invoiceId, productId)) {
                System.err.println("Sản phẩm đã tồn tại trong hóa đơn. Vui lòng cập nhật số lượng nếu cần.");
                return;
            }

            System.out.print("Nhập số lượng: ");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity <= 0) {
                    System.err.println("Số lượng phải lớn hơn 0.");
                    return;
                }
                if (quantity > selectedProduct.getStock()) {
                    System.err.println("Số lượng vượt quá tồn kho (hiện có: " + selectedProduct.getStock() + ")");
                    return;
                }
            } catch (NumberFormatException e) {
                System.err.println("Số lượng không hợp lệ.");
                return;
            }

            InvoiceDetail detail = new InvoiceDetail();
            detail.setInvoiceId(invoiceId);
            detail.setProId(productId);
            detail.setQuantity(quantity);
            detail.setUnitPrice(selectedProduct.getProPrice());

            boolean added = invoiceDetailDAO.addInvoiceDetail(detail);
            if (added) {
                double newTotalAmount = invoice.getTotalAmount() + (quantity * selectedProduct.getProPrice());
                invoice.setTotalAmount(newTotalAmount);
                invoiceService.updateInvoiceStatus(invoiceId, invoice.getStatus());
                System.out.println("\u001B[32mThêm chi tiết hóa đơn thành công!\u001B[0m");
            } else {
                System.err.println("Thêm chi tiết hóa đơn thất bại!");
            }
        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi khi thêm chi tiết hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayInvoiceDetailsByInvoice() {
        System.out.println("\u001B[34m========= HIỂN THỊ CHI TIẾT HÓA ĐƠN =========\u001B[0m");
        System.out.print("Nhập ID hóa đơn: ");
        int invoiceId;
        try {
            invoiceId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("ID hóa đơn không hợp lệ.");
            return;
        }

        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        if (invoice == null) {
            System.err.println("Không tìm thấy hóa đơn với ID " + invoiceId);
            return;
        }

        List<InvoiceDetail> details = invoiceDetailDAO.getDetailsByInvoiceId(invoiceId);
        if (details.isEmpty()) {
            System.out.println("Hóa đơn này không có chi tiết nào.");
            return;
        }

        System.out.println("\u001B[32m===== THÔNG TIN HÓA ĐƠN =====\u001B[0m");
        System.out.println("ID hóa đơn: " + invoice.getInvoiceId());
        System.out.println("Khách hàng: " + invoice.getCustomerName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateString = (invoice.getCreateAt() != null) ? dateFormat.format(invoice.getCreateAt()) : "N/A";
        System.out.println("Ngày tạo: " + dateString);
        System.out.println("Tổng tiền: " + invoice.getTotalAmount() + " VND");
        System.out.println("Trạng thái: " + invoice.getStatus());

        System.out.println("\n\u001B[32m===== CHI TIẾT HÓA ĐƠN =====\u001B[0m");
        System.out.println("\u001B[36m==================================================================================\u001B[0m");
        System.out.printf("\u001B[36m| %-5s | %-30s | %-8s | %-12s | %-12s |\u001B[0m%n",
                "STT", "Sản phẩm", "Số lượng", "Đơn giá", "Thành tiền");
        System.out.println("\u001B[36m==================================================================================\u001B[0m");

        int count = 1;
        double calculatedTotal = 0.0;

        for (InvoiceDetail detail : details) {
            Product product = productService.findProductById(detail.getProId());
            String productName = (product != null) ? product.getProName() : "Sản phẩm #" + detail.getProId();
            double amount = detail.getQuantity() * detail.getUnitPrice();
            calculatedTotal += amount; // Accumulate the total

            System.out.printf("| %-5d | %-30s | %-8d | %-12.2f | %-12.2f |\n",
                    count++, productName, detail.getQuantity(), detail.getUnitPrice(), amount);
        }

        System.out.println("\u001B[36m==================================================================================\u001B[0m");

        System.out.printf("\u001B[32mTổng cộng: %.2f VND\u001B[0m\n", calculatedTotal);
    }
}