package com.poly.services;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.AccountDAO;
import com.poly.dao.RentApartmentDAO;
import com.poly.entity.Account;
import com.poly.entity.MailInfo;
import com.poly.entity.RentApartment;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class MailerServiceImpl implements MailerService{
	List<MailInfo> list = new ArrayList<>();
	@Autowired
	JavaMailSender sender;
	@Override
	public void send(MailInfo mail) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		// Sử dụng Helper để thiết lập các thông tin cần thiết cho message
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
		helper.setFrom(mail.getFrom());
		helper.setTo(mail.getTo());
		helper.setSubject(mail.getSubject());
		helper.setText(mail.getBody(), true);
		helper.setReplyTo(mail.getFrom());

		String[] cc = mail.getCc();
		//Kiểm tra mảng cc có tồn tại hay không
		if (cc != null && cc.length > 0) {
			helper.setCc(cc);
		}
		
		String[] bcc = mail.getBcc();
		//Kiểm tra mảng bcc có tồn tại hay không
		if (bcc != null && bcc.length > 0) {
			helper.setBcc(bcc);
		}
		//Mảng file
		List<File> files = mail.getFiles();
		if (files.size()>0) {
			for (File file:files) {
				helper.addAttachment(file.getName(), file);
			}
		}
		// Gửi message đến SMTP server
		sender.send(message);
		
	}

	@Override
	public String send(String to, String subject, String body) throws MessagingException {
		subject = "Confirm Register";
		String code = generateRandomCode();
		body = "Your 6 digits are " + code; 
		this.send(new MailInfo(to, subject, body));
		return code;
		
	}
	
	private String generateRandomCode() {
	    Random random = new Random();
	    int code = 100000 + random.nextInt(900000); // Số ngẫu nhiên từ 100000 đến 999999
	    return String.format("%06d", code); // Chuyển đổi số thành chuỗi với đúng 6 chữ số
	}

	@Override
	public void queue(MailInfo mail) {
		list.add(mail);
		
	}

	@Override
	public void queue(String to, String subject, String body) {
		queue(new MailInfo(to, subject, body));
		
	}
	
	@Scheduled(fixedDelay = 5000)
	public void run() {
		while (!list.isEmpty()) {
			MailInfo mail = list.remove(0);
			try {
				this.send(mail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Autowired
    public MailerServiceImpl(JavaMailSender javaMailSender) {
        this.sender = javaMailSender;
    }
	
	@Async
    public void sendEmail(SimpleMailMessage email) {
		sender.send(email);
    }
	
	// gửi email kèm file pdf hợp đồng
		@Override
		public void sendContractEmail(String to, String pdfPath) throws MessagingException {
			MailInfo mail = new MailInfo();
			mail.setTo(to);
			mail.setSubject("Xác nhận thanh toán thành công");
			// Nội dung email
			StringBuilder body = new StringBuilder();
			body.append("Kính gửi Quý khách,\n\n");
			body.append("Chúng tôi xin chân thành cảm ơn Quý khách đã sử dụng dịch vụ của chúng tôi.\n\n");
			body.append("Chúng tôi xin thông báo rằng thanh toán của Quý khách đã được thực hiện thành công. ");
			body.append("Quý khách vui lòng kiểm tra file PDF đính kèm để xem chi tiết hóa đơn.\n\n");
			body.append(
					"Nếu Quý khách có bất kỳ câu hỏi hoặc cần hỗ trợ thêm, vui lòng liên hệ với chúng tôi qua email này hoặc số điện thoại 098888998.\n\n");
			body.append("Một lần nữa, xin cảm ơn Quý khách đã tin tưởng và sử dụng dịch vụ của chúng tôi.\n\n");
			body.append("Trân trọng,\n");
			body.append("[0988989888\n");
			body.append("duccongsos@gmail.com.");

			mail.setBody(body.toString());

			List<File> files = new ArrayList<>();
			files.add(new File(pdfPath));
			mail.setFiles(files);
			this.send(mail);
		}
		
		public void sendExpiryNotification(String to, String username, LocalDateTime endDate) throws MessagingException {
			String subject = "Thông báo hết hạn thuê";
			String body = String.format(
					"Chào %s,\n\nHợp đồng thuê của bạn sẽ hết hạn vào ngày %s. Vui lòng gia hạn hợp đồng nếu muốn thuê thêm.\n\nTrân trọng,\nĐội ngũ hỗ trợ",
					username, endDate);
			this.send(new MailInfo(to, subject, body));
		}
		
		// Phương thức mới để gửi email tự động khi gần hết hạn
		@Autowired
		private AccountDAO accountDAO;
		@Autowired
		RentApartmentDAO reportDao;
		
		
		@Scheduled(cron = "0 44 22 * * ?")
		public void checkAndSendExpiryNotifications() {
			LocalDateTime currentDate = LocalDateTime.now();
			LocalDateTime startDate = currentDate.plusDays(1);
			LocalDateTime endDate = currentDate.plusDays(6);

			List<RentApartment> rentedItemsWithEndDate = reportDao.getRentedWithEndDate(startDate, endDate);

			if (!rentedItemsWithEndDate.isEmpty()) {
				for (RentApartment item : rentedItemsWithEndDate) {
					Account account = accountDAO.findByUsername(item.getAccount().getUsername());
					try {
						sendExpiryNotification(account.getUsername(), item.getAccount().getUsername(), item.getEnddate());
						item.setAlertExpire(true);
						System.out.println("Sent email successfully to: " + account.getEmail());
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}
		}

}
