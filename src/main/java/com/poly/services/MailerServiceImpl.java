package com.poly.services;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.poly.dao.AccountDAO;
import com.poly.dao.RentApartmentDetailDAO;
import com.poly.entity.Account;
import com.poly.entity.MailInfo;
import com.poly.entity.Rented;

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
	public void sendExpiryNotification(String to, String username, LocalDateTime endDate) throws MessagingException {
        String subject = "Thông báo hết hạn thuê";
        String body = String.format("Chào %s,\n\nHợp đồng thuê của bạn sẽ hết hạn vào ngày %s. Vui lòng thực hiện các hành động cần thiết để gia hạn hoặc trả lại sản phẩm.\n\nTrân trọng,\nĐội ngũ hỗ trợ", username, endDate);
        this.send(new MailInfo(to, subject, body));
    }
	// Phương thức mới để gửi email tự động khi gần hết hạn
	@Autowired
	private AccountDAO accountDAO;
	@Autowired
	RentApartmentDetailDAO reportDao;
    @Scheduled(cron = "0 00 00 * * ?")
    public void checkAndSendExpiryNotifications() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.plusDays(1);
        LocalDateTime endDate = currentDate.plusDays(6);

        List<Rented> rentedItemsWithEndDate = reportDao.getRentedWithEndDate(startDate, endDate);

        if (!rentedItemsWithEndDate.isEmpty()) {
            for (Rented item : rentedItemsWithEndDate) {
                Account account = accountDAO.findByUsername(item.getPerson());
                try {
                    sendExpiryNotification(account.getEmail(), item.getPerson(), item.getEnddate());
                    System.out.println("Sent email successfully to: " + account.getEmail());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
