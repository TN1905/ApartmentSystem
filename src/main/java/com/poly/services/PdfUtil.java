package com.poly.services;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.poly.entity.Account;
import com.poly.entity.RentApartment;
import com.poly.entity.RentApartmentDetail;
public class PdfUtil {
	public static String createContractPdf(RentApartment rentApart) throws FileNotFoundException {
        String pdfPath = "invoice_" + rentApart.getId() + ".pdf";
        try {
        		double payPrice = rentApart.getApartment().getPrice() * rentApart.getMonthrent();
                PdfWriter writer = new PdfWriter(pdfPath);
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                PdfFont font = PdfFontFactory.createFont("src/main/resources/fonts/arial.ttf",
                                PdfEncodings.IDENTITY_H);
                document.setFont(font);
                float pageSizeWidth = PageSize.A4.getWidth();
                Table headerTable = new Table(new float[] { 1, 1 });
                headerTable.setWidth(pageSizeWidth);

                Paragraph invoiceTitle = new Paragraph("CHEAP HOUSE RENT")
                                .setFontSize(20)
                                .setBold()
                                .setTextAlignment(TextAlignment.LEFT);
                Cell invoiceCell = new Cell().add(invoiceTitle).setBorder(null).setPadding(10);
                headerTable.addCell(invoiceCell);

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                Paragraph invoiceInfo = new Paragraph()
                                .add("Invoice No: RK356748\n")
                                .add("Date: " + LocalDate.now().format(dateFormatter) + "\n")
                                .setWidth(250)
                                .setTextAlignment(TextAlignment.RIGHT);
                Cell infoCell = new Cell().add(invoiceInfo).setBorder(null).setPadding(10);
                headerTable.addCell(infoCell);

                document.add(headerTable);

                // Thêm dòng kẻ ngang
                Div line = new Div();
                line.setHeight(3).setWidth(UnitValue.createPercentValue(100))
                                .setBackgroundColor(DeviceRgb.BLACK);
                document.add(line);
                // Thông tin người thuê

                Account tenant = rentApart.getAccount();
                document.add(new Paragraph("THÔNG TIN NGƯỜI THUÊ").setBold()
                                .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph().add(new Text("Tên: ").setBold())
                                .add(tenant.getFirstname() + " " + tenant.getLastname()));
                document.add(new Paragraph().add(new Text("Email: ").setBold()).add(tenant.getEmail()));
                document.add(new Paragraph().add(new Text("Số điện thoại: ").setBold())
                                .add(String.valueOf(tenant.getPhone())));
                document.add(new Paragraph().add(new Text("Tên đăng nhập: ").setBold())
                                .add(tenant.getUsername()));
                // Thêm dòng kẻ ngang
                document.add(line);

                // Thông tin đơn hàng
                document.add(new Paragraph("THÔNG TIN ĐƠN HÀNG").setBold()
                                .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph().add(new Text("Mã đơn hàng: ").setBold())
                                .add(String.valueOf(rentApart.getId())));
                document.add(new Paragraph().add(new Text("Số tiền thanh toán: ").setBold())
                                .add(String.valueOf(payPrice) + "đ"));
                document.add(new Paragraph().add(new Text("Phí giao dịch: ").setBold()).add("0đ"));
                document.add(new Paragraph().add(new Text("Địa chỉ: ").setBold())
                                .add(rentApart.getApartment().getAddress() + ", "
                                                + rentApart.getApartment().getWard()
                                                + ", "
                                                + rentApart.getApartment().getDistrict()
                                                + ", "
                                                + rentApart.getApartment()
                                                                .getCity()));
                document.add(new Paragraph().add(new Text("Loại căn hộ: ").setBold())
                                .add(rentApart.getApartment()
                                                .getApartmentType().getName()));
                document.add(new Paragraph().add(new Text("Diện tích: ").setBold())
                                .add(String.valueOf(rentApart.getApartment().getAcreage())
                                                + " m²"));
                document.add(new Paragraph().add(new Text("Số tháng thuê: ").setBold())
                                .add(String.valueOf(rentApart.getMonthrent())
                                                + " tháng"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                document.add(new Paragraph().add(new Text("Ngày bắt đầu hợp đồng: ").setBold())
                                .add(rentApart.getCreatedate().format(dateFormatter)));
                document.add(new Paragraph().add(new Text("Ngày kết thúc hợp đồng: ").setBold())
                                .add(rentApart.getEnddate()
                                                .format(formatter)));
                document.add(line);
                // Add Terms and Conditions
                document.add(new Paragraph("CÁC ĐIỀU KHOẢN VÀ ĐIỀU KIỆN").setBold()
                                .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("Quyền và nghĩa vụ của chủ nhà:").setBold());
                document.add(new Paragraph(
                                "1. Đảm bảo phòng trọ và các thiết bị trong tình trạng hoạt động tốt."));
                document.add(new Paragraph(
                                "2. Thực hiện bảo trì, sửa chữa khi cần thiết (trừ khi do lỗi của người thuê gây ra)."));
                document.add(new Paragraph("Quyền và nghĩa vụ của người thuê:").setBold());
                document.add(new Paragraph(
                                "1. Sử dụng phòng trọ đúng mục đích, không gây tiếng ồn, không làm ảnh hưởng đến các phòng trọ khác."));
                document.add(new Paragraph(
                                "2. Bảo quản tài sản và các thiết bị trong phòng, thông báo ngay khi có hỏng hóc."));
                document.close();
        } catch (Exception e) {
                e.printStackTrace();
        }

        return pdfPath;
}
}
