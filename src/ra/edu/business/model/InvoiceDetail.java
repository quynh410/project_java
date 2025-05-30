package ra.edu.business.model;

import ra.edu.utils.IApp;

import java.util.Scanner;

public class InvoiceDetail implements IApp {
    private int invoiceDetailId;
    private int invoiceId;
    private int proId;
    private int quantity;
    private double unitPrice;

    public InvoiceDetail() {
    }

    public InvoiceDetail(int invoiceDetailId, int invoiceId, int proId, int quantity, double unitPrice) {
        this.invoiceDetailId = invoiceDetailId;
        this.invoiceId = invoiceId;
        this.proId = proId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getInvoiceDetailId() {
        return invoiceDetailId;
    }

    public void setInvoiceDetailId(int invoiceDetailId) {
        this.invoiceDetailId = invoiceDetailId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return "InvoiceDetail{" +
                "invoiceDetailId=" + invoiceDetailId +
                ", invoiceId=" + invoiceId +
                ", proId=" + proId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }

    @Override
    public void inputData(Scanner sc) {
        System.out.print("Nhập mã hóa đơn: ");
        this.invoiceId = Integer.parseInt(sc.nextLine());

        System.out.print("Nhập mã sản phẩm: ");
        this.proId = Integer.parseInt(sc.nextLine());

        System.out.print("Nhập số lượng: ");
        this.quantity = Integer.parseInt(sc.nextLine());

        System.out.print("Nhập đơn giá: ");
        this.unitPrice = Double.parseDouble(sc.nextLine());
    }

}
