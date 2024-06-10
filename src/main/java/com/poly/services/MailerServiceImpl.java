package com.poly.services;

import java.io.File;
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

import com.poly.entity.MailInfo;

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

}
